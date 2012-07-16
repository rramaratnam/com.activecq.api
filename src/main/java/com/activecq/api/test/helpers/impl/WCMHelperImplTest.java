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
package com.activecq.api.test.helpers.impl;

import com.activecq.api.testing.AbstractRemoteTemplate;
import com.activecq.api.testing.RemoteTester;
import com.day.cq.commons.jcr.JcrUtil;
import javax.jcr.Node;
import javax.jcr.Session;
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
public class WCMHelperImplTest extends AbstractRemoteTemplate {

    @TestReference
    private ResourceResolverFactory resourceResolverFactory;

    public final static class Constants {

        public static final String RESOURCE_PATH = "/content/testing/activecq/helpers/wcmhelper";
    }

    public WCMHelperImplTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp(resourceResolverFactory);

        setRemoteTester(new RemoteTester(
                "admin",
                "admin",
                "http",
                "localhost:5502",
                Constants.RESOURCE_PATH,
                "GET"));

        Session session = getSession();

        Node node = JcrUtil.createPath(Constants.RESOURCE_PATH, "nt:unstructured", session);
        node.setProperty("sling:resourceType", "activecq/api/test/helpers/wcmhelper");
        node.setProperty("jcr:title", "WCMHelper Resource for Testing");
        session.save();
    }

    @After
    public void tearDown() throws Exception {
        removeNodes(getResourceResolver(),
                "/content/testing");

        super.tearDown();
    }

    /**
     * REMOTE TEST *
     */
    @Test
    public void test_isAuthor() {
        this.assertEquals(remote.execute("isAuthor"),
                "true");
    }

    @Test
    public void test_isPublish() {
        this.assertEquals(remote.execute("isPublish"),
                "true");        
        
    }

    @Test
    public void test_isDesignMode() {
        this.assertEquals(remote.execute("isDesignMode", "wcmmode=design"),
                "true");
    }

    @Test
    public void test_isDisabledMode() {
        this.assertEquals(remote.execute("isDisabledMode", "wcmmode=disabled"),
                "true");
    }

    @Test
    public void test_isEditMode() {
        this.assertEquals(remote.execute("isEditMode", "wcmmode=edit"),
                "true");
    }

    @Test
    public void test_isPreviewMode() {
        this.assertEquals(remote.execute("isPreviewMode", "wcmmode=preview"),
                "true");
    }

    @Test
    public void test_isReadOnlyMode() {
        this.assertEquals(remote.execute("isReadOnlyMode", "wcmmode=read_only"),
                "true");
    }

    @Test
    public void test_isAuthoringMode() {
        this.assertEquals(remote.execute("isAuthoringMode", "wcmmode=edit"),
                "true");
        this.assertEquals(remote.execute("isAuthoringMode", "wcmmode=design"),
                "true");
    }
    
    @Test
    public void test_printEditBlock() {
        this.assertEquals(remote.execute("printEditBlock", "wcmmode=edit"),
                "true");        
        
        this.assertEquals(remote.execute("isAuthoringMode", "wcmmode=design"),
                "true");
        
        this.assertEquals(remote.execute("isAuthoringMode", "wcmmode=disabled"),
                "false");        
    }    
}