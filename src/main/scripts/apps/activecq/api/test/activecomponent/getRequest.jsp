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
final String PATH = Constants.CONTENT_PAGE_PATH + ".getRequest.html";
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.getRequest() == null ? false : PATH.equals(c.getRequest().getRequestURI()) %>
<!-- End Test -->
