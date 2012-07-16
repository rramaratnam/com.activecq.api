<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.getRequestPageProperties().size() %>
<!-- End Test -->