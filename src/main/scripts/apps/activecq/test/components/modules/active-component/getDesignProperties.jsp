<%@page session="false" 
        import="com.activecq.api.ActiveComponent,
                com.activecq.api.test.ActiveComponentTest,
                com.activecq.api.plugins.ExposedPlugin,
                org.apache.sling.api.scripting.SlingScriptHelper,
                javax.jcr.RepositoryException,
                org.apache.sling.api.resource.LoginException"
%><%@include file="/apps/activecq/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTest c = new ActiveComponentTest(sling);
ExposedPlugin ex = c.getExposed();    
/** End Component Setup **/
%>

<h2>ActiveComponent</h2>
<dl>
    <dt>Core</dt>
    <dd>getComponent(): <%= c.getComponent().getName() %></dd>    
    <dd>getComponentContext(): <%= c.getComponentContext() %></dd>    
    <dd>getDesign(): <%= c.getDesign().getPath() %></dd>    
    <dd>getDesignProperties(): <%= c.getDesignProperties().size() %></dd>    
    <dd>getDesigner(): Exists > <%= c.getDesigner() != null %></dd>    
    <dd>getEditContext(): <%= c.getEditContext() %></dd>    
    <dd>getNode(): <%= c.getNode().getPath() %></dd>    
    <dd>getPage(): <%= c.getPage().getPath() %></dd>    
    <dd>getPageManager(): Exists > <%= c.getPageManager() != null %></dd>    
    <dd>getProperties(): <%= c.getProperties().size() %></dd>    
    <dd>getQueryBuilder(): Exists > <%= c.getQueryBuilder() != null %></dd>    
    <dd>getRequest(): <%= c.getRequest().getRequestURI() %></dd>    
    <dd>getRequestDesign(): <%= c.getRequestDesign().getPath() %></dd>    
    <dd>getResourcePage(): <%= c.getResourcePage().getPath() %></dd>    
    <dd>getResourceResolver(): <%= c.getResourceResolver().resolve("/etc/designs").getPath() %></dd>    
    <dd>getResponse(): Exists > <%= c.getResponse() != null %></dd>    
    <dd>getStyle(): <%= c.getStyle().getPath() %></dd>    
    <dd>getService(): <%= c.getService(com.day.cq.search.QueryBuilder.class) != null %></dd>    
</dl>

<h2>Properties</h2>
<dl>
    <dt>Resource Properties</dt>
    <dd><%= c.getProperty("test-id") %></dd>
    <dd><%= c.getProperty("string-val") %></dd>
    <dd><%= c.getProperty("double-val") %></dd>
    <dd><%= c.getProperty("long-val") %></dd>
    <dd><%= c.getProperty("boolean-val") %></dd>
    <dd><%= c.getProperty("date-val") %></dd>
    <dd><%= c.getProperty("default-val", "DEFAULT") %></dd>
    
</dl>

<h2>Plugins</h2>
<dl>
    <dt>XSS</dt>
    <dd>Safe string: <%= c.getPlugins().XSS.protect("I am safe") %></dd>
    <dd>Unsafe string: <%= c.getPlugins().XSS.protect("<script>alert('unsafe');</script>") %></dd>
    <dd>Mixed string: <%= c.getPlugins().XSS.protect("The start is safe! <script>alert('the middle');</script> the end is safe!") %></dd>
</dl>


<style>
    body { font-family: arial, sans-serif; }
</style>