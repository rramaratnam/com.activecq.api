<%@page session="false" 
        import="com.activecq.api.helpers.WCMHelper"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Helper Setup **/
WCMHelper helper = sling.getService(WCMHelper.class); 
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= !helper.isPublish() %>
<!-- End Test -->