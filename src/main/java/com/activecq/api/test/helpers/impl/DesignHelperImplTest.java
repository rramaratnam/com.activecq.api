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

import com.activecq.api.testing.AbstractRemoteTest;
import com.activecq.api.testing.RemoteTester;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.Page;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Session;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author david
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class DesignHelperImplTest extends AbstractRemoteTest {

    @TestReference
    private ResourceResolverFactory resourceResolverFactory;

    public final static class Constants {

        public static final String RESOURCE_PATH = "/content/testing/activecq/helpers/designhelper";
    }

    public DesignHelperImplTest() {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp(resourceResolverFactory);

        setRemoteTester(new RemoteTester(
                "admin",
                "admin",
                "http://localhost:5502" + WCMHelperImplTest.Constants.RESOURCE_PATH,
                "GET"));

        Session session = getSession();

        Node node = JcrUtil.createPath(WCMHelperImplTest.Constants.RESOURCE_PATH, "nt:unstructured", session);
        node.setProperty("sling:resourceType", "activecq/api/test/helpers/designhelper");
        node.setProperty("jcr:title", "DesignHelper Resource for Testing");
        session.save();
    }

    @After
    public void tearDown() throws Exception {
        removeNodes(getResourceResolver(),
                "/content/testing");

        super.tearDown();
    }

    /**
     * Test of cssTag method, of class DesignHelperImpl.
     */
    @Test
    public void testCssTag_String_Page() {
        remote.execute("cssTag");
    }

    /**
     * Test of cssSrc method, of class DesignHelperImpl.
     */
    @Test
    public void testCssSrc() {
        remote.execute("cssSrc");
    }

    /**
     * Test of imgTag method, of class DesignHelperImpl.
     */
    @Test
    public void testImgTag_String_Page() {
        System.out.println("imgTag");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imgTag method, of class DesignHelperImpl.
     */
    @Test
    public void testImgTag_3args_1() {
        System.out.println("imgTag");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imgTag method, of class DesignHelperImpl.
     */
    @Test
    public void testImgTag_3args_2() {
        System.out.println("imgTag");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imgSrc method, of class DesignHelperImpl.
     */
    @Test
    public void testImgSrc() {
        System.out.println("imgSrc");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of scriptTag method, of class DesignHelperImpl.
     */
    @Test
    public void testScriptTag() {
        System.out.println("scriptTag");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of scriptSrc method, of class DesignHelperImpl.
     */
    @Test
    public void testScriptSrc() {
        System.out.println("scriptSrc");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
