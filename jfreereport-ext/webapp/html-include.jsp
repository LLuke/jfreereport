<%@ page import="org.jfree.report.ext.servletdemo.JFreeReportHtmlFragmentGenerator"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body>
    <h1>An included report: This Header is defined in the JSP Page.</h1>
    <hr>
    <div style="border: 2pt solid black">
    <%
      JFreeReportHtmlFragmentGenerator generator = new JFreeReportHtmlFragmentGenerator();
      out.println(generator.performGenerate(pageContext));
     %>
    </div>
    <hr>
    <h1>An included report: This Footer is defined in the JSP Page.</h1>
  </body>
</html>