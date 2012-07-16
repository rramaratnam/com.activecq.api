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
public class ContentResourceGenerator implements Generator {
        
    private final String CONTENT_PREFIX = "/content";
    private String path = "unknown";
    private String resourceType = "unknown";
    private String name = "unknown";
    private Node node = null;
    
    public ContentResourceGenerator(Session session, String name, String resourceType) throws RepositoryException {
        this.path = CONTENT_PREFIX + "/" + name;
        this.resourceType = resourceType;
        this.name = name;
        build(session);
    }
    
    public String makeContentPath(String relPath) {
        return CONTENT_PREFIX + "/jcr:content/" + relPath;
    }
    
    public String getDeletePath() {
        return this.path;
    }     
    
    public String getPath() {
        return this.path;
    }

    private void build(Session session) throws RepositoryException {        
        this.node = JcrUtil.createPath(this.path, "nt:unstructured", session);        
        session.save();
    }    
}
