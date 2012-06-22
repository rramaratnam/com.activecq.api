<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException,
                org.apache.commons.lang.StringUtils,
                com.activecq.api.test.ActiveComponentTest.Constants,                
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);

String PATH = Constants.DESIGN_PATH;
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= StringUtils.equals(PATH, c.getDesign().getPath()) %>
<!-- End Test -->