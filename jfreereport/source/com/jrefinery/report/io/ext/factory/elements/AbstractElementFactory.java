/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.Element;

import java.util.Hashtable;

/**
 * A base class for implementing the {@link ElementFactory} interface.
 * 
 * @author Thomas Morgner
 */
public class AbstractElementFactory implements ElementFactory
{
  /** Storage for the elements. */
  private Hashtable elements;

  /**
   * Creates a new element factory.
   */
  public AbstractElementFactory()
  {
    elements = new Hashtable();
  }

  /**
   * Registers an element.
   * 
   * @param e  the element.
   */
  public void registerElement (Element e)
  {
    registerElement(e.getContentType(), e);
  }

  /**
   * Registers an element.
   * 
   * @param type  the element type.
   * @param e  the element.
   */
  public void registerElement (String type, Element e)
  {
    elements.put(type, e);
  }

  /**
   * Returns an element for the specified type.
   * 
   * @param type  the type.
   * 
   * @return The element.
   */
  public Element getElementForType(String type)
  {
    Element e = (Element) elements.get(type);
    if (e == null) 
    {
      return null;
    }
    try
    {
      return (Element) e.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }
}
