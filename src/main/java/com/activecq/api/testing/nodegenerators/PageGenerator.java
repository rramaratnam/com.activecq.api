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
public class PageGenerator implements Generator {
        
    private final String CONTENT_PREFIX = "/content";
    private String path = "unknown";
    private String designPath = "unknown";
    private String resourceType = "unknown";

    public PageGenerator(String name, String designPath, String resourceType, Session session) throws RepositoryException {
        this.path = CONTENT_PREFIX + "/" + name;
        this.designPath = designPath;
        this.resourceType = resourceType;
        build(session);
    }

    public PageGenerator(String resourceType, String designPath, Session session) throws RepositoryException {
        this.path = CONTENT_PREFIX + "/test/page";
        this.designPath = designPath;
        this.resourceType = resourceType;
        build(session);
    }
    
    public Node buildParResource(String relPath, String resourceType, Session session) throws RepositoryException {
        final String parPath = this.path + "/jcr:content/par/" + relPath;                
        
        Node node = JcrUtil.createPath(parPath, "nt:unstructured", session);
        node.setProperty("sling:resourceType", resourceType);
        session.save();
        
        return node;
    }
    
    public String makeContentPath(String relPath) {
        return this.path + "/jcr:content/" + relPath;
    }
    
    public String getDeletePath() {
        return this.path;
    } 
    
    public String getPath() {
        return this.path;
    }   

    private void build(Session session) throws RepositoryException {
        Node node = null;
        
        node = JcrUtil.createPath(this.path, "cq:Page", session);
        
        node = JcrUtil.createPath(node.getPath() + "/jcr:content", "cq:PageContent", session);
        node.setProperty("sling:resourceType", this.resourceType);
        node.setProperty("jcr:title", "Transient Test Page Resource");
        node.setProperty("pageTitle", "Transient Test Page Resource");
        node.setProperty("cq:designPath", this.designPath);        
        
        node = JcrUtil.createPath(node.getPath() + "/par", "nt:unstructured", session);
        node.setProperty("sling:resourceType", "foundation/components/parsys");        
        
        session.save();
    }    
}
