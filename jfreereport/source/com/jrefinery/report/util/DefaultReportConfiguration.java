/**
 * Date: Jan 14, 2003
 * Time: 10:42:02 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

public class DefaultReportConfiguration extends ReportConfiguration
{
  public DefaultReportConfiguration()
  {
    this.getConfiguration().put (DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT);
    this.getConfiguration().put (LOGLEVEL, LOGLEVEL_DEFAULT);
    this.getConfiguration().put (PDFTARGET_AUTOINIT, PDFTARGET_AUTOINIT_DEFAULT);
    this.getConfiguration().put (PDFTARGET_ENCODING, PDFTARGET_ENCODING_DEFAULT);
  } 
}
