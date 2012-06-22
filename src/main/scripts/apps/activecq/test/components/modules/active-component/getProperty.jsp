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
final String DEFAULT_STR_VALUE = "This is the content resource default value";
/** End Component Setup **/
%>

<!-- Begin Test -->
  "Plain Text": <%= StringUtils.equals(Constants.CONTENT_PLAIN_TEXT, c.getProperty(Constants.PROPERTY_PLAIN_TEXT, String.class)) %>
  "Rich Text": <%= StringUtils.equals(Constants.CONTENT_RICH_TEXT, c.getProperty(Constants.PROPERTY_RICH_TEXT, String.class)) %>
  "Double":  <%= new Double(Constants.CONTENT_DOUBLE).equals(c.getProperty(Constants.PROPERTY_DOUBLE, Double.class)) %> 
  "Long":    <%= new Long(Constants.CONTENT_LONG).equals(c.getProperty(Constants.PROPERTY_LONG, Long.class)) %>
  "Boolean": <%= new Boolean(Constants.CONTENT_BOOLEAN).equals(c.getProperty(Constants.PROPERTY_BOOLEAN, Boolean.class)) %>
  "Default": <%= StringUtils.equals(DEFAULT_STR_VALUE, c.getProperty("default-value", DEFAULT_STR_VALUE)) %>
  "String Array": <%= ArrayUtils.isEquals(Constants.CONTENT_STR_ARRAY, c.getProperty(Constants.PROPERTY_STR_ARRAY, new String[] {})) %>
<!-- End Test -->