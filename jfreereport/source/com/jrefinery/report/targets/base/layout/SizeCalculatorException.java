/**
 * Date: Feb 7, 2003
 * Time: 10:21:21 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.layout;

import com.jrefinery.report.util.StackableException;

public class SizeCalculatorException extends StackableException
{
  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public SizeCalculatorException()
  {
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public SizeCalculatorException(String message, Exception ex)
  {
    super(message, ex);
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public SizeCalculatorException(String message)
  {
    super(message);
  }
}
