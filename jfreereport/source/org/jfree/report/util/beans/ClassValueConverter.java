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
 * ClassValueConverter.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ClassValueConverter.java,v 1.3 2005/03/03 23:00:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

import org.jfree.util.ObjectUtilities;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from their
 * {@link String} representation.
 */
public class ClassValueConverter implements ValueConverter
{
  public ClassValueConverter ()
  {
  }

  public String toAttributeValue (final Object o)
  {
    if (o instanceof Class)
    {
      final Class c = (Class) o;
      return c.getName();
    }
    throw new ClassCastException("Give me a real type.");
  }

  public Object toPropertyValue (final String s)
  {
    try
    {
      final ClassLoader loader = ObjectUtilities.getClassLoader(ClassValueConverter.class);
      return loader.loadClass(s);
    }
    catch (ClassNotFoundException e)
    {
      throw new IllegalArgumentException("No such class.");
    }
  }
}
