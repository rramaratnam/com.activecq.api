<%@page session="false" 
        import="com.activecq.api.test.ActiveComponentTestStub"
%><%@include file="/apps/activecq/global/global.jsp" %>

<c:set var="c" value="<%= new ActiveComponentTestStub(slingRequest) %>"/>

<!-- Begin Test -->
    ${c.component.name}
<!-- End Test -->

