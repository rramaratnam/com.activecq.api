<%@ page session="false" %><% 
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><% if(!"default".equalsIgnoreCase(currentDesign.getId())) { currentDesign.writeCssIncludes(pageContext); } %><%
%><cq:include script="/apps/activecq/global/init.jsp"/>
