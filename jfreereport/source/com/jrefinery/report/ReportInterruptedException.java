/**
 * Date: Feb 2, 2003
 * Time: 9:51:54 PM
 *
 * $Id$
 */
package com.jrefinery.report;

public class ReportInterruptedException extends ReportProcessingException
{
  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public ReportInterruptedException(String message, Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public ReportInterruptedException(String message)
  {
    super(message);
  }
}
