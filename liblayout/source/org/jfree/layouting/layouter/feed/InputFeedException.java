package org.jfree.layouting.layouter.feed;

import org.jfree.util.StackableException;

public class InputFeedException extends StackableException
{
  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public InputFeedException ()
  {
  }

  /**
   * Creates an exception.
   *
   * @param message the exception message.
   */
  public InputFeedException (final String message)
  {
    super(message);
  }

  /**
   * Creates an exception.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
  public InputFeedException (final String message, final Exception ex)
  {
    super(message, ex);
  }
}
