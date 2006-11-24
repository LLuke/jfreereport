/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.data;

import java.util.ArrayList;

import org.jfree.report.structure.Element;

/**
 * A precompute-node represents a resolved element or section of the report
 * definition. Unlike the structural nodes, these nodes can always have childs.
 * <p/>
 * The resulting tree gets pruned as early as possible - nodes which do not
 * contain precomputed or preserved expressions will not be stored.
 *
 * @author Thomas Morgner
 */
public class PrecomputeNodeImpl implements PrecomputeNode
{
  private PrecomputeNode parent;
  private PrecomputeNode next;
  private PrecomputeNode firstChild;
  private PrecomputeNode lastChild;

  private Element element;
  private String id;
  private String name;
  private String tag;
  private String namespace;
  private ArrayList functionResults;
  private ArrayList functionNames;

  public PrecomputeNodeImpl(final Element element,
                            final PrecomputeNode parent)
  {
    if (element == null)
    {
      throw new NullPointerException();
    }
    this.parent = parent;
    this.element = element;
    this.id = this.element.getId();
    this.namespace = this.element.getNamespace();
    this.tag = this.element.getType();
    this.name = this.element.getName();
  }

  public Element getElement()
  {
    return element;
  }

  public String getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getTag()
  {
    return tag;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public PrecomputeNode getParent()
  {
    return parent;
  }

  public PrecomputeNode getNext()
  {
    return next;
  }

  public void setNext(final PrecomputeNode next)
  {
    this.next = next;
  }

  public PrecomputeNode getFirstChild()
  {
    return firstChild;
  }

  public void setFirstChild(final PrecomputeNode firstChild)
  {
    this.firstChild = firstChild;
  }

  public PrecomputeNode getLastChild()
  {
    return lastChild;
  }

  public void setLastChild(final PrecomputeNode lastChild)
  {
    this.lastChild = lastChild;
  }

  public void addFunction(final String name, final Object value)
  {
    if (this.functionNames == null)
    {
      functionNames = new ArrayList();
      functionResults = new ArrayList();
    }

    this.functionNames.add(name);
    this.functionResults.add(name);
  }

  public int getFunctionCount()
  {
    if (functionNames == null)
    {
      return 0;
    }
    return functionNames.size();
  }

  public String getFunctionName(int idx)
  {
    if (functionNames == null)
    {
      throw new IndexOutOfBoundsException();
    }
    return (String) functionNames.get(idx);
  }

  public Object getFunctionResult(int idx)
  {
    if (functionResults == null)
    {
      throw new IndexOutOfBoundsException();
    }
    return functionResults.get(idx);
  }
}
