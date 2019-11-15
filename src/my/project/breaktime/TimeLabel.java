/*
 * The MIT License (MIT)
 * Copyright (c) 2019 GameplayJDK
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package my.project.breaktime;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class TimeLabel extends JLabel {
    private static final long serialVersionUID = -1377568046017931666L;

    private static final String FONT_NAME = "monospaced";
    private static final int FONT_SIZE = 16;

    private String formatText;

    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    public TimeLabel(String text) {
        this(text, null, null);
    }

    public TimeLabel(String formatText, LocalDateTime fromTime, LocalDateTime toTime) {
        this.formatText = formatText;

        this.fromTime = fromTime;
        this.toTime = toTime;

        this.initialize();
    }

    private void initialize() {
        if (this.isLabel()) {
            this.setText(this.formatText);
        }

        this.setForeground(Color.WHITE);

        this.setFont(this.createFont());
    }

    private Font createFont() {
        return new Font(TimeLabel.FONT_NAME, Font.PLAIN, TimeLabel.FONT_SIZE);
    }

    public void setTime(LocalDateTime now) {
        if (this.isLabel()) {
            return;
        }

        Duration fromDuration = Duration.between(now, this.fromTime);
        Duration toDuration = Duration.between(now, this.toTime);

        this.setForeground(this.findColor(now));

        this.setText(String.format(this.formatText, new Object[]{
                this.formatDuration(fromDuration),
                this.formatDuration(toDuration),
        }));
    }

    private boolean isLabel() {
        return (this.fromTime == null && this.toTime == null);
    }

    private Color findColor(LocalDateTime now) {
        if (now.isAfter(this.fromTime) && now.isBefore(this.toTime)) {
            return Color.CYAN;
        }

        if (now.isBefore(this.fromTime)) {
            return Color.RED;
        }

        if (now.isAfter(this.toTime)) {
            return Color.GREEN;
        }

        return Color.WHITE;
    }

    // Suggestion: Return 00:00:00 for expired (negative) durations.
    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long secondsAbsolute = Math.abs(seconds);

        String positive = String.format("%02d:%02d:%02d", new Object[]{
                ((secondsAbsolute / (60 * 60)) % 60),
                ((secondsAbsolute / (60)) % 60),
                ((secondsAbsolute) % 60),
        });

        return ((seconds < 0L) ? "-" : "+") + positive;
    }
}
