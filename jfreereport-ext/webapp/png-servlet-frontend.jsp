<%@ page import="java.net.URL,
                 javax.servlet.ServletException,
                 org.jfree.report.JFreeReport,
                 org.jfree.report.ext.servletdemo.AbstractPageableReportServletWorker,
                 org.jfree.report.ext.servletdemo.DefaultPageableReportServletWorker,
                 org.jfree.report.ext.servletdemo.DemoModelProvider,
                 org.jfree.report.modules.output.pageable.graphics.G2OutputTarget,
                 org.jfree.util.ObjectUtilities"%>

<%
  // initalize the report if not already done ...
  URL in = ObjectUtilities.getResource
          ("/org/jfree/report/demo/swingicons/swing-icons.xml", JFreeReport.class);
  if (in == null)
  {
    throw new ServletException("Missing Resource: /org/jfree/report/demo/swing-icons.xml");
  }

  URL base = getServletConfig().getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
  AbstractPageableReportServletWorker worker =
      new DefaultPageableReportServletWorker(null,
                                             in,
                                             new DemoModelProvider(base));
  G2OutputTarget target = new G2OutputTarget(G2OutputTarget.createEmptyGraphics());
  worker.setOutputTarget(target);
  int numberOfPages = worker.getNumberOfPages();
%>

<html>
<head><title>PNG Servlet Fontend</title></head>
<body>

<%
if (numberOfPages == 0)
{
  %>
  The report could not be loaded, the number of pages property is invalid.
  <%
}
else
{
  for (int i = 0; i < numberOfPages; i++)
  {
%>
  <img src="pngreport?page=<%=i%>" alt="Page <%=i%>">
  <p>
<%
  }
}
%>
</body>
</html>
