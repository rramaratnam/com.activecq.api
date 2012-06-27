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
package com.activecq.api.helpers.impl;

import com.activecq.api.testing.AbstractRemoteTest;
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
public class WCMHelperImplTest extends AbstractRemoteTest {

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
                "http://localhost:5502" + Constants.RESOURCE_PATH, 
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

    /** REMOTE TEST **/
    
    @Test
    public void test_isAuthorTrue() {
        remote.execute("isAuthorTrue");
    }
            
    @Test
    public void test_isPublishFalse() {
        remote.execute("isPublishFalse");
    }    
          
    @Test
    public void test_isDesignModeTrue() {
        remote.execute("isDesignModeTrue", "wcmmode=design");
    }
    
    @Test
    public void test_isDisabledModeTrue() {
        remote.execute("isDisabledModeTrue", "wcmmode=disabled");
    }
    
    @Test
    public void test_isEditModeTrue() {
        remote.execute("isEditModeTrue", "wcmmode=edit");
    }    
    
    @Test
    public void test_isPreviewModeTrue() {
        remote.execute("isPreviewModeTrue", "wcmmode=preview");
    }       
    
    @Test
    public void test_isReadOnlyModeTrue() {
        remote.execute("isReadOnlyModeTrue", "wcmmode=read_only");
    }       
    
    @Test
    public void test_isAuthoringModeTrue() {
        remote.execute("isAuthoringModeTrue", "wcmmode=edit");
        remote.execute("isAuthoringModeTrue", "wcmmode=design");
    }
}