/**
 * Date: Dec 6, 2002
 * Time: 7:40:45 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import java.io.PrintStream;
import java.io.PrintWriter;

public class StackableException extends Exception
{
  /** The parent exception. */
  private Exception parent;

  public StackableException()
  {}

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public StackableException (String message, Exception ex)
  {
    super (message);
    parent = ex;
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public StackableException (String message)
  {
    super (message);
  }

  /**
   * Returns the parent exception (possibly null).
   *
   * @return the parent exception.
   */
  public Exception getParent ()
  {
    return parent;
  }

  /**
   * Prints the stack trace to the specified stream.
   *
   * @param stream  the output stream.
   */
  public void printStackTrace (PrintStream stream)
  {
    super.printStackTrace (stream);
    if (getParent () != null)
    {
      stream.println ("ParentException: ");
      getParent ().printStackTrace (stream);
    }
  }

  /**
   * Prints the stack trace to the specified writer.
   *
   * @param writer  the writer.
   */
  public void printStackTrace (PrintWriter writer)
  {
    super.printStackTrace (writer);
    if (getParent () != null)
    {
      writer.println ("ParentException: ");
      getParent ().printStackTrace (writer);
    }
  }

}
