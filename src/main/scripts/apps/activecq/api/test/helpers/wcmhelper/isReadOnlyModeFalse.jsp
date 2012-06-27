<%@page session="false" 
        import="com.activecq.api.helpers.WCMHelper,
                org.apache.commons.lang.StringUtils,
                java.util.*,
                java.lang.*"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Helper Setup **/
WCMHelper helper = sling.getService(WCMHelper.class); 
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= !helper.isReadOnlyMode(slingRequest) %>
<!-- End Test -->