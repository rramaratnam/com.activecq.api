<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,       
                com.activecq.api.test.ActiveComponentTest.Constants,
                org.apache.commons.lang.ArrayUtils"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String DEFAULT_STR_VALUE = "This is the default design value";
/** End Component Setup **/
%>

<!-- Begin Test -->
     <%= c.getDesignProperty(Constants.PROPERTY_PLAIN_TEXT, String.class) %>
<!-- End Test -->

<!-- Begin Test -->
     <%= c.getDesignProperty(Constants.PROPERTY_RICH_TEXT, String.class) %>     
<!-- End Test -->

<!-- Begin Test -->
     <%= c.getDesignProperty(Constants.PROPERTY_DOUBLE, Double.class) %> 
<!-- End Test -->

<!-- Begin Test -->
     <%= c.getDesignProperty(Constants.PROPERTY_LONG, Long.class) %>
<!-- End Test -->

<!-- Begin Test -->
     <%= c.getDesignProperty(Constants.PROPERTY_BOOLEAN, Boolean.class) %>
<!-- End Test -->

<!-- Begin Test -->
     <%= c.getDesignProperty("default-value", DEFAULT_STR_VALUE) %>
<!-- End Test -->

<!-- Begin Test -->
     <%= ArrayUtils.toString(c.getDesignProperty(Constants.PROPERTY_STR_ARRAY, new String[] {})) %>    
<!-- End Test -->