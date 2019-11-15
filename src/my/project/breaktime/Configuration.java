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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {

    private static final String PATH_INTERNAL = "/default.properties";

    private static final String PATH_EXTERNAL = "./configuration.properties";

    private static final String REGEX_KEY = "^%1$s\\.(\\d+)$";
    private static final String REGEX_VALUE = "^([\\w- ]+:?[ ]*)(?:(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2}))?$";

    private Properties properties;

    public Configuration() {
        this.properties = new Properties();
    }

    public void initialize() {
        InputStream stream = this.getStream();

        try {
            this.properties.load(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private InputStream getStream() {
        File configurationFile = new File(Configuration.PATH_EXTERNAL);
        Class<Configuration> configurationClass = Configuration.class;

        InputStream stream = null;

        try {
            if (configurationFile.exists()) {
                stream = new FileInputStream(configurationFile);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        if (null == stream) {
            stream = configurationClass.getResourceAsStream(Configuration.PATH_INTERNAL);
        }

        return stream;
    }

    public Configuration.Value[] getValueArray(String name, int limit) {
        name = name.toLowerCase();

        List<Configuration.Value> result = new ArrayList<Configuration.Value>(limit);
        for (int index = 0; index < limit; index++) {
            result.add(null);
        }

        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            int index = this.createKey(name, (String) entry.getKey());

            if (index < 0) {
                continue;
            }

            String valueString = (String) entry.getValue();
            Configuration.Value value = this.createValue(valueString);

            result.set(index, value);
        }

        Configuration.Value[] valueArray = new Configuration.Value[limit];

        return result.toArray(valueArray);
    }

    private int createKey(String name, String key) {
        final String regex = String.format(Configuration.REGEX_KEY, new Object[]{
                Pattern.quote(name),
        });

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);

        int index = -1;

        if (matcher.matches()) {
            try {
                index = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ex) {
            }
        }

        return index;
    }

    private Value createValue(String valueString) {
        Pattern pattern = Pattern.compile(Configuration.REGEX_VALUE);
        Matcher matcher = pattern.matcher(valueString);
        if (!matcher.matches()) {
            return null;
        }

        String label = matcher.group(1);

        Configuration.Value value = new Configuration.Value(label);


        int fromHour = -1;
        int fromMinute = -1;

        try {
            fromHour = Integer.parseInt(matcher.group(2));
            fromMinute = Integer.parseInt(matcher.group(3));
        } catch (NumberFormatException ex) {
        }

        if (fromHour > -1 && fromMinute > -1) {
            value.setFromTime(fromHour, fromMinute);
        }

        int toHour = -1;
        int toMinute = -1;

        try {
            toHour = Integer.parseInt(matcher.group(4));
            toMinute = Integer.parseInt(matcher.group(5));
        } catch (NumberFormatException ex) {
        }

        if (toHour > -1 && toMinute > -1) {
            value.setToTime(toHour, toMinute);
        }

        return value;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public static class Value {

        private String label;

        private int fromHour;
        private int fromMinute;

        private int toHour;
        private int toMinute;

        public Value(String label) {
            this.label = label;

            this.fromHour = -1;
            this.fromHour = -1;

            this.toHour = -1;
            this.toMinute = -1;
        }

        public String getLabel() {
            return this.label;
        }

        public int getFromHour() {
            return this.fromHour;
        }

        public int getFromMinute() {
            return this.fromMinute;
        }

        public void setFromTime(int hour, int minute) {
            this.fromHour = hour;
            this.fromMinute = minute;
        }

        public boolean hasFromTime() {
            return this.fromHour > -1 && this.fromMinute > -1;
        }

        public int getToHour() {
            return this.toHour;
        }

        public int getToMinute() {
            return this.toMinute;
        }

        public void setToTime(int hour, int minute) {
            this.toHour = hour;
            this.toMinute = minute;
        }

        public boolean hasToTime() {
            return this.toHour > -1 && this.toMinute > -1;
        }
    }
}
