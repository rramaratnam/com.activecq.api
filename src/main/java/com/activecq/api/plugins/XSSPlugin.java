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

import com.day.cq.xss.ProtectionContext;
import com.day.cq.xss.XSSProtectionException;
import com.day.cq.xss.XSSProtectionService;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author david
 */
public class XSSPlugin {

    private boolean enabled;
    private ProtectionContext protectionContext;
    private ArrayList<String> whitelist;
    private XSSProtectionService service;

    public XSSPlugin(CorePlugin exposed) {
        this.service = exposed.getService(XSSProtectionService.class);
        this.whitelist = new ArrayList<String>();
        this.enabled = (service != null);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Enabled XSS Protection for specified fields
     *
     * @param whitelist
     */
    public void enable(String... whitelist) {
        this.enabled = true;
        this.protectionContext = null;

        if (whitelist == null || whitelist.length < 1) {
            this.whitelist = new ArrayList<String>();
        } else {
            this.whitelist = new ArrayList<String>(Arrays.asList(whitelist));
        }
    }

    /**
     * Default enabling of XSS Protection using HTML_HTML_CONTENT on all
     * properties
     *
     * > Escape HTML for use inside element content (rules #6 and - to some
     * degree - #1), using a policy to remove potentially malicious HTML
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/xss/ProtectionContext.html
     *
     */
    public void enable() {
        enable(ProtectionContext.HTML_HTML_CONTENT);
    }

    /**
     * Enable XSS Protection using the supplied ProtectionContext
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/xss/ProtectionContext.html
     *
     * @param context
     */
    public void enable(ProtectionContext context) {
        enable(context, new String[0]);
    }

    /**
     * Enable XSS protection on the component
     *
     * @param context type of XSS protection
     * @param whitelist list of fields that do not require XSS protection
     */
    public void enable(ProtectionContext context, String... whitelist) {
        this.enabled = true;
        this.protectionContext = context;

        if (whitelist == null || whitelist.length < 1) {
            this.whitelist = new ArrayList<String>();
        } else {
            this.whitelist = new ArrayList<String>(Arrays.asList(whitelist));
        }
    }

    /**
     * Disable XSS Protection for the component
     */
    public void disable() {
        this.enabled = false;
        this.protectionContext = null;
    }

    /**
     * XSS Protect a specific string
     *
     * @param str
     * @return
     */
    public String protect(String str) {
        if (!isEnabled()) {
            return str;
        }

        try {
            if (this.hasContext()) {
                return this.getXSSProtectionService().protectForContext(this.getProtectionContext(), str);
            } else {
                return this.getXSSProtectionService().protectFromXSS(str);
            }
        } catch (XSSProtectionException e) {
            return "<!-- XSS Protection Error -->";
        }
    }
    
    public String protect(String str, String key) {
        if (!isEnabled() || isWhitelisted(key)) {
            return str;
        }

        try {
            if (this.hasContext()) {
                return this.getXSSProtectionService().protectForContext(this.getProtectionContext(), str);
            } else {
                return this.getXSSProtectionService().protectFromXSS(str);
            }
        } catch (XSSProtectionException e) {
            return "<!-- XSS Protection Error -->";
        }
    }

    /**
     * Checks if the field is whitelisted (does not require XSS protection)
     *
     * @param field
     * @return
     */
    protected boolean isWhitelisted(String field) {
        return this.whitelist.contains(field);
    }

    protected boolean hasContext() {
        return this.protectionContext != null;
    }

    protected ProtectionContext getProtectionContext() {
        return this.protectionContext;
    }

    private XSSProtectionService getXSSProtectionService() {
        return this.service;
    }

}
