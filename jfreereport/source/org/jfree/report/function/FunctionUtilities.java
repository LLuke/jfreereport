/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
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
 * ----------------------
 * FunctionUtilities.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionUtilities.java,v 1.16 2005/10/01 11:48:16 taqua Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Version 1;
 *
 */
package org.jfree.report.function;

import java.util.ArrayList;

import org.jfree.report.structure.Element;
import org.jfree.report.structure.Group;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.Section;
import org.jfree.util.ObjectUtilities;

/**
 * A collection of utility methods relating to functions.
 *
 * @author Thomas Morgner.
 */
public final class FunctionUtilities
{
  /** Default Constructor. */
  private FunctionUtilities()
  {
  }

  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band    the band that is suspected to contain the element.
   * @param element the element name.
   * @return the found element or null, if no element could be found.
   */
  public static Element findElement(final Element element,
                                    final String namespace,
                                    final String name)
  {
    if (name == null || namespace == null)
    {
      throw new NullPointerException("Element name must not be null");
    }

    if (isMatch(element, namespace, name))
    {
      return element;
    }

    if (element instanceof Section == false)
    {
      return null;
    }

    final Section band = (Section) element;
    final Node[] nodes = band.getNodeArray();
    for (int i = 0; i < nodes.length; i++)
    {
      final Node n = nodes[i];
      if (n instanceof Element == false)
      {
        continue;
      }
      final Element e = (Element) n;
      final Element retval = findElement(e, namespace, name);
      if (retval != null)
      {
        return retval;
      }
    }
    return null;
  }

  public static boolean isMatch(final Element element,
                                final String namespace,
                                final String name)
  {
    return ObjectUtilities.equal(element.getName(), name) &&
            ObjectUtilities.equal(element.getNamespace(), namespace);
  }


  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band    the band that is suspected to contain the element.
   * @param element the element name.
   * @return the found element or null, if no element could be found.
   */
  public static Element[] findAllElements(final Element element,
                                          final String namespace,
                                          final String name)
  {
    if (element == null)
    {
      throw new NullPointerException("Element must not be null");
    }
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }
    if (element instanceof Section)
    {
      Section s = (Section) element;
      final ArrayList collector = new ArrayList();
      if (isMatch(s, namespace, name))
      {
        collector.add(s);
      }
      performFindElement(s, namespace, name, collector);
      return (Element[]) collector.toArray(new Element[collector.size()]);
    }
    else
    {
      if (isMatch(element, namespace, name))
      {
        return new Element[]{element};
      }
      return new Element[0];
    }
  }

  private static void performFindElement(final Section band,
                                         final String namespace,
                                         final String name,
                                         final ArrayList collector)
  {
    final Node[] elements = band.getNodeArray();
    for (int i = 0; i < elements.length; i++)
    {

      final Node n = elements[i];
      if (n instanceof Element == false)
      {
        continue;
      }
      final Element e = (Element) n;
      if (isMatch(e, namespace, name))
      {
        collector.add(e);
      }
      if (e instanceof Section)
      {
        performFindElement((Section) e, namespace, name, collector);
      }
    }
  }

  public static Group getSubGroup(final Element element, final String groupName)
  {
    if (groupName == null)
    {
      throw new NullPointerException("Group name must not be null");
    }

    if (element instanceof Section == false)
    {
      return null;
    }

    final Section band = (Section) element;
    final Node[] nodes = band.getNodeArray();
    for (int i = 0; i < nodes.length; i++)
    {
      final Node n = nodes[i];
      if (n instanceof Section == false)
      {
        continue;
      }

      final Group retval = getSubGroup((Section) n, groupName);
      if (retval != null)
      {
        return retval;
      }
    }
    return null;
  }
}
