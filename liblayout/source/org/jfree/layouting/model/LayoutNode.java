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
 * LayoutNode.java
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.util.ObjectUtilities;

/**
 * This represents an abstract node in the document model. Each node has a style.
 *
 * @author Thomas Morgner
 */
public abstract class LayoutNode
        implements Serializable, Cloneable
{
  private ContextId contextId;
  private LayoutElement parent;
  private Map processAttributes;
  private InputSavePoint inputSavePoint;
  private LayoutContext layoutContext;
  private OutputProcessor outputProcessor;
  // this gives the position in the parent
  // this can only work, if the layout element does no reordering
  private int position;
  private Locale locale;

  protected LayoutNode (final LayoutNode completedElement)
  {
    this.contextId = completedElement.getContextId();
    this.processAttributes = new HashMap();
  }

  protected LayoutNode (final ContextId contextId,
                        final OutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
    this.layoutContext = new DefaultLayoutContext(outputProcessor);
    this.contextId = contextId;
    this.processAttributes = new HashMap();
  }

  protected void setProcessAttributes (Map processAttributes)
  {
    this.processAttributes.clear();
    this.processAttributes.putAll(processAttributes);
  }

  protected Map getProcessAttributes ()
  {
    return processAttributes;
  }

  public OutputProcessor getOutputProcessor ()
  {
    return outputProcessor;
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    LayoutNode node = (LayoutNode) super.clone();
    node.processAttributes = (Map) ObjectUtilities.clone(processAttributes);
    node.parent = null;
    //node.predecessor = null;
    return node;
  }

  public LayoutNode getPredecessor ()
  {
    if (parent == null)
    {
      return null;
    }
    if (position < 1)
    {
      return null;
    }
    return parent.getChild(position - 1);
  }

  public ContextId getContextId ()
  {
    return contextId;
  }

  public void setProcessAttribute (ProcessAttributeName name, Object value)
  {
    if (value == null)
    {
      processAttributes.remove(name);
    }
    else
    {
      processAttributes.put(name, value);
    }
  }

  public Object getProcessAttribute (ProcessAttributeName name)
  {
    return processAttributes.get(name);
  }

  public abstract LayoutStyle getStyle ();

  /**
   * Position can be -1 if the element is artificial.
   *
   * @param parent
   * @param position
   */
  public void setParent (LayoutElement parent, int position)
  {
    this.parent = parent;
    this.position = position;
  }

  protected int getPosition ()
  {
    return position;
  }

  public LayoutElement getParent ()
  {
    return parent;
  }

  public LayoutContext getLayoutContext ()
  {
    return layoutContext;
  }

  public InputSavePoint getInputSavePoint()
  {
    return inputSavePoint;
  }

  public void setInputSavePoint(final InputSavePoint inputSavePoint)
  {
    this.inputSavePoint = inputSavePoint;
  }

  public void clearFromParent()
  {
    final LayoutElement parent = getParent();
    parent.clearElement(this);
  }

  public Locale getLocale()
  {
    if (locale != null)
    {
      return locale;
    }
    if (getParent() != null)
    {
      return getParent().getLocale();
    }
    return Locale.getDefault();
  }

  public void setLocale(final Locale locale)
  {
    this.locale = locale;
  }
}
