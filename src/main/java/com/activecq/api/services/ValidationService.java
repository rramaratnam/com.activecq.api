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
package com.activecq.api.services;

import com.activecq.api.ActiveForm;

/**
 *
 * @author david
 */
public interface ValidationService {
     
    public enum Comparisons {
        LESS_THAN,
        LESS_THAN_OR_EQUAL_TO,
        EQUAL_TO,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL_TO
    };
    
    public boolean isPresent(ActiveForm form, String key);
    public boolean isValidPath(ActiveForm form, String key);

    public boolean isConfirmed(ActiveForm form, String key);
    public <T> boolean isNotIn(ActiveForm form, String key, T... blacklist);
    public <T> boolean isIn(ActiveForm form, String key, T... whitelist);

    //public <T> boolean isValue(ActiveResource form, String key, T value, Comparisons... comparisons);
    public boolean isLength(ActiveForm form, String key, Comparisons comparison, int length);

    public boolean isNumber(ActiveForm form, String key);
    public boolean isOfFormat(ActiveForm form, String key, String regex);
}
