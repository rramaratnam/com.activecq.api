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
package com.activecq.api.helpers;

import com.activecq.api.helpers.AnalyticsHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class AnalyticsHelperTest {
    
    public AnalyticsHelperTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    public void testToString() {
        AnalyticsHelper a = new AnalyticsHelper(
                AnalyticsHelper.Keys.JS_EVENTS, "click mouseover",
                AnalyticsHelper.Keys.COLLECT, "true",
                AnalyticsHelper.Keys.EVENT, "testClick",
                AnalyticsHelper.Keys.VALUES, "{\"foo\": \"bar\"}",
                AnalyticsHelper.Keys.COMPONENT_PATH, "test/component/analytics");
        
        String expResults = "data-analytics-js-events=\"click mouseover\"" +
                " data-analytics-collect=\"true\"" +
                " data-analytics-event=\"testClick\"" +
                " data-analytics-values=\"{\"obj\": \"js:this\"}\" " +
                " data-analytics-componentPath=\"test/component/anaytics\"";

        assertEquals(expResults, a.toString());
    }
    
    @Test
    public void testToDataDash() {
        System.out.println("testToDataDash");
        
        assertEquals("data-analytics-test=\"testdata\"", 
                AnalyticsHelper.toDataDash("test", "testdata"));        
    }
}
