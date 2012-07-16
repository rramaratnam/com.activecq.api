<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                com.activecq.api.test.ActiveComponentTest.Constants,                                
                org.apache.commons.lang.ArrayUtils"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
Resource propResource = resourceResolver.resolve(Constants.EXTRA_RESOURCE_PATH);
final String DEFAULT_STR_VALUE = "This is the extra default value";
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.getProperty(propResource, Constants.PROPERTY_PLAIN_TEXT, String.class) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= c.getProperty(propResource, Constants.PROPERTY_RICH_TEXT, String.class) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= c.getProperty(propResource, Constants.PROPERTY_DOUBLE, Double.class) %> 
<!-- End Test -->

<!-- Begin Test -->
    <%= c.getProperty(propResource, Constants.PROPERTY_LONG, Long.class) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= c.getProperty(propResource, Constants.PROPERTY_BOOLEAN, Boolean.class) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= c.getProperty(propResource, "default-value", DEFAULT_STR_VALUE) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= ArrayUtils.toString(c.getProperty(propResource, Constants.PROPERTY_STR_ARRAY, new String[] {})) %>       
<!-- End Test -->
