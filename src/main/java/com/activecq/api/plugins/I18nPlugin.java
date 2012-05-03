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
package com.activecq.api.plugins;

import com.day.cq.i18n.I18n;
import java.util.ResourceBundle;

/**
 *
 * @author david
 */
public class I18nPlugin {

    private boolean enableI18n;
    private I18n i18n;

    /**
     * Constructor
     *
     * @param exposed
     */
    public I18nPlugin(ExposedPlugin exposed) {
        this.i18n = new I18n(exposed.getRequest());
        this.enableI18n = (this.i18n != null);
    }

    /**
     * Enable/Disbled I18n processing when getting properties
     *
     * @param enable
     */
    public void enable(boolean enable) {
        this.enableI18n = enable;
    }

    /**
     * Checks if i18n is enabled
     *
     * @return
     */
    public boolean isEnabled() {
        return this.enableI18n;
    }

    /**
     * Set i18n resource bundle; by default set to the Request
     *
     * @param resourceBundle
     */
    public void setI18n(ResourceBundle resourceBundle) {
        this.i18n = new I18n(resourceBundle);
    }

    /**
     * Translate the supplied string
     *
     * @param str
     * @return
     */
    public String translate(String str) {
        if (isEnabled()) {
            return this.i18n.get(str);
        } else {
            return str;
        }
    }
}
