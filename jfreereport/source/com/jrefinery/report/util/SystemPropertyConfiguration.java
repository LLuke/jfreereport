/**
 * Date: Jan 14, 2003
 * Time: 10:28:22 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

/**
 * A property configuration based on system properties.
 */
public class SystemPropertyConfiguration extends ReportConfiguration
{
  /**
   * Creates a report configuration that includes all the system properties (whether they are
   * related to reports or not).  The parent configuration is a
   * <code>PropertyFileReportConfiguration</code>.
   */
  public SystemPropertyConfiguration()
  {
    super();
    this.getConfiguration().putAll (System.getProperties());
  }
}
