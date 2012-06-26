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
package com.activecq.api.utils;

import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.osgi.service.event.Event;

/**
 *
 * @author david
 */
public class EventsUtilTest {

    private static MockResourceResolver resourceResolver;

    public EventsUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        resourceResolver = new MockResourceResolver();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEventResource method, of class EventsUtil.
     */
    @Test
    public void testGetEventResource() {
        System.out.println("getEventResource");

        final String RESOURCE_PATH = "/content/testing/page";
        
        Dictionary<String, String> map = new Hashtable<String, String>();
        map.put("path", RESOURCE_PATH);
        Event event = new Event("test-topic", map);

        Resource expResult = new MockResource(resourceResolver, RESOURCE_PATH, "page");

        resourceResolver.addResource(expResult);

        Resource result = EventsUtil.getEventResource(event, resourceResolver);
        assertEquals(expResult, result);
    }
}