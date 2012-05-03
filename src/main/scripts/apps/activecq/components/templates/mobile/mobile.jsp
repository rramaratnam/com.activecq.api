<%@page session="false"
        contentType="text/html; charset=utf-8"
        import="com.day.cq.wcm.api.WCMMode,
                com.day.cq.wcm.foundation.ELEvaluator" %><%
%><%@include file="/apps/activecq/global/global.jsp" %><%
%><cq:defineObjects/><%
%><cq:include script="forbidden/preprocessor.jsp"/><%
%><cq:include script="preprocessor.jsp"/><%
%><!doctype html> 
<!--[if IEMobile 7 ]>    <html class="no-js iem7" manifest="default.appcache?v=1"> <![endif]--> 
<!--[if (gt IEMobile 7)|!(IEMobile)]><!--> <html class="no-js"> <!--<![endif]--> 

    <head>
        <meta charset="utf-8"/> 
        <meta name="HandheldFriendly" content="True"/> 
        <meta name="MobileOptimized" content="320"/> 
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="cleartype" content="on"/> 
         
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
