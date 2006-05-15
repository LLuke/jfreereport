package org.jfree.layouting.normalizer;

import org.jfree.util.StackableException;

public class NormalizationException extends StackableException
{
  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public NormalizationException ()
  {
  }

  /**
   * Creates an exception.
   *
   * @param message the exception message.
   */
  public NormalizationException (final String message)
  {
    super(message);
  }

  /**
   * Creates an exception.
   *
   * @param message the exception message.
   * @param ex      the parent exception.
   */
  public NormalizationException (final String message, final Exception ex)
  {
    super(message, ex);
  }
}
