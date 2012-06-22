<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException,
                org.apache.commons.lang.ArrayUtils,
                com.activecq.api.test.ActiveComponentTest.Constants,                                                
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String PATH = Constants.CONTENT_RESOURCE_PATH;
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.getNode() == null ? false : PATH.equals(c.getNode().getPath()) %>
<!-- End Test -->