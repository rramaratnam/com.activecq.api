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

String PATH = Constants.CONTENT_PAGE_PATH;
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= PATH.equals(c.getPage().getPath()) %>
<!-- End Test -->