/**
 * Date: Jan 14, 2003
 * Time: 10:28:32 PM
 *
 * $Id: PropertyFileReportConfiguration.java,v 1.1 2003/01/14 23:50:10 taqua Exp $
 */
package com.jrefinery.report.util;

import java.io.IOException;
import java.io.InputStream;

/** A report configuration that reads its values from the jfreereport.properties file. */
public class PropertyFileReportConfiguration extends ReportConfiguration
{
  public void load(String fileName)
  {
    InputStream in = this.getClass().getResourceAsStream(fileName);
    if (in != null)
    {
      try
      {
        this.getConfiguration().load(in);
      }
      catch (IOException ioe)
      {
        Log.warn ("Unable to read global configuration", ioe);
      }
    }
    else
    {
      Log.debug ("File: " + fileName);
    }

  }

  /**
   * Default constructor.
   */
  public PropertyFileReportConfiguration()
  {
  }
}
