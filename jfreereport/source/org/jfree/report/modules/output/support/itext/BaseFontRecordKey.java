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
 * $Id: BaseFontRecordKey.java,v 1.3 2003/08/25 14:29:32 taqua Exp $
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
  /** The logical name. */
  private final String logicalName;

  /** The encoding. */
  private final String encoding;

  private int hashCode;
  /**
   * Creates a new key.
   *
   * @param logicalName  the logical name.
   * @param encoding  the encoding.
   */
  public BaseFontRecordKey(final String logicalName, final String encoding)
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
  }

  /**
   * Indicates whether some other object is "equal to" this BaseFontRecordKey.
   *
   * @param o  the object to test.
   *
   * @return true or false.
   */
  public boolean equals(final Object o)
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
  public int hashCode()
  {
    if (hashCode == 0)
    {
      int result;
      result = logicalName.hashCode();
      result = 29 * result + encoding.hashCode();
      hashCode = result;
    }
    return hashCode;
  }
}
