package com.jrefinery.report.io;

import org.xml.sax.helpers.DefaultHandler;
import com.jrefinery.report.JFreeReport;
import java.net.URL;

public class ReportDefinitionException extends Exception
{
  public ReportDefinitionException ()
  {
  }

  public ReportDefinitionException (String message)
  {
    super (message);
  }

}
