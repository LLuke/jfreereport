/**
 * Date: Jan 13, 2003
 * Time: 4:58:19 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.StackableException;

public class ObjectFactoryException extends StackableException
{
  /**
   * Constructs a new exception with <code>null</code> as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   */
  public ObjectFactoryException()
  {
  }

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param   message   the detail message. The detail message is saved for
   *          later retrieval by the {@link #getMessage()} method.
   */
  public ObjectFactoryException(String message)
  {
    super(message);
  }

  public ObjectFactoryException(String message, Exception cause)
  {
    super(message, cause);
  }
}
