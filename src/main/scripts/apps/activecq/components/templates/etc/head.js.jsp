<%@page session="false" 
    import="com.day.cq.widget.HtmlLibraryManager"%><%
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><script src="/libs/cq/ui/resources/cq-ui.js"></script><%
%><%
    HtmlLibraryManager htmlMgr = sling.getService(HtmlLibraryManager.class);
    if (htmlMgr != null) {

        htmlMgr.writeIncludes(slingRequest, out, "cq.wcm.edit", "cq.tagging", 
                "cq.personalization", "cq.security", "cq.widgets");
    }
%>