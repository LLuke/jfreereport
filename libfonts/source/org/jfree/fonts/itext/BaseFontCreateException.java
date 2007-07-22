package org.jfree.fonts.itext;

import org.jfree.util.StackableRuntimeException;

/**
 * The BaseFontCreateException is thrown if there are problemns while creating iText
 * fonts.
 *
 * @author Thomas Morgner
 */
public class BaseFontCreateException extends StackableRuntimeException
{
  /**
   * Creates a new BaseFontCreateException with no message.
   */
  public BaseFontCreateException ()
  {
  }

  /**
   * Creates a new BaseFontCreateException with the given message and base exception.
   *
   * @param s the message for this exception
   * @param e the exception that caused this exception.
   */
  public BaseFontCreateException (final String s, final Exception e)
  {
    super(s, e);
  }

  /**
   * Creates a new BaseFontCreateException with the given message.
   *
   * @param s the message for this exception
   */
  public BaseFontCreateException (final String s)
  {
    super(s);
  }
}
