<%@ page import="java.net.URL,
                 java.util.ResourceBundle,
                 java.util.Locale"%>

<html>
<head><title>Classpath test Servlet Fontend</title></head>
<body>

<%
  final String baseName = "com.jrefinery.report.resources.JFreeReportResources";
  try
  {
    Class c = this.getClass().getClassLoader().loadClass(baseName);
    c.newInstance();
    System.out.println ("OK");
  }
  catch (Throwable ioe)
  {
    System.out.println ("Failed to load resource bundle class");
    ioe.printStackTrace();
  }

  try
  {
    ResourceBundle.getBundle(baseName);
  }
  catch (Exception e)
  {
    System.out.println ("Failed to load resource bundle, simple way");
    e.printStackTrace();
  }

  try
  {
    ResourceBundle.getBundle(baseName, Locale.getDefault(), getClass().getClassLoader());
  }
  catch (Exception e)
  {
    System.out.println ("Failed to load resource bundle, complex way");
    e.printStackTrace();
  }
%>
</body>
</html>
