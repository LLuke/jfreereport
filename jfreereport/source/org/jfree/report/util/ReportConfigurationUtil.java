package org.jfree.report.util;

import org.jfree.report.JFreeReportCoreModule;
import org.jfree.util.Configuration;

/**
 * An Utility class that simplifies using the report configuration.
 *
 * @author Thomas Morgner
 */
public class ReportConfigurationUtil
{
  /**
   * Hidden default constructor.
   */
  private ReportConfigurationUtil()
  {

  }

  /**
   * Checks, whether report processing should be aborted when an exception
   * occurs.
   *
   * @param config the configuration.
   * @return if strict error handling is enabled.
   */
  public static boolean isStrictErrorHandling (final Configuration config)
  {
    final String strictError = config.getConfigProperty
                    (JFreeReportCoreModule.STRICT_ERROR_HANDLING_KEY);
    return "true".equals(strictError);
  }
}
