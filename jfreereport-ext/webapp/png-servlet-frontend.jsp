<%@ page import="java.net.URL,
                 com.jrefinery.report.ext.demo.AbstractPageableReportServletWorker,
                 com.jrefinery.report.ext.demo.DefaultPageableReportServletWorker,
                 com.jrefinery.report.demo.SwingIconsDemoTableModel,
                 com.jrefinery.report.targets.pageable.output.G2OutputTarget,
                 com.jrefinery.report.JFreeReport"%>

<%
  // initalize the report if not already done ...
  URL in = getClass().getResource("/com/jrefinery/report/demo/swing-icons.xml");
  if (in == null)
  {
    throw new ServletException("Missing Resource: /com/jrefinery/report/demo/swing-icons.xml");
  }

  URL base = getServletConfig().getServletContext().getResource("/WEB-INF/lib/jlfgr-1_0.jar");
  AbstractPageableReportServletWorker worker =
      new DefaultPageableReportServletWorker(null,
                                             in,
                                             new SwingIconsDemoTableModel(base));
  JFreeReport report = worker.getReport();
  G2OutputTarget target = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), report.getDefaultPageFormat());
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
  <img src="pngservlet?page=<%=i%>" alt="Page <%=i%>">
  <p>
<%
  }
}
%>
</body>
</html>
