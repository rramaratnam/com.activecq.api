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
package com.activecq.api.utils;

import com.activecq.api.ActiveForm;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.jcr.PropertyType;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author david
 */
public class TypeUtil {

    private static final Pattern JSON_DATE = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}[-+]{1}[0-9]{2}[:]{0,1}[0-9]{2}$");
    private static final String REFERENCE = "jcr:reference:";
    private static final String PATH = "jcr:path:";
    private static final String NAME = "jcr:name:";
    private static final String URI = "jcr:uri:";

    /**
     * Turn a even length Array into a Map. The Array is expected to be in the
     * format: { key1, value1, key2, value2, ... , keyN, valueN }
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> Map<T, T> ArrayToMap(T[] list) {
        HashMap<T, T> map = new HashMap();
        if (list == null) {
            return map;
        }
        if (list.length > 0 && (list.length % 2) == 1) {
            throw new IllegalArgumentException("Array must be even in length, representing a series of Key, Value pairs.");
        }

        for (int i = 0; i < list.length; i++) {
            map.put(list[i], list[++i]);
        }

        return map;
    }

    /**
     * Converts a JSONObject to a simple Map. This will only work properly for
     * JSONObjects of depth 1.
     *
     * @param json
     * @return
     */
    public static Map toMap(JSONObject json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> keys = IteratorUtils.toList(json.keys());

        for (String key : keys) {
            try {
                map.put(key, json.get(key));
            } catch (JSONException ex) {
                Logger.getLogger(ActiveForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return map;
    }

    // TODO: Write JUnit Test
    /*
     * public static int getPropertyType(String key, Object object) { if (object
     * instanceof Double || object instanceof Float) { return
     * PropertyType.DOUBLE; } else if (object instanceof Number) { return
     * PropertyType.LONG; } else if (object instanceof Boolean) { return
     * PropertyType.BOOLEAN; } else if (object instanceof String) { if
     * (key.startsWith(REFERENCE)) { return PropertyType.REFERENCE; } if
     * (key.startsWith(PATH)) { return PropertyType.PATH; } if
     * (key.startsWith(NAME)) { return PropertyType.NAME; } if
     * (key.startsWith(URI)) { return PropertyType.URI; } if
     * (JSON_DATE.matcher((String) object).matches()) { return
     * PropertyType.DATE; } }
     *
     * return PropertyType.STRING; }
     */
    public static <T> Class<T> getType(Object object) {
        if (object instanceof Double || object instanceof Float) {
            return (Class<T>) Double.class;
        } else if (object instanceof Number) {
            return (Class<T>) Long.class;
        } else if (object instanceof Boolean) {
            return (Class<T>) Boolean.class;
        } else if (object instanceof String) {
            if (JSON_DATE.matcher((String) object).matches()) {
                return (Class<T>) Date.class;
            }
        }

        return (Class<T>) String.class;
    }

    public static <T> T toObjectType(String data, Class<T> klass) {
        if(Double.class.equals(klass)) {    
            try {                
                return klass.cast(Double.parseDouble(data));
            } catch (NumberFormatException ex) {
                return null;
            }
        } else if(Long.class.equals(klass)) {    
            try {
                return klass.cast(Long.parseLong(data));
            } catch (NumberFormatException ex) {
                return null;
            }
        } else if (StringUtils.equalsIgnoreCase("true", data)) {
            return klass.cast(Boolean.TRUE);
        } else if (StringUtils.equalsIgnoreCase("false", data)) {
            return klass.cast(Boolean.FALSE);
        } else if (JSON_DATE.matcher(data).matches()) {
            return klass.cast(ISODateTimeFormat.dateTimeParser().parseDateTime(data).toDate());            
        } else {
            return klass.cast(data);
        }        
    }
}
