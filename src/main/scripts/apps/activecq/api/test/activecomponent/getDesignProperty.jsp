<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException,
                org.apache.commons.lang.ArrayUtils,
                org.apache.commons.lang.StringUtils,                
                com.activecq.api.test.ActiveComponentTest.Constants,                
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String DEFAULT_STR_VALUE = "This is the default design value";
/** End Component Setup **/
%>

<!-- Begin Test -->
    "Plain Text String": <%= StringUtils.equals(Constants.DESIGN_PLAIN_TEXT, c.getDesignProperty(Constants.PROPERTY_PLAIN_TEXT, String.class)) %>
    "Rich Text String":  <%= StringUtils.equals(Constants.DESIGN_RICH_TEXT, c.getDesignProperty(Constants.PROPERTY_RICH_TEXT, String.class)) %>
    "Double":  <%= new Double(Constants.DESIGN_DOUBLE).equals(c.getDesignProperty(Constants.PROPERTY_DOUBLE, Double.class)) %> 
    "Long":    <%= new Long(Constants.DESIGN_LONG).equals(c.getDesignProperty(Constants.PROPERTY_LONG, Long.class)) %>
    "Boolean": <%= new Boolean(Constants.DESIGN_BOOLEAN).equals(c.getDesignProperty(Constants.PROPERTY_BOOLEAN, Boolean.class)) %>
    "Default": <%= StringUtils.equals(DEFAULT_STR_VALUE, c.getDesignProperty("default-value", DEFAULT_STR_VALUE)) %>
    "String Array": <%= ArrayUtils.isEquals(Constants.DESIGN_STR_ARRAY, c.getDesignProperty(Constants.PROPERTY_STR_ARRAY, new String[] {})) %>    
<!-- End Test -->