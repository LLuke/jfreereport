/**
 * Date: Mar 9, 2003
 * Time: 5:11:57 PM
 *
 * $Id: RecordCreationException.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.wmf.records;

public class RecordCreationException extends Exception
{
  /**
   * Constructs an <code>Exception</code> with no specified detail message.
   */
  public RecordCreationException ()
  {
  }

  /**
   * Constructs an <code>Exception</code> with the specified detail message.
   *
   * @param s the detail message.
   */
  public RecordCreationException (final String s)
  {
    super(s);
  }
}
