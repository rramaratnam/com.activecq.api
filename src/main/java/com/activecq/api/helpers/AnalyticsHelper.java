/*
 * Copyright 2012 david.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api.helpers;

import com.activecq.api.utils.TypeUtil;
import java.util.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author david
 */
public class AnalyticsHelper {

    private Map<String, String> other = new HashMap<String, String>();
    public static final String DATA_PREFIX = "data-analytics-";

    public static final class Keys {
        public static final String JS_EVENTS = "js-events";
        public static final String EVENT = "event";
        public static final String VALUES = "values";
        public static final String COLLECT = "collect";
        public static final String OPTIONS = "options";
        public static final String COMPONENT_PATH = "componentPath"; 
        //public static final String ADHOC_EVARS = "adhocevars";
        //public static final String ADHOC_EVENTS = "adhocevents";
    }
    
    private List<String> jsEvents;
    private String event = "genericClick";
    private String values;
    private boolean collect = false;
    private String options = "{\"obj\": this}";
    private String componentPath;

    public AnalyticsHelper(String... options) {
        Map<String, String> map = TypeUtil.ArrayToMap(options);
        
        for (String key : map.keySet()) {
            if (StringUtils.equals(Keys.JS_EVENTS, key)) {
                this.jsEvents = Arrays.asList(StringUtils.split(map.get(key), ' '));
            } else if (StringUtils.equals(Keys.EVENT, key)) {
                this.event = map.get(key);
            } else if (StringUtils.equals(Keys.VALUES, key)) {
                this.values = map.get(key);
            } else if (StringUtils.equals(Keys.COLLECT, key)) {
                this.collect = Boolean.parseBoolean(map.get(key));
            } else if (StringUtils.equals(Keys.OPTIONS, key)) {
                this.options = map.get(key);
            } else if (StringUtils.equals(Keys.COMPONENT_PATH, key)) {
                this.componentPath = map.get(key);
            } else {
                this.other.put(key, map.get(key));
            }
        }
    }

    /**
     * Creates all the
     *
     * @return
     */
    @Override
    public String toString() {
        ArrayList<String> output = new ArrayList<String>();

        output.add(toDataDash(Keys.JS_EVENTS, StringUtils.join(this.jsEvents, ' ')));
        output.add(toDataDash(Keys.EVENT, this.event));
        output.add(toDataDash(Keys.VALUES, jsIfy(this.values)));
        output.add(toDataDash(Keys.COLLECT, Boolean.toString(this.collect).toLowerCase()));
        output.add(toDataDash(Keys.OPTIONS, jsIfy(this.options)));
        output.add(toDataDash(Keys.COMPONENT_PATH, this.componentPath));

        if (this.other.size() > 0) {
            for (String key : this.other.keySet()) {
                output.add(toDataDash(key, jsIfy(this.other.get(key))));
            }
        }

        return StringUtils.join(output, ' ');
    }

    /**
     * Creates the properly named data-* attribute for the passed key/value
     *
     * @param key
     * @param value
     * @return
     */
    public static String toDataDash(String key, String value) {
        if (StringUtils.stripToNull(value) == null) {
            return "";
        }

        return DATA_PREFIX.concat(key).concat("=\"").concat(value).concat("\"");
    }

    /**
     * Replaces values of 'this' with 'js:this', which the supporting JavaScript
     * will use to inject the actual JS this obj
     *
     * @param str
     * @return
     */
    private static String jsIfy(String str) {
        String thisReplacement = "js:this";

        str = StringUtils.stripToEmpty(str);
        if (StringUtils.equals("this", str)) {
            return thisReplacement;
        } else {
            return str.replaceAll("(:\\s*[^\"])this(\\s*)", ": \"" + thisReplacement + "\"");
        }
    }
}
