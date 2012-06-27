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
package com.activecq.api.test;

import com.activecq.api.testing.RemoteTester;
import com.day.cq.commons.jcr.JcrUtil;
import javax.jcr.Node;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Execute test from: http://localhost:5503/system/sling/junit/
 *
 * @author david
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class ActiveComponentTest {

    @TestReference
    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;
    private static RemoteTester remote;

    public final static class Constants {

        public static final String COMPONENT_NAME = "active-component";
        public static final String ROOT_DESIGN_PATH = "/etc/designs/testing";
        public static final String ROOT_CONTENT_PATH = "/content/testing";
        public static final String DESIGN_PATH = ROOT_DESIGN_PATH + "/activecq/active-component";
        public static final String DESIGN_RESOURCE_PATH = DESIGN_PATH + "/jcr:content/test-harness/par/test-1";
        public static final String CONTENT_PATH = ROOT_CONTENT_PATH + "/activecq";
        public static final String CONTENT_PAGE_PATH = CONTENT_PATH + "/active-component";
        public static final String CONTENT_RESOURCE_PATH = CONTENT_PAGE_PATH + "/jcr:content/par/test-1";
        public static final String EXTRA_RESOURCE_PATH = CONTENT_PAGE_PATH + "/jcr:content/extra";
        // Resource Property Names
        public static final String PROPERTY_PLAIN_TEXT = "plain-text";
        public static final String PROPERTY_RICH_TEXT = "rich-text";
        public static final String PROPERTY_DOUBLE = "double";
        public static final String PROPERTY_LONG = "long";
        public static final String PROPERTY_BOOLEAN = "boolean";
        public static final String PROPERTY_STR_ARRAY = "str-array";
        // Design Properties
        public static final String DESIGN_PLAIN_TEXT = "This is plain text from the CQ Design property";
        public static final String DESIGN_RICH_TEXT = "<p>This is <em>rich text</em> from the CQ Design property</p>";
        public static final double DESIGN_DOUBLE = 12388.123D;
        public static final long DESIGN_LONG = 89399990L;
        public static final boolean DESIGN_BOOLEAN = true;
        public static final String[] DESIGN_STR_ARRAY = new String[]{"Design Array Element 1", "Design Array Element 2"};
        // Content Resource Properties
        public static final String CONTENT_PLAIN_TEXT = "This is plain text from the CQ Resource property";
        public static final String CONTENT_RICH_TEXT = "<p>This is <em>rich text</em> from the Content Resource property</p>";
        public static final double CONTENT_DOUBLE = 28894.24D;
        public static final long CONTENT_LONG = 123339L;
        public static final boolean CONTENT_BOOLEAN = false;
        public static final String[] CONTENT_STR_ARRAY = new String[]{"Content Array Element 1", "Content Array Element 2"};
        // Extra Resource Properties
        public static final String EXTRA_PLAIN_TEXT = "This is plain text from an Extra Resource property";
        public static final String EXTRA_RICH_TEXT = "<p>This is <em>rich text</em> from an Extra Resource property</p>";
        public static final double EXTRA_DOUBLE = 1237.00012D;
        public static final long EXTRA_LONG = 29L;
        public static final boolean EXTRA_BOOLEAN = true;
        public static final String[] EXTRA_STR_ARRAY = new String[]{"Extra Array Element 1", "Extra Array Element 2"};
    }

    public ActiveComponentTest() {
    }

    @BeforeClass
    public static void setupClass() {
        remote = new RemoteTester("admin", "admin", "http://localhost:5502" + Constants.CONTENT_PAGE_PATH, "GET");
    }

    @Before
    public void setUp() throws Exception {
        resourceResolver = getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        Node node = null;
        
        /** DESIGN NODES **/
        
        // Etc Design Nodes
        JcrUtil.createPath(Constants.DESIGN_PATH, "cq:Page", session);
        session.save();

        // Design PageContent Node
        node = JcrUtil.createPath(Constants.DESIGN_PATH.concat("/jcr:content"), "nt:unstructured", session);
        node.setProperty("sling:resourceType", "wcm/core/components/designer");
        node.setProperty("jcr:title", "Active Component Design for Testing");
        node.setProperty("cq:doctype", "html5");
        session.save();

        // Component Design Properties
        node = JcrUtil.createPath(Constants.DESIGN_RESOURCE_PATH, "nt:unstructured", session);
        node.setProperty("sling:resourceType", "activecq/api/test/activecomponent");
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.DESIGN_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.DESIGN_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.DESIGN_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.DESIGN_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.DESIGN_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.DESIGN_BOOLEAN);
        session.save();

        /** CONTENT NODES **/
        
        // Page
        JcrUtil.createPath(Constants.CONTENT_PATH, "nt:folder", session);
        session.save();

        JcrUtil.createPath(Constants.CONTENT_PAGE_PATH, "cq:Page", session);
        session.save();

        // Page Content
        node = JcrUtil.createPath(Constants.CONTENT_PAGE_PATH + "/jcr:content", "cq:PageContent", session);
        node.setProperty("sling:resourceType", "activecq/components/templates/test-harness");
        node.setProperty("cq:designPath", Constants.DESIGN_PATH);
        session.save();

        // ParSys
        node = JcrUtil.createPath(Constants.CONTENT_PAGE_PATH + "/jcr:content/par", "nt:unstructured", session);
        node.setProperty("sling:resourceType", "activecq/api/test/support/parsys");
        session.save();

        // Component 1
        node = JcrUtil.createPath(Constants.CONTENT_PAGE_PATH + "/jcr:content/par/test-1", "nt:unstructured", session);
        node.setProperty("sling:resourceType", "activecq/api/test/activecomponent");
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.CONTENT_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.CONTENT_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.CONTENT_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.CONTENT_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.CONTENT_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.CONTENT_BOOLEAN);
        session.save();

        // Extra Resource 
        node = JcrUtil.createPath(Constants.EXTRA_RESOURCE_PATH, "nt:unstructured", session);
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.EXTRA_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.EXTRA_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.EXTRA_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.EXTRA_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.EXTRA_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.EXTRA_BOOLEAN);
        session.save();
    }

    @After
    public void tearDown() throws Exception {
        resourceResolver = getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        session.getNode(Constants.ROOT_DESIGN_PATH).remove();
        session.save();

        session.getNode(Constants.ROOT_CONTENT_PATH).remove();
        session.save();

        if (resourceResolver != null) {
            resourceResolver.close();
            resourceResolver = null;
        }
    }

    /** REMOTE TEST **/
    
    @Test
    public void test_getComponent() {
        remote.execute("getComponent");
    }

    @Test
    public void test_getComponentContext() {
        remote.execute("getComponentContext");
    }

    @Test
    public void test_getDesign() {
        remote.execute("getDesign");
    }

    @Test
    public void test_getDesignProperties() {
        remote.execute("getDesignProperties");
    }

    @Test
    public void test_getDesignProperty() {
        remote.execute("getDesignProperty");
    }

    @Test
    public void test_getDesignResource() {
        remote.execute("getDesignResource");
    }

    @Test
    public void test_getDesigner() {
        remote.execute("getDesigner");
    }

    @Test
    public void test_getEditContext() {
        remote.execute("getEditContext");
    }

    @Test
    public void test_getNode() {
        remote.execute("getNode");
    }

    @Test
    public void test_getPageManager() {
        remote.execute("getPageManager");
    }

    @Test
    public void test_getProperties() {
        remote.execute("getProperties");
    }

    @Test
    public void test_getProperty() {
        remote.execute("getProperty");
    }

    @Test
    public void test_getPropertyResource() {
        remote.execute("getPropertyResource");
    }

    @Test
    public void test_getQueryBuilder() {
        remote.execute("getQueryBuilder");
    }

    @Test
    public void test_getRequest() {
        remote.execute("getRequest");
    }

    @Test
    public void test_getRequestDesign() {
        remote.execute("getRequestDesign");
    }

    @Test
    public void test_getRequestPage() {
        remote.execute("getRequestPage");
    }

    @Test
    public void test_getResource() {
        remote.execute("getResource");
    }

    @Test
    public void test_getResourceDesign() {
        remote.execute("getResourceDesign");
    }

    @Test
    public void test_getResourcePage() {
        remote.execute("getResourcePage");
    }

    @Test
    public void test_getResourceResolver() {
        remote.execute("getResourceResolver");
    }

    @Test
    public void test_getReponse() {
        remote.execute("getReponse");
    }

    @Test
    public void test_getService() {
        remote.execute("getService");
    }

    @Test
    public void test_getStyle() {
        remote.execute("getStyle");
    }

    @Test
    public void test_hasNode() {
        remote.execute("hasNode");
    }

    /**
     * Private Method *
     */
    private ResourceResolver getResourceResolver() {
        try {
            if (resourceResolver == null) {
                resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            }
            return resourceResolver;
        } catch (LoginException e) {
            fail(e.toString());
        }
        return null;
    }
}