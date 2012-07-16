<%@page session="false" 
        import="com.activecq.api.helpers.DesignHelper"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Helper Setup **/
DesignHelper helper = sling.getService(DesignHelper.class); 
/** End Helper Setup **/
%>

<!-- Begin Test -->
    <%= helper.cssTag("does-not-exist", currentPage) %>
<!-- End Test -->

<!-- Begin Test -->
    <%= helper.cssTag("exists", currentPage) %>
<!-- End Test -->