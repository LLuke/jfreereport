/**
 * Date: Feb 2, 2003
 * Time: 9:51:54 PM
 *
 * $Id: ReportInterruptedException.java,v 1.1 2003/02/02 22:47:39 taqua Exp $
 */
package com.jrefinery.report;

/**
 * todo
 */
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
