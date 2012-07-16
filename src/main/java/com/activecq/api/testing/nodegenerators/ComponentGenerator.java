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
package com.activecq.api.testing.nodegenerators;

import com.day.cq.commons.jcr.JcrUtil;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 *
 * @author david
 */
public class ComponentGenerator implements Generator {
        
    private final String APPS_PREFIX = "/apps";
    private String path = "unknown";
    private String resourceType = "unknown";
    private String name = "unknown";

    public ComponentGenerator(Session session, String resourceType) throws RepositoryException {
        this.path = APPS_PREFIX + "/" + resourceType;
        this.resourceType = resourceType;
        build(session);
    }
    
    public String getName() {
        return this.name;
    }
       
    public String getResourceType() {
        return this.resourceType;
    }    
    
    public String getDeletePath() {
        return this.path;
    }
    
    public String getPath() {
        return this.path;
    }
    
    private void build(Session session) throws RepositoryException {
        Node node;
        
        node = JcrUtil.createPath(this.path, "cq:Component", session);        
        node.setProperty("sling:resourceType", "wcm/core/components/designer");
        node.setProperty("jcr:title", "Transient Test Component Resource");
        node.setProperty("jcr:description", "Transient Test Component Resource");
        node.setProperty("cq:noDecoration", true);        
        node.setProperty("componentGroup", ".hidden");        
        
        this.name = node.getName();
        
        session.save();
    }    
}
