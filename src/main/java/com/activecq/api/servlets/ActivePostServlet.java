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
package com.activecq.api.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 *
 * @author david
 */
public abstract class ActivePostServlet {//extends SlingPostServlet implements Servlet {
    
    protected abstract void beforeValidate(SlingHttpServletRequest request, SlingHttpServletResponse response);
    protected abstract boolean validate(SlingHttpServletRequest request, SlingHttpServletResponse response);
    protected abstract void afterValidate(SlingHttpServletRequest request, SlingHttpServletResponse response);
 
    protected abstract void beforeSave(SlingHttpServletRequest request, SlingHttpServletResponse response);
    protected abstract boolean save(SlingHttpServletRequest request, SlingHttpServletResponse response);
    protected abstract void afterSave(SlingHttpServletRequest request, SlingHttpServletResponse response);
    
    protected abstract void success(SlingHttpServletRequest request, SlingHttpServletResponse response);
    protected abstract void error(SlingHttpServletRequest request, SlingHttpServletResponse response);

    
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws IOException {

        if(!accepts(request, response)) { return; }

        beforeValidate(request, response);        
        if(!validate(request, response)) { error(request, response); }
        afterValidate(request, response);

        beforeSave(request, response);
        if(!save(request, response)) { error(request, response); }
        afterSave(request, response);

        success(request, response);         
    }   
        
    protected boolean accepts(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // By default assume the Servlet Service selection criteria is specific enough.
        return true;
    }
}

 