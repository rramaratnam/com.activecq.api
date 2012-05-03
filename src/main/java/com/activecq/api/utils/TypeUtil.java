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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.IteratorUtils;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

/**
 *
 * @author david
 */
public class TypeUtil {
    
    /** 
     * Turn a even length Array into a Map. The Array is expected to be in the format:
     *  { key1, value1, key2, value2, ... , keyN, valueN }
     * 
     * @param <T>
     * @param list
     * @return 
     */
    public static <T> Map<T, T> ArrayToMap(T[] list) {
        HashMap<T, T> map = new HashMap();     
        if(list == null) { return map; }
        if(list.length > 0 && (list.length % 2) == 1) {
            throw new IllegalArgumentException("Array must be even in length, representing a series of Key, Value pairs.");
        }
        
        for(int i = 0; i < list.length; i++) {
            map.put(list[i], list[++i]);
        }
        
        return map;
    }
        
    /** 
     * Converts a JSONObject to a simple Map. This will only work properly for 
     * JSONObjects of depth 1.
     * @param json
     * @return 
     */
    public static Map toMap(JSONObject json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> keys = IteratorUtils.toList(json.keys());
        
        for(String key : keys) {
            try {
                map.put(key, json.get(key));
            } catch (JSONException ex) {
                Logger.getLogger(ActiveForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return map;
    }      
}
