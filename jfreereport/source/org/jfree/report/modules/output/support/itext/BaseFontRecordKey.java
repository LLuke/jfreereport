/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ---------------------
 * BaseFontRecordKey.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontRecordKey.java,v 1.8 2005/02/19 13:30:00 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 * 01-Feb-2002 : Refactoring moved this class from package
 *               com.jefinery.report.targets.pageable.output
 *
 */

package org.jfree.report.modules.output.support.itext;

/**
 * A PDF font record key. This class is immutable.
 *
 * @author Thomas Morgner
 */
public final class BaseFontRecordKey
{
  /**
   * The logical name.
   */
  private final String logicalName;

  /**
   * The encoding.
   */
  private final String encoding;

  private boolean embedded;

  /**
   * The cached hashcode for this object.
   */
  private int hashCode;

  /**
   * Creates a new key.
   *
   * @param logicalName the logical name.
   * @param encoding    the encoding.
   */
  public BaseFontRecordKey (final String logicalName,
                            final String encoding, final boolean embedded)
  {
    if (logicalName == null)
    {
      throw new NullPointerException("Logical font name is null.");
    }
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null.");
    }
    this.logicalName = logicalName;
    this.encoding = encoding;
    this.embedded = embedded;
  }

  /**
   * Indicates whether some other object is "equal to" this BaseFontRecordKey.
   *
   * @param o the object to test.
   * @return true or false.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof BaseFontRecordKey))
    {
      return false;
    }

    final BaseFontRecordKey key = (BaseFontRecordKey) o;
    if (embedded != key.embedded)
    {
      return false;
    }
    if (!logicalName.equals(key.logicalName))
    {
      return false;
    }
    if (!encoding.equals(key.encoding))
    {
      return false;
    }
    return true;
  }

  /**
   * Returns a hash code for the key.
   *
   * @return the hash code.
   */
  public int hashCode ()
  {
    if (hashCode == 0)
    {
      int result;
      result = embedded ? 0 : 1;
      result = 29 * result + logicalName.hashCode();
      result = 29 * result + encoding.hashCode();
      hashCode = result;
    }
    return hashCode;
  }

  /**
   * Returns a string representation of the object. In general, the <code>toString</code>
   * method returns a string that "textually represents" this object. The result should be
   * a concise but informative representation that is easy for a person to read. It is
   * recommended that all subclasses override this method.
   * <p/>
   * The <code>toString</code> method for class <code>Object</code> returns a string
   * consisting of the name of the class of which the object is an instance, the at-sign
   * character `<code>@</code>', and the unsigned hexadecimal representation of the hash
   * code of the object. In other words, this method returns a string equal to the value
   * of: <blockquote>
   * <pre>
   * getClass().getName() + '@' + Integer.toHexString(hashCode())
   * </pre></blockquote>
   *
   * @return a string representation of the object.
   */
  public String toString ()
  {
    return ("FontKey={name=" + logicalName + "; encoding=" +
            encoding + "; embedded=" + embedded + "}");

  }
}
