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
package com.activecq.api.services.impl;

import com.activecq.api.ActiveForm;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;

public class ActiveResourceManagerImpl {

    public boolean save(String path, ActiveForm form, ResourceResolver resourceResolver) throws PathNotFoundException, RepositoryException {
        if (resourceResolver == null) {
            return false;
        }

        Session session = resourceResolver.adaptTo(Session.class);
        if (session == null) {
            return false;
        }

        Node parent = session.getNode(path);

        return false;
    }

    private boolean saveProperty(Node node, String property, Object value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        if (isMultiNodePropertyPath(property)) {
            List<String> nodes = this.getNodesFromProperty(property);
            property = this.getProperty(property);

            for (String subNode : nodes) {
                if (node.hasNode(subNode)) {
                    node = node.getNode(subNode);
                } else {
                    node.addNode(subNode);
                    node = node.getNode(subNode);
                }
            }
        }

        property = this.getProperty(property);

        if (value == null) {
            node.setProperty(property, (String) null);
        } else {
            setProperty(node, property, value);
        }

        node.getSession().save();
        
        return true;
    }

    private boolean isMultiNodePropertyPath(String path) {
        return !StringUtils.contains(StringUtils.removeStart(path, "./"), "/");
    }

    private List<String> getNodesFromProperty(String path) {
        path = StringUtils.removeStart(path, "./");
        List<String> nodes = Arrays.asList(StringUtils.split(path, '/'));
        nodes.remove(nodes.size());
        return nodes;
    }

    private String getProperty(String path) {
        path = StringUtils.removeStart(path, "./");
        List<String> nodes = Arrays.asList(StringUtils.split(path, '/'));
        return nodes.get(nodes.size());
    }

    private boolean setProperty(Node node, String property, Object value) {
        if(node == null || property == null || value == null) {
            return false;
        }
        
        try {
            if (value instanceof String) {
                node.setProperty(property, (String) value);
            } else if (value instanceof BigDecimal) {
                node.setProperty(property, (BigDecimal) value);
            } else if (value instanceof Binary) {
                node.setProperty(property, (Binary) value);
            } else if (value instanceof Calendar) {
                node.setProperty(property, (Calendar) value);
            } else if (value instanceof InputStream) {
                node.setProperty(property, (InputStream) value);
            } else if (value instanceof Node) {
                node.setProperty(property, (Node) value);
            } else if (value instanceof String[]) {
                node.setProperty(property, (String[]) value);
            } else if (value instanceof Value) {
                node.setProperty(property, (Value) value);
            } else if (value instanceof Value[]) {
                node.setProperty(property, (Value[]) value);
            } else if (value instanceof Boolean) {
                node.setProperty(property, ((Boolean) value).booleanValue());
            } else if (value instanceof Double) {
                node.setProperty(property, ((Double) value).doubleValue());
            } else if (value instanceof Long) {
                node.setProperty(property, ((Long) value).longValue());
            } else if (value instanceof List) {
                List<String> tmp = (List<String>) value;
                String[] tmp2 = tmp.toArray(new String[0]);
                node.setProperty(property, tmp2);
            } else {
                node.setProperty(property, value.toString());
            }
            
            return true;

        } catch (ValueFormatException ex) {
            Logger.getLogger(ActiveResourceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (VersionException ex) {
            Logger.getLogger(ActiveResourceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LockException ex) {
            Logger.getLogger(ActiveResourceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConstraintViolationException ex) {
            Logger.getLogger(ActiveResourceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(ActiveResourceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
