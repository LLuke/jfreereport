/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * LayoutElement.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.model;

import java.util.ArrayList;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.LayoutStylePool;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.util.AttributeMap;

/**
 * This class represents an element node in the document model. An element node
 * has a parent and has children.
 *
 * @author Thomas Morgner
 */
public class LayoutElement extends LayoutNode
{
  private LayoutStyle style;
  private AttributeMap attributes;
  private ArrayList childNodes;
  private String name;
  private String namespace;
  private DefaultElementContext elementContext;
  private static final LayoutNode[] EMPTY_LAYOUT = new LayoutNode[0];

  protected LayoutElement(final LayoutElement completedElement)
  {
    super(completedElement);
    this.name = completedElement.name;
    this.namespace = completedElement.namespace;
    this.attributes = new AttributeMap(completedElement.getAttributes());
    // Style and child nodes are not copied ..
  }

  public LayoutElement(final ContextId contextId,
                       final OutputProcessor outputProcessor,
                       final String namespace,
                       final String name)
  {
    super(contextId, outputProcessor);
    this.elementContext = new DefaultElementContext();
    this.attributes = new AttributeMap();
    this.childNodes = null;
    this.name = name;
    this.namespace = namespace;
  }

  public LayoutStyle getStyle()
  {
    if (style == null)
    {
      style = LayoutStylePool.getPool().getStyle();
    }
    return style;
  }

  public void setAttribute(final String namespace,
                           final String name,
                           final Object value)
  {
    attributes.setAttribute(namespace, name, value);
  }

  public Object getAttribute(final String namespace,
                             final String name)
  {
    return attributes.getAttribute(namespace, name);
  }

  public AttributeMap getAttributes()
  {
    return attributes;
  }

  protected void setAttributes(AttributeMap attributes)
  {
    this.attributes = new AttributeMap(attributes);
  }

  public String getName()
  {
    return name;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public String getId()
  {
    return (String) attributes.getAttribute(Namespaces.XML_NAMESPACE, "id");
  }

  public ElementContext getElementContext()
  {
    return elementContext;
  }

  public void addChild(LayoutNode ctx)
  {
    if (childNodes == null)
    {
      childNodes = new ArrayList();
    }
    final int insertPosition = childNodes.size();
    childNodes.add(ctx);
    ctx.setParent(this, insertPosition);
  }

//  // not allowed for sanity reasons. We do not allow reordering
//  // of elements.
//  private void removeChild (int indx)
//  {
//    if (childNodes == null)
//    {
//      throw new IndexOutOfBoundsException();
//    }
//    LayoutNode oldNode = (LayoutNode) childNodes.remove(indx);
//    oldNode.setParent(null, -1);
//  }

  public LayoutNode getChild(int indx)
  {
    if (childNodes == null)
    {
      throw new IndexOutOfBoundsException();
    }
    return (LayoutNode) childNodes.get(indx);
  }

  public int getChildCount()
  {
    if (childNodes == null)
    {
      return 0;
    }
    return childNodes.size();
  }

  public LayoutNode[] getChilds()
  {
    if (childNodes == null)
    {
      return EMPTY_LAYOUT;
    }
    return (LayoutNode[]) childNodes.toArray(new LayoutNode[childNodes.size()]);
  }

  private void clearElement(int position)
  {
    if (childNodes == null)
    {
      throw new IndexOutOfBoundsException();
    }
    LayoutNode oldNode = (LayoutNode) childNodes.set(position, null);
    oldNode.setParent(null, -1);
  }

  public void setParent(LayoutElement parent, final int position)
  {
    super.setParent(parent, position);
    if (elementContext == null)
    {
      return;
    }

    if (parent != null)
    {
      elementContext.setParent(parent.getElementContext());
    }
    else
    {
      elementContext.setParent(null);
    }
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object. The result should be a concise but informative representation
   * that is easy for a person to read. It is recommended that all subclasses
   * override this method.
   * <p/>
   * The <code>toString</code> method for class <code>Object</code> returns a
   * string consisting of the name of the class of which the object is an
   * instance, the at-sign character `<code>@</code>', and the unsigned
   * hexadecimal representation of the hash code of the object. In other words,
   * this method returns a string equal to the value of: <blockquote>
   * <pre>
   * getClass().getName() + '@' + Integer.toHexString(hashCode())
   * </pre></blockquote>
   *
   * @return a string representation of the object.
   */
  public String toString()
  {
    return "LayoutElement={name=" + name + ",  childs=" + (childNodes == null ? 0 : childNodes
            .size()) + "}";
  }

  public void clearElement(LayoutNode element)
  {
    // step one: Check for child-parent-relationship
    if (this != element.getParent())
    {
      return;
    }

    final int position = element.getPosition();
    if (position == -1)
    {
      return;
    }

    LayoutNode node = (LayoutNode) childNodes.get(position);
    if (node != element)
    {
      throw new IllegalStateException("Ho ho ho ho ho");
    }
    clearElement(position);
  }

  public void clearFromParent()
  {
    super.clearFromParent();
    if (style != null)
    {
      style.dispose();
      style = null;
    }
  }
}
