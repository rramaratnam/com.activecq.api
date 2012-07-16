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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author david
 */
public class OsgiPropertyUtil {
    
    private OsgiPropertyUtil() { } 
    
    public static AbstractMap.SimpleEntry toSimpleEntry(String value, String separator) {
        String[] tmp = StringUtils.split(value, separator);
        
        if(tmp == null) { return null; }
        
        if(tmp.length == 2) {
            return new AbstractMap.SimpleEntry(tmp[0], tmp[1]);
        } else {
            return null;
        }
    }
    
    public static Map<String, String> toMap(String[] values, String separator) {
        Map<String, String> map = new HashMap<String, String>();
        
        if(values == null || values.length < 1) { return map; }
        
        for(String value : values) {
            String[] tmp = StringUtils.split(value, separator);
            if (tmp.length == 2) {
                map.put(tmp[0], tmp[1]);
            } 
        }
       
        return map;
    }    
}
