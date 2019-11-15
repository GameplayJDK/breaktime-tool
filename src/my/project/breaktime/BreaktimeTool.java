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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BreaktimeTool extends JFrame implements ActionListener, FocusListener {
    private static final long serialVersionUID = 4468363829659627748L;

    private static final String[] IMAGE_PATHS = {
            "/icon-128.png",
            "/icon-64.png",
            "/icon-32.png",
            "/icon-16.png",
    };

    private static final int LABEL_LIMIT = 64;

    private Configuration configuration;

    private TimeLabel[] label;

    private Timer timer;

    public BreaktimeTool() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setTitle("Breaktime Tool");

        this.initialize();

        this.pack();
        this.setLocationRelativeTo(null);

        this.setAlwaysOnTop(true);
    }

    private void initialize() {
        this.setLayout(this.createLayoutManager());

        this.setIconImages(this.createIconImages(BreaktimeTool.IMAGE_PATHS));
        this.getContentPane().setBackground(Color.BLACK);

        this.configuration = new Configuration();

        this.label = new TimeLabel[BreaktimeTool.LABEL_LIMIT];

        this.initializeData();

        this.timer = new Timer(1000, this);
        this.timer.setInitialDelay(1000);
        this.timer.start();

        this.actionPerformed(null);

        this.addFocusListener(this);
    }

    private void initializeData() {
        this.configuration.initialize();

        LocalDate today = LocalDate.now();

        Configuration.Value[] valueArray = this.configuration.getValueArray(today.getDayOfWeek().name(), BreaktimeTool.LABEL_LIMIT);

        int n = 0;

        for (Configuration.Value value : valueArray) {
            n++;

            if (null == value) {
                continue;
            }

            if (!value.hasFromTime() || !value.hasToTime()) {
                this.addTimeLabel(n, this.createLabel(value.getLabel()));
            } else {
                this.addTimeLabel(n,
                        this.createLabel(value.getLabel() + "%1$s   %2$s",
                                LocalDateTime.of(today, LocalTime.of(value.getFromHour(), value.getFromMinute())),
                                LocalDateTime.of(today, LocalTime.of(value.getToHour(), value.getToMinute()))));
            }
        }
    }

    private LayoutManager createLayoutManager() {
        return new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
    }

    private TimeLabel createLabel(String text) {
        return this.createLabel(text, null, null);
    }

    private TimeLabel createLabel(String formatText, LocalDateTime fromTime, LocalDateTime toTime) {
        return new TimeLabel(formatText, fromTime, toTime);
    }

    private List<Image> createIconImages(String[] imagePaths) {
        List<Image> images = new ArrayList<Image>();

        for (String imagePath : imagePaths) {
            images.add(this.createIconImage(imagePath));
        }

        return images;
    }

    private Image createIconImage(String imagePath) {
        return Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(imagePath));
    }

    private void addTimeLabel(int index, TimeLabel label) {
        if (index > -1 && index < this.label.length) {
            this.add(this.label[index] = label);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        for (TimeLabel label : this.label) {
            if (label == null) {
                continue;
            }

            label.setTime(LocalDateTime.now());
        }
    }

    @Override
    public void focusGained(FocusEvent event) {
        this.setOpacity(1.00f);
    }

    @Override
    public void focusLost(FocusEvent event) {
        this.setOpacity(0.65f);
    }
}
