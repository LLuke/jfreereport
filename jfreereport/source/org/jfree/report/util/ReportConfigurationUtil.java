package org.jfree.report.util;

import org.jfree.report.JFreeReportCoreModule;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 07.09.2005, 15:53:00
 *
 * @author: Thomas Morgner
 */
public class ReportConfigurationUtil
{
  private ReportConfigurationUtil()
  {

  }

  public static boolean isStrictErrorHandling (final Configuration config)
  {
    final String strictError = config.getConfigProperty
                    (JFreeReportCoreModule.STRICT_ERROR_HANDLING_KEY);
    return "true".equals(strictError);
  }
}
