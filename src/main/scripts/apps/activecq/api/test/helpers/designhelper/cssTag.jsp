<%@page session="false" 
        import="com.activecq.api.helpers.DesignHelper,
                org.apache.commons.lang.StringUtils,
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Helper Setup **/
DesignHelper helper = sling.getService(DesignHelper.class); 
/** End Helper Setup **/
%>

<!-- Begin Test -->
    <%= helper.cssTag("test", currentPage) %>
<!-- End Test -->