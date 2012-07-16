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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author david
 */
public class DesignGenerator implements Generator {
    
    public static final String CLIENTLIB = "test.clientlib";
    
    private final String DESIGN_PREFIX = "/etc/designs";
    private String path = "unknown";

    public DesignGenerator(Session session, String name) throws RepositoryException {
        this.path = DESIGN_PREFIX + "/" + name;
        build(session);
    }

    public DesignGenerator(Session session) throws RepositoryException {
        this.path = DESIGN_PREFIX + "/test";
        build(session);
    }
    
    public String makeContentPath(String relPath) {
        return DESIGN_PREFIX + "/jcr:content/" + relPath;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getDeletePath() {
        return this.path;
    }
    
    private void build(Session session) throws RepositoryException {
        Node node;
        
        JcrUtil.createPath(this.path, "cq:Page", session);
        
        node = JcrUtil.createPath(this.path + "/jcr:content", "cq:PageContent", session);
        node.setProperty("sling:resourceType", "wcm/core/components/designer");
        node.setProperty("jcr:title", "Transient Test Design Node");
        node.setProperty("cq:doctype", "html5");        
        
        JcrUtil.createPath(this.path + "/images", "nt:folder", session);
        
        JcrUtil.createPath(this.path + "/styles", "nt:folder", session);
        
        node = JcrUtil.createPath(this.path + "/clientlibs", "cq:ClientLibraryFolder", session);
        node.setProperty("categories",  new String[] { CLIENTLIB });
        
        session.save();
    }    
    
    public Node buildContentResource(Session session, String relPath) throws RepositoryException {
        return buildContentResource(session, relPath, null);
    }     
    
    public Node buildContentResource(Session session, String relPath, String resourceType) throws RepositoryException {
        final String parPath = this.path + "/jcr:content/" + relPath;                
        
        Node node = JcrUtil.createPath(parPath, "nt:unstructured", session);
        if(StringUtils.stripToNull(resourceType) != null) {
            node.setProperty("sling:resourceType", resourceType);
        }
        session.save();
        
        return node;
    } 
}
