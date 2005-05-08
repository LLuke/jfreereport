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
 * ValueConverter.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ValueConverter.java,v 1.3 2005/03/03 23:00:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

/**
 * A value converter is an object that can transform an object into a string or vice
 * versa.
 */
public interface ValueConverter
{
  /**
   * Converts an object to an attribute value.
   *
   * @param o the object.
   * @return the attribute value.
   */
  public String toAttributeValue (Object o) throws BeanException;

  /**
   * Converts a string to a property value.
   *
   * @param s the string.
   * @return a property value.
   */
  public Object toPropertyValue (String s) throws BeanException;
}
