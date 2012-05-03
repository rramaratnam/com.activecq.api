<%@include file="/apps/activecq/global/global.jsp" %><%
%><%@ page import="com.day.cq.commons.Doctype,
                   org.apache.commons.lang.StringEscapeUtils" %><%                 
String path = currentDesign.getPath();                   
%><%       
        %><title><%= currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle() %></title>
        <meta http-equiv="description" content="<%= StringEscapeUtils.escapeHtml(properties.get("jcr:description", "")) %>"/>
        <meta http-equiv="keywords" content="<%= StringEscapeUtils.escapeHtml(WCMUtils.getKeywords(currentPage, false)) %>"/>
