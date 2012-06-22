<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException,
                org.apache.commons.lang.ArrayUtils,
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String PATH = "/etc/designs/testing-activecq";
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.getResourceResolver() == null ? false : PATH.equals(c.getResourceResolver().resolve("/etc/designs/testing-activecq").getPath()) %>
<!-- End Test -->
