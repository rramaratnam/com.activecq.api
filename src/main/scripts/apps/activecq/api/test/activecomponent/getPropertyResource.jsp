<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException,
                com.activecq.api.test.ActiveComponentTest.Constants,                                
                org.apache.commons.lang.StringUtils,                
                org.apache.commons.lang.ArrayUtils,
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
Resource propResource = resourceResolver.resolve(Constants.EXTRA_RESOURCE_PATH);
final String DEFAULT_STR_VALUE = "This is the extra default value";
/** End Component Setup **/
%>

<!-- Begin Test -->
    "Plain Text": <%= StringUtils.equals(Constants.EXTRA_PLAIN_TEXT, c.getProperty(propResource, Constants.PROPERTY_PLAIN_TEXT, String.class)) %>
    "Rich Text": <%= StringUtils.equals(Constants.EXTRA_RICH_TEXT, c.getProperty(propResource, Constants.PROPERTY_RICH_TEXT, String.class)) %>
    "Double":  <%= new Double(Constants.EXTRA_DOUBLE).equals(c.getProperty(propResource, Constants.PROPERTY_DOUBLE, Double.class)) %> 
    "Long":    <%= new Long(Constants.EXTRA_LONG).equals(c.getProperty(propResource, Constants.PROPERTY_LONG, Long.class)) %>
    "Boolean": <%= new Boolean(Constants.EXTRA_BOOLEAN).equals(c.getProperty(propResource, Constants.PROPERTY_BOOLEAN, Boolean.class)) %>
    "Default": <%= StringUtils.equals(DEFAULT_STR_VALUE, c.getProperty(propResource, "default-value", DEFAULT_STR_VALUE)) %>
    "String Array": <%= ArrayUtils.isEquals(Constants.EXTRA_STR_ARRAY, c.getProperty(propResource, Constants.PROPERTY_STR_ARRAY, new String[] {})) %>       
<!-- End Test -->
