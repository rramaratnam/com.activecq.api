<%@page session="false"
        contentType="text/html; charset=utf-8"
        import="com.day.cq.wcm.api.WCMMode,
                com.day.cq.wcm.foundation.ELEvaluator" %><%
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><cq:defineObjects/><%
%><cq:include script="forbidden/preprocessor.jsp"/><%
%><cq:include script="preprocessor.jsp"/><%
%><!doctype html>
    <!--[if lt IE 7 ]> <html class="no-js ie6"> <![endif]--> 
    <!--[if IE 7 ]>    <html class="no-js ie7"> <![endif]--> 
    <!--[if IE 8 ]>    <html class="no-js ie8"> <![endif]--> 
    <!--[if IE 9 ]>    <html class="no-js ie9"> <![endif]--> 
    <!--[if (gt IE 9)|!(IE)]><!--> <html class="no-js"> <!--<![endif]--> 

    <head>
        <meta charset="utf-8"/> 
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/> 
        <meta name="viewport" content="width=device-width,initial-scale=1">        
        <cq:include script="head.meta.jsp"/> 
        <cq:include script="head.css.jsp"/>
        <cq:include script="forbidden/head.cq.jsp"/>
        <cq:include script="forbidden/head.js.jsp"/>
        <cq:include script="head.js.jsp"/>     
    </head>
    
    <body>
        <cq:include script="forbidden/body.first.cq.jsp"/>
        <cq:include script="body.jsp"/>
        <cq:include script="forbidden/body.last.cq.jsp"/>
        <cq:include script="forbidden/body.last.js.jsp"/>
        <cq:include script="body.js.jsp"/>
    </body>
</html>
