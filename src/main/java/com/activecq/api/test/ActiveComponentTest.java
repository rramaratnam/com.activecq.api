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

import com.activecq.api.testing.AbstractRemoteTemplate;
import com.activecq.api.testing.RemoteTester;
import com.activecq.api.testing.nodegenerators.ComponentGenerator;
import com.activecq.api.testing.nodegenerators.DesignGenerator;
import com.activecq.api.testing.nodegenerators.PageGenerator;
import com.day.cq.commons.jcr.JcrUtil;
import javax.jcr.Node;
import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Execute test from: http://localhost:5503/system/sling/junit/
 *
 * @author david
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class ActiveComponentTest extends AbstractRemoteTemplate {

    private ComponentGenerator component;
    private DesignGenerator design;
    private PageGenerator page;    
    
    private String stylePath = "";
    private String testResourcePath = "";
    private String extraResourcePath = Constants.EXTRA_RESOURCE_PATH;
    
    
    @TestReference
    private ResourceResolverFactory resourceResolverFactory;

    public final static class Constants {
        public static final String COMPONENT_NAME = "active-component";        
        public static final String EXTRA_RESOURCE_PATH = "/content/test/page/jcr:content/par/extra";

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

    @Before
    public void setUp() throws Exception {        
        super.setUp(resourceResolverFactory);
        
        Node node;
        
        component = new ComponentGenerator(getSession(), "activecq/api/test/active-component");
        design = new DesignGenerator(getSession());
        page = new PageGenerator("activecq/components/templates/test-harness", design.getPath(), getSession());
        
        System.out.println("page: " + page.getPath());
        
        node = design.buildContentResource(getSession(), component.getName(), component.getResourceType());
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.DESIGN_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.DESIGN_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.DESIGN_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.DESIGN_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.DESIGN_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.DESIGN_BOOLEAN);
        node.getSession().save();
        stylePath = node.getPath() + "/jcr:content/test-harness/par/test-1";

        // ParSys
        node = JcrUtil.createPath(page.getPath() + "/jcr:content/par", "nt:unstructured", getSession());
        node.setProperty("sling:resourceType", "activecq/api/test/support/parsys");
        node.getSession().save();

        // Component 1
        node = page.buildParResource("test-1", component.getResourceType(), getSession());
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.CONTENT_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.CONTENT_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.CONTENT_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.CONTENT_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.CONTENT_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.CONTENT_BOOLEAN);
        node.getSession().save();
        testResourcePath = node.getPath();

        // Extra Resource 
        node = JcrUtil.createPath(Constants.EXTRA_RESOURCE_PATH, "nt:unstructured", getSession());
        node.setProperty(Constants.PROPERTY_PLAIN_TEXT, Constants.EXTRA_PLAIN_TEXT);
        node.setProperty(Constants.PROPERTY_RICH_TEXT, Constants.EXTRA_RICH_TEXT);
        node.setProperty(Constants.PROPERTY_DOUBLE, Constants.EXTRA_DOUBLE);
        node.setProperty(Constants.PROPERTY_LONG, Constants.EXTRA_LONG);
        node.setProperty(Constants.PROPERTY_STR_ARRAY, Constants.EXTRA_STR_ARRAY);
        node.setProperty(Constants.PROPERTY_BOOLEAN, Constants.EXTRA_BOOLEAN);
        node.getSession().save();
        
        // Remote Tester        
        setRemoteTester(new RemoteTester(
                "admin",
                "admin",
                "http",
                "localhost:5502",
                testResourcePath,
                "GET"));        
    }

    @After
    @Override
    public void tearDown() throws Exception {
       /*removeNodes(getResourceResolver(),
                design.getDeletePath(),
                component.getDeletePath(),
                page.getDeletePath());*/
        super.tearDown();
    }

    /**
     * REMOTE TEST *
     */
    @Test
    public void test_getComponent() {
        this.assertEquals(remote.execute("getComponent"),
                "active-component");
    }

    @Test
    public void test_getComponentContext() {
        this.assertEquals(remote.execute("getComponentContext"),
                "active-component");
    }

    @Test
    public void test_getDesign() {
        this.assertEquals(remote.execute("getDesign"),
                design.getPath());
    }

    @Test
    public void test_getDesignProperties() {
        this.assertEquals(remote.execute("getDesignProperties"),
                "8");
    }

    @Test
    public void test_getDesignProperty() {
        this.assertEquals(remote.execute("getDesignProperty"),
                Constants.DESIGN_PLAIN_TEXT,
                Constants.DESIGN_RICH_TEXT,
                String.valueOf(Constants.DESIGN_DOUBLE),
                String.valueOf(Constants.DESIGN_LONG),
                String.valueOf(Constants.DESIGN_BOOLEAN),
                "This is the default design value",
                ArrayUtils.toString(Constants.DESIGN_STR_ARRAY));
    }

    @Test
    public void test_getDesigner() {
        this.assertEquals(remote.execute("getDesigner"),
                "true");
    }

    @Test
    public void test_getEditContext() {
        this.assertEquals(remote.execute("getEditContext"),
                "true");
    }

    @Test
    public void test_getNode() {
        this.assertEquals(remote.execute("getNode"),
                testResourcePath);
    }

    @Test
    public void test_getPage() {
        this.assertEquals(remote.execute("getPage"),
                page.getPath());
    }

    @Test
    public void test_getPageManager() {
        this.assertEquals(remote.execute("getPageManager"),
                "true");
    }

    @Test
    public void test_getPageProperties() {
        this.assertEquals(remote.execute("getPageProperties"),
                "7");
    }

    @Test
    public void test_getProperties() {
        this.assertEquals(remote.execute("getProperties"),
                "8");
    }

    @Test
    public void test_getProperty() {
        this.assertEquals(remote.execute("getProperty"),
                Constants.CONTENT_PLAIN_TEXT,
                Constants.CONTENT_RICH_TEXT,
                String.valueOf(Constants.CONTENT_DOUBLE),
                String.valueOf(Constants.CONTENT_LONG),
                String.valueOf(Constants.CONTENT_BOOLEAN),
                "This is the content resource default value",
                ArrayUtils.toString(Constants.CONTENT_STR_ARRAY));
    }

    @Test
    public void test_getProperty_Resource() {
        this.assertEquals(remote.execute("getPropertyResource"),
                Constants.EXTRA_PLAIN_TEXT,
                Constants.EXTRA_RICH_TEXT,
                String.valueOf(Constants.EXTRA_DOUBLE),
                String.valueOf(Constants.EXTRA_LONG),
                String.valueOf(Constants.EXTRA_BOOLEAN),
                "This is the extra default value",
                ArrayUtils.toString(Constants.EXTRA_STR_ARRAY));    }

    @Test
    public void test_getQueryBuilder() {
        this.assertEquals(remote.execute("getQueryBuilder"),
                "true");
    }

    @Test
    public void test_getRequest() {
        this.assertEquals(remote.execute("getRequest"),
                testResourcePath + ".getRequest.html");
    }

    @Test
    public void test_getRequestDesign() {
        this.assertEquals(remote.execute("getRequestDesign"),
                design.getPath());
    }

    @Test
    public void test_getRequestDesignProperties() {
        this.assertEquals(remote.execute("getRequestDesignProperties"),
                "8");
    }

    @Test
    public void test_getRequestPage() {
        this.assertEquals(remote.execute("getRequestPage"),
                page.getPath());
    }

    @Test
    public void test_getRequestPageProperties() {
        this.assertEquals(remote.execute("getRequestPageProperties"),
                "7");
    }

    @Test
    public void test_getRequestStyle() {
        this.assertEquals(remote.execute("getRequestStyle"),
                design.getPath() + "/jcr:content/test-harness/par/test-1");
    }

    @Test
    public void test_getResource() {
        this.assertEquals(remote.execute("getResource"),
                testResourcePath);
    }

    @Test
    public void test_getResourceDesign() {
        this.assertEquals(remote.execute("getResourceDesign"),
                design.getPath());
    }

    @Test
    public void test_getResourceDesignProperties() {
        this.assertEquals(remote.execute("getResourceDesignProperties"),
                "8");
    }

    @Test
    public void test_getResourcePage() {
        this.assertEquals(remote.execute("getResourcePage"),
                page.getPath());
    }

    @Test
    public void test_getResourcePageProperties() {
        this.assertEquals(remote.execute("getResourcePageProperties"),
                "7");
    }

    @Test
    public void test_getResourceResolver() {
        this.assertEquals(remote.execute("getResourceResolver"),
                "true");
    }

    @Test
    public void test_getResourceStyle() {
        this.assertEquals(remote.execute("getResourceStyle"),
                design.getPath() + "/jcr:content/test-harness/par/test-1");
    }

    @Test
    public void test_getResponse() {
        this.assertEquals(remote.execute("getResponse"),
                "true");
    }

    @Test
    public void test_getService() {
        this.assertEquals(remote.execute("getService"),
                "true");
    }

    @Test
    public void test_getStyle() {
        this.assertEquals(remote.execute("getStyle"),
                design.getPath() + "/jcr:content/test-harness/par/test-1");
    }

    @Test
    public void test_hasNode() {
        this.assertEquals(remote.execute("hasNode"),
                "true");
    }
    
    @Test
    public void test_jstl() {
        this.assertEquals(remote.execute("jsstl"),
                "active-component");
    }    
}