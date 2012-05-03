<%@page session="false"
        contentType="text/html; charset=utf-8"
        import="com.day.cq.wcm.api.WCMMode,
                com.day.cq.wcm.foundation.ELEvaluator" %><%
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><cq:defineObjects/><%
    // read the redirect target from the 'page properties' and perform the
    // redirect if WCM is disabled.
    String location = properties.get("redirectTarget", "");
    // resolve variables in path
    location = ELEvaluator.evaluate(location, slingRequest, pageContext);
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && location.length() > 0) {
        // check for recursion
        if (!location.equals(currentPage.getPath())) {
            response.sendRedirect(request.getContextPath() + location + ".html");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return;
    }
%>
