<%@ page session="false" 
         import="com.day.cq.commons.Doctype,
                 com.day.cq.wcm.mobile.api.device.DeviceGroup"%><% 
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><cq:include script="/apps/activecq/global/init.jsp"/><%
%><%

    String webclipIcon = currentDesign.getPath() + "/webclip.png";
    if (resourceResolver.getResource(webclipIcon) == null) {
        webclipIcon = null;
    }
    
    String webclipIconPre = currentDesign.getPath() + "/webclip-precomposed.png";
    if (resourceResolver.getResource(webclipIconPre) == null) {
        webclipIconPre = null;
    }

    /*
        Retrieve the current mobile device group from the request in the following order:
        1) group defined by <path>.<groupname-selector>.html
        2) if not found and in author mode, get default device group as defined in the page properties
           (the first of the mapped groups in the mobile tab)

        If a device group is found, use the group's drawHead method to include the device group's associated
        emulator init component (only in author mode) and the device group's rendering CSS.
     */
     final DeviceGroup deviceGroup = slingRequest.adaptTo(DeviceGroup.class);
     if (null != deviceGroup) {
         deviceGroup.drawHead(pageContext);
     }
    %><% if (webclipIcon != null) { %>
    <link rel="apple-touch-icon" href="<%= webclipIcon %>" />
    <% } %>
    <% if (webclipIconPre != null) { %>
    <link rel="apple-touch-icon-precomposed" href="<%= webclipIconPre %>" />
    <% } %>
    <%-- currentDesign.writeCssIncludes(pageContext); --%>