<%@page session="false" 
        import="com.activecq.api.helpers.WCMHelper,
                com.activecq.api.helpers.WCMEditType"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Helper Setup **/
WCMHelper helper = sling.getService(WCMHelper.class); 
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= helper.printEditBlock(sling, 
            WCMEditType.IMAGE,
            true) %>
<!-- End Test -->
