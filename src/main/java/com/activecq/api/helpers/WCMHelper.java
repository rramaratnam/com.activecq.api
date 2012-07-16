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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;

public interface WCMHelper {

    public boolean isDesignMode(SlingHttpServletRequest request);

    public boolean isDisabledMode(SlingHttpServletRequest request);

    public boolean isEditMode(SlingHttpServletRequest request);

    public boolean isPreviewMode(SlingHttpServletRequest request);

    public boolean isReadOnlyMode(SlingHttpServletRequest request);

    public boolean isAuthoringMode(SlingHttpServletRequest request);

    public boolean isAuthor();

    public boolean isPublish();

    public boolean printEditBlock(SlingScriptHelper sling,
            WCMEditType editType,
            boolean... conditions);
}