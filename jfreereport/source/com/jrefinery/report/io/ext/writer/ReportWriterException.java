/**
 * Date: Jan 13, 2003
 * Time: 6:36:54 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.util.StackableException;

public class ReportWriterException extends StackableException
{
  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public ReportWriterException()
  {
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public ReportWriterException(String message, Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public ReportWriterException(String message)
  {
    super(message);
  }
}
