package com.jrefinery.report.io;

import org.xml.sax.helpers.DefaultHandler;
import com.jrefinery.report.JFreeReport;
import java.net.URL;

public abstract class AbstractReportDefinitionHandler extends DefaultHandler
{
  private URL contentBase;

  public AbstractReportDefinitionHandler ()
  {
  }

  public abstract JFreeReport getReport ();
  
  public void setContentBase (URL url)
  {
    this.contentBase = url;
  }
  
  public URL getContentBase ()
  {
    return contentBase;
  }
}
