/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * LayoutElement.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.11.2005 : Initial version
 */
package org.jfree.layouting.model;

import java.util.HashMap;
import java.util.LinkedList;

import org.jfree.layouting.layouter.state.InputSavePoint;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessor;

/**
 * This class represents an element node in the document model. An element node
 * has a parent and has children.
 *
 * @author Thomas Morgner
 */
public class LayoutElement extends LayoutNode
{
  private LayoutStyle style;
  private HashMap attributes;
  private LinkedList childNodes;
  private String name;
  private DefaultElementContext elementContext;

  public LayoutElement(final ContextId contextId,
                       final OutputProcessor outputProcessor,
                       final String name)
  {
    super(contextId, outputProcessor);
    this.elementContext = new DefaultElementContext();
    this.style = new LayoutStyle();
    this.attributes = new HashMap();
    this.childNodes = new LinkedList();
    this.name = name;
  }

  public LayoutStyle getStyle()
  {
    return style;
  }

  public void setAttribute(String name, Object value)
  {
    attributes.put(name, value);
  }

  public Object getAttribute(String name)
  {
    return attributes.get(name);
  }

  public String[] getAttributeNames ()
  {
    return (String[]) attributes.keySet().toArray(new String[attributes.size()]);
  }

  public String getName()
  {
    return name;
  }

  public String getId()
  {
    return (String) attributes.get("id");
  }

  public ElementContext getElementContext()
  {
    return elementContext;
  }

  public void addChild(LayoutNode ctx)
  {
    childNodes.add(ctx);
    ctx.setParent(this);
    if (childNodes.size() > 1)
    {
      LayoutNode prevNode = (LayoutNode) childNodes.get(childNodes.size() - 2);
      ctx.setPredecessor(prevNode);
    }
  }

  public void removeChild(int indx)
  {
    LayoutNode oldNode = (LayoutNode) childNodes.remove(indx);
    oldNode.setPredecessor(null);
    oldNode.setParent(null);
    if (indx == 0)
    {
      LayoutNode firstNode = (LayoutNode) childNodes.get(0);
      firstNode.setPredecessor(null);
    }
    else
    {
      LayoutNode prevNode = (LayoutNode) childNodes.get(indx-1);
      LayoutNode nextNode = (LayoutNode) childNodes.get(indx);
      nextNode.setPredecessor(prevNode);
    }
  }

  public LayoutNode getChild(int indx)
  {
    return (LayoutNode) childNodes.get(indx);
  }

  public int getChildCount()
  {
    return childNodes.size();
  }

  public LayoutNode[] getChilds()
  {
    return (LayoutNode[]) childNodes.toArray(new LayoutNode[childNodes.size()]);
  }

  public void replaceElement(int position, LayoutNode ctx)
  {
    LayoutNode oldNode = (LayoutNode) childNodes.set(position, ctx);
    oldNode.setParent(null);
    oldNode.setPredecessor(null);
    ctx.setParent(this);
    if (position == 0)
    {
      ctx.setPredecessor(null);
    }
    else
    {
      ctx.setPredecessor(getChild(position - 1));
    }
  }

  public InputSavePoint getSavePoint()
  {
    return (InputSavePoint) getProcessAttribute(ProcessAttributeName.INPUT_SAVE_POINT);
  }

  public void setSavePoint(final InputSavePoint savePoint)
  {
    setProcessAttribute(ProcessAttributeName.INPUT_SAVE_POINT, savePoint);
  }

  protected void setParent (LayoutElement parent)
  {
    super.setParent(parent);
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
   * Returns a string representation of the object. In general, the <code>toString</code>
   * method returns a string that "textually represents" this object. The result should be
   * a concise but informative representation that is easy for a person to read. It is
   * recommended that all subclasses override this method.
   * <p/>
   * The <code>toString</code> method for class <code>Object</code> returns a string
   * consisting of the name of the class of which the object is an instance, the at-sign
   * character `<code>@</code>', and the unsigned hexadecimal representation of the hash
   * code of the object. In other words, this method returns a string equal to the value
   * of: <blockquote>
   * <pre>
   * getClass().getName() + '@' + Integer.toHexString(hashCode())
   * </pre></blockquote>
   *
   * @return a string representation of the object.
   */
  public String toString ()
  {
    return "LayoutElement={name=" + name + ",  childs=" + childNodes.size()+ "}";
  }
}
