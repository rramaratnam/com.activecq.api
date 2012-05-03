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
package com.activecq.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class ActiveProperties {

    /**
     * Uses reflection to create a List of the keys defined in the ActiveField subclass (and its inheritance tree).
     * Only fields that meet the following criteria are returned:
     * - public
     * - static
     * - final
     * - String
     * - Upper case 
     * @return a List of all the Fields
     */
    @SuppressWarnings("unchecked")
    public List<String> getKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        Class cls = this.getClass();

        if(cls == null) {
            return Collections.unmodifiableList(keys);
        }

        Field fieldList[] = cls.getFields();

        for (Field fld : fieldList) {
            int mod = fld.getModifiers();

            // Only look at public static final fields
            if (!Modifier.isPublic(mod) || !Modifier.isStatic(mod) || !Modifier.isFinal(mod)) {
                continue;
            }

            // Only look at String fields
            if (!String.class.equals(fld.getType())) {
                continue;
            }

            // Only look at upper case fields
            if (!StringUtils.equals(fld.getName().toUpperCase(), fld.getName())) {
                continue;
            }

            // Get the value of the field
            String value = null;
            try {
                value = StringUtils.stripToNull((String) fld.get(this));
            } catch (IllegalArgumentException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }

            // Do not add duplicate or null keys, or previously added named fields
            if (value == null || names.contains(fld.getName()) || keys.contains(value)) {
                continue;
            }

            // Success! Add key to key list
            keys.add(value);

            // Add field named to process field names list
            names.add(fld.getName());

        }

        return Collections.unmodifiableList(keys);
    }

    /**
     * Return the number of Fields keys
     * @return number of Fields
     */
    public int size() {
        return getKeys().size();
    }
   
    /** 
     * Convenience method
     * 
     * Calls "size()"
     * @return number of Fields
     */
    public int length() {
        return this.size();
    }
}
