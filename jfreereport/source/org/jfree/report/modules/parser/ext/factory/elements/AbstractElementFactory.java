/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------------
 * AbstractElementFactory.java
 * ---------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: AbstractElementFactory.java,v 1.7 2004/05/07 14:29:48 mungady Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.elements;

import java.util.HashMap;

import org.jfree.report.Element;

/**
 * A base class for implementing the {@link ElementFactory} interface.
 *
 * @author Thomas Morgner
 */
public class AbstractElementFactory implements ElementFactory
{
  /**
   * Storage for the elements.
   */
  private final HashMap elements;

  /**
   * Creates a new element factory.
   */
  public AbstractElementFactory ()
  {
    elements = new HashMap();
  }

  /**
   * Registers an element.
   *
   * @param e the element.
   */
  public void registerElement (final Element e)
  {
    registerElement(e.getContentType(), e);
  }

  /**
   * Registers an element.
   *
   * @param type the element type.
   * @param e    the element.
   */
  public void registerElement (final String type, final Element e)
  {
    elements.put(type, e);
  }

  /**
   * Returns an element for the specified type. This implementation assumes, that all
   * elements have a public default constructor and uses Class.newInstance() to create a
   * new instance of that element.
   *
   * @param type the type.
   * @return The element.
   */
  public Element getElementForType (final String type)
  {
    final Element e = (Element) elements.get(type);
    if (e == null)
    {
      return null;
    }
    try
    {
      return (Element) e.getClass().newInstance();
    }
    catch (Exception cne)
    {
      return null;
    }
  }

  /**
   * Compares this object with the given object for equality. The object will be
   * considered equal if it is a element factory and contains the same elements.
   *
   * @param o the object that should be compared.
   * @return true, if the given object is equal, false otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof AbstractElementFactory))
    {
      return false;
    }

    final AbstractElementFactory abstractElementFactory = (AbstractElementFactory) o;

    if (!elements.keySet().equals(abstractElementFactory.elements.keySet()))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes an hashcode for this factory.
   *
   * @return the hashcode.
   *
   * @see java.lang.Object#hashCode()
   */
  public int hashCode ()
  {
    return elements.hashCode();
  }
}
