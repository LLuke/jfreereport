/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------------------
 * ElementFactoryCollector.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementFactoryCollector.java,v 1.7 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.factory.elements;

import java.util.ArrayList;
import java.util.Iterator;

import com.jrefinery.report.Element;

/**
 * An element factory that encapsulates multiple element factories.
 *
 * @author Thomas Morgner
 */
public class ElementFactoryCollector implements ElementFactory
{
  /** Storage for the element factories. */
  private ArrayList factories;

  /**
   * Creates a new element factory.
   */
  public ElementFactoryCollector()
  {
    factories = new ArrayList();
  }

  /**
   * Adds an element factory.
   *
   * @param factory  the factory.
   */
  public void addFactory(final ElementFactory factory)
  {
    factories.add(factory);
  }

  /**
   * Returns an iterator that provides access to the factories.
   *
   * @return The iterator.
   */
  public Iterator getFactories()
  {
    return factories.iterator();
  }

  /**
   * Returns an element for the given type.
   *
   * @param type  the content type.
   *
   * @return The element.
   */
  public Element getElementForType(final String type)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      final ElementFactory fact = (ElementFactory) factories.get(i);
      final Element element = fact.getElementForType(type);
      if (element != null)
      {
        return element;
      }
    }
    return null;
  }
}
