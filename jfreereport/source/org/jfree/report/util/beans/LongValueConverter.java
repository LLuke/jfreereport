/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * LongValueConverter.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LongValueConverter.java,v 1.4 2005/09/20 15:38:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from their
 * {@link String} representation.
 *
 * @author Thomas Morgner
 */
public class LongValueConverter implements ValueConverter
{

  /**
   * Creates a new value converter.
   */
  public LongValueConverter ()
  {
    super();
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link Integer} expected).
   * @return A string representing the {@link Integer} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (o instanceof Long)
    {
      return o.toString();
    }
    throw new ClassCastException("Give me a real type.");
  }

  /**
   * Converts a string to a {@link Integer}.
   *
   * @param s the string.
   * @return a {@link Integer}.
   */
  public Object toPropertyValue (final String s)
  {
    return new Long(s);
  }
}
