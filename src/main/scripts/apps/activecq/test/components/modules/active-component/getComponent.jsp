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

final String NAME = Constants.COMPONENT_NAME;
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= StringUtils.equals(NAME, c.getComponent().getName()) %>
<!-- End Test -->    