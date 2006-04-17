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
 * ContentBox.java
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
package org.jfree.layouting.normalizer.common;

import java.util.ArrayList;

import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.input.style.keys.box.DisplayRole;

/**
 * A compound element. This element consists of a logical view and a physical
 * view.
 *
 * There should be a normalization step after elements have been added. That
 * normalizer should remove empty elements and move 'run-in' and 'compact'
 * elements to their final location.
 *
 * @author Thomas Morgner
 */
public class ContentBox extends ContentNode
{
  private CompositionStrategy compositionStrategy;
  private LayoutingStrategy layoutingStrategy;
  private boolean finished;
  private ArrayList elements;

//  private boolean runInPending;
//  private boolean compactPending;

  protected ContentBox (final LayoutElement core,
                        final CompositionStrategy compositionStrategy,
                        final LayoutingStrategy layoutingStrategy)
  {
    super(core);
    this.compositionStrategy = compositionStrategy;
    this.layoutingStrategy = layoutingStrategy;
  }

  public ContentBox(final LayoutElement core,
                    final CompositionStrategy compositionStrategy)
  {
    super(core);
    this.elements = new ArrayList();
    this.compositionStrategy = compositionStrategy;
    this.layoutingStrategy = new InlineLayoutingStrategy();
  }

  public CompositionStrategy getCompositionStrategy()
  {
    return compositionStrategy;
  }

  public void setLayoutingStrategy(final LayoutingStrategy layoutingStrategy)
  {
    if (layoutingStrategy == null)
    {
      throw new NullPointerException();
    }
    this.layoutingStrategy = layoutingStrategy;
    invalidate();
  }

  public LayoutingStrategy getLayoutingStrategy()
  {
    return layoutingStrategy;
  }

  public void add(ContentNode ce)
  {
    this.compositionStrategy.addElement(this, ce);
  }

  public boolean isContainingBlock()
  {
    return (this.compositionStrategy instanceof BlockCompositionStrategy);
  }

  /**
   * Call this from outside a Composition strategy, and you will be doomed to
   * watch bad movies all day and night.
   *
   * @param node
   */
  public void addElementInternal(ContentNode node)
  {
    elements.add(node);
    node.setParent(this);
    invalidate();
  }

  public int getElementCount()
  {
    return elements.size();
  }

  public ContentNode getElementAt(int i)
  {
    return (ContentNode) elements.get(i);
  }

  public ContentNode[] getElements()
  {
    return (ContentNode[]) elements.toArray(new ContentNode[elements.size()]);
  }

  /**
   * Is a enum in the real world ..
   *
   * @return
   */
  public DisplayRole getDisplayRole()
  {
    LayoutElement element = (LayoutElement) getLayoutElement();
    if (element != null)
    {
      return element.getLayoutContext().getBoxSpecification().getDisplayRole();
    }
    else
    {
      return DisplayRole.INLINE;
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
    LayoutElement element = (LayoutElement) getLayoutElement();
    if (element != null)
    {
      return element.getName();
    }
    else
    {
      return "<@>";
    }
  }

  public void setFinished(final boolean finished)
  {
    this.finished = finished;
  }

  /**
   * A flag indicating whether the composition has been completed for this
   * element. No further element additions are allowed to this instance.
   *
   * @return true, if the composition is finished, false otherwise.
   */
  public boolean isFinished()
  {
    return finished;
  }

  public ContentBox derive() throws CloneNotSupportedException
  {
    ContentBox clone = (ContentBox) clone();
    clone.compositionStrategy = compositionStrategy.getInstance();
    clone.layoutingStrategy = layoutingStrategy.getInstance();
    clone.elements = new ArrayList();
    clone.finished = false;
    return clone;
  }

  public boolean isEmpty()
  {
    return elements.isEmpty();
  }

  public void clear()
  {
    // be carefull with that ...
    elements.clear();
  }

  protected void revalidate()
  {
    this.layoutingStrategy.updateLayout(this);
  }
}
