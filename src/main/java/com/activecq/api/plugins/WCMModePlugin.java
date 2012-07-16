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

import com.day.cq.wcm.api.WCMMode;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class WCMModePlugin extends BasePlugin {

    private WCMMode current;
    private WCMMode previous;
    private WCMMode original;

    public WCMModePlugin(SlingHttpServletRequest request) {
        super(request);
        this.original = WCMMode.fromRequest(this.request);
        this.previous = this.original;
        this.current = this.previous;
    }

    public void switchTo(WCMMode mode) {
        if (mode == null) {
            return;
        }
        
        this.previous = this.current;
        this.current = mode;
        this.current.toRequest(this.request);
    }

    public void switchBack() {
        this.previous.toRequest(this.request);
    }
    
    public void switchToOriginal() {
        this.previous = this.current;
        this.current = this.original;
        this.original.toRequest(this.request);
    }    
}
