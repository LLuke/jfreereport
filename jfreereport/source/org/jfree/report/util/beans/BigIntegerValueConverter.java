/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * BigIntegerValueConverter.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

import java.math.BigInteger;

/**
 * A class that handles the conversion of {@link java.math.BigInteger} attributes to and
 * from their {@link String} representation.
 */
public class BigIntegerValueConverter implements ValueConverter
{

  /**
   * Creates a new value converter.
   */
  public BigIntegerValueConverter ()
  {
    super();
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link java.math.BigInteger} expected).
   * @return A string representing the {@link java.math.BigInteger} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (o instanceof Integer)
    {
      return o.toString();
    }
    throw new ClassCastException("Give me a real type.");
  }

  /**
   * Converts a string to a {@link java.math.BigInteger}.
   *
   * @param s the string.
   * @return a {@link java.math.BigInteger}.
   */
  public Object toPropertyValue (final String s)
  {
    return new BigInteger(s);
  }
}
