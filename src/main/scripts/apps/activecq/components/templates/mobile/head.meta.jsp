<%@include file="/apps/activecq/global/global.jsp" %><%
%><%@ page import="com.day.cq.commons.Doctype,
                   org.apache.commons.lang.StringEscapeUtils" %><%                 
String path = currentDesign.getPath();                   
%><%       
%><title><%= currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle() %></title>
<meta http-equiv="description" content="<%= StringEscapeUtils.escapeHtml(properties.get("jcr:description", "")) %>"/>
<meta http-equiv="keywords" content="<%= StringEscapeUtils.escapeHtml(WCMUtils.getKeywords(currentPage, false)) %>"/>

<%-- For iPhone 4 with high-resolution Retina display: --%> 
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<%= path %>/images/icons/h/apple-touch-icon.png"> 
<%-- For first-generation iPad: --%> 
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<%= path %>/images/icons/m/apple-touch-icon.png"> 
<%-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: --%> 
<link rel="apple-touch-icon-precomposed" href="<%= path %>/images/icons/l/apple-touch-icon-precomposed.png"> 
<%-- For nokia devices: --%> 
<link rel="shortcut icon" href="<%= path %>/images/icons/l/apple-touch-icon.png"> 