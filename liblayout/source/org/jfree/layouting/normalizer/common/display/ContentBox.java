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
 * $Id: ContentBox.java,v 1.1 2006/04/17 21:04:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.common.display;

import java.util.ArrayList;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.normalizer.ContentGenerator;
import org.jfree.util.Log;

/**
 * A compound element. This element consists of a logical view and a physical view.
 * <p/>
 * There should be a normalization step after elements have been added. That normalizer
 * should remove empty elements and move 'run-in' and 'compact' elements to their final
 * location.
 * <p/>
 * Run-ins and compact elements are not yet implemented. They will be implemented once the
 * core is complete.
 *
 * @author Thomas Morgner
 */
public class ContentBox extends ContentNode
{
  private CompositionStrategy compositionStrategy;
  private ArrayList elements;

  private boolean finished;
  private boolean layoutable;

  public ContentBox (final LayoutElement core,
                     final CompositionStrategy compositionStrategy)
  {
    super(core);
    if (compositionStrategy == null)
    {
      throw new NullPointerException();
    }
    this.elements = new ArrayList();
    this.layoutable = true;
    this.compositionStrategy = compositionStrategy;
  }

  public CompositionStrategy getCompositionStrategy ()
  {
    return compositionStrategy;
  }

  public void add (ContentNode ce)
  {
    if (finished)
    {
      throw new IllegalStateException
              ("It is not allowed to add content once the element has been finished.");
    }
    this.compositionStrategy.addElement(this, ce);
  }

  public boolean isContainingBlock ()
  {
    return (this.compositionStrategy instanceof BlockCompositionStrategy);
  }

  /**
   * Call this from outside a Composition strategy, and you will be doomed to watch bad
   * movies all day and night.
   *
   * @param node
   */
  public void addElementInternal (ContentNode node)
  {
    elements.add(node);
    node.setParent(this);
    ContentRoot root = getRoot();
    if (root == null)
    {
      return;
    }

    ContentGenerator generator = root.getGenerator();
    if (generator == null)
    {
      return;
    }
    if (node instanceof ContentBox)
    {
      generator.nodeStarted((ContentBox) node);
    }
    else
    {
      generator.nodeProcessable(node);
    }
  }

  public int getElementCount ()
  {
    return elements.size();
  }

  public ContentNode getElementAt (int i)
  {
    return (ContentNode) elements.get(i);
  }

  public ContentNode[] getElements ()
  {
    return (ContentNode[]) elements.toArray(new ContentNode[elements.size()]);
  }

  /**
   * Is a enum in the real world ..
   *
   * @return the display role for this node
   */
  public DisplayRole getDisplayRole ()
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

  public void markFinished ()
  {
    if (finished)
    {
      return;
    }

    for (int i = 0; i < elements.size(); i++)
    {
      ContentNode node = (ContentNode) elements.get(i);
      if (node instanceof ContentBox)
      {
        ContentBox box = (ContentBox) node;
        if (box.isFinished() == false)
        {
          box.markFinished();
        }
      }
    }

    // maybe: A child-box that has been marked as finished can be removed
    // from the parent.

    // argh, that design is a little bit stupid - merge the strategies with the
    // content box again (possibly by subclassing - weekend work, this is)

    this.finished = true;

    ContentRoot root = getRoot();
    if (root == null)
    {
      Log.debug("Root is null?");
      return;
    }

    ContentGenerator generator = root.getGenerator();
    if (generator == null)
    {
      Log.debug("Generator is null?");
      return;
    }
    generator.nodeFinished(this);
  }

  /**
   * A flag indicating whether the composition has been completed for this element. No
   * further element additions are allowed to this instance.
   *
   * @return true, if the composition is finished, false otherwise.
   */
  public boolean isFinished ()
  {
    return finished;
  }

  /**
   * Derives a new, empty instance with the same configuration as this content box.
   *
   * @return the derived instance
   *
   * @throws CloneNotSupportedException
   */
  public ContentBox derive ()
          throws CloneNotSupportedException
  {
    ContentBox clone = (ContentBox) super.clone();
    clone.compositionStrategy = compositionStrategy.getInstance();
    clone.elements = new ArrayList();
    clone.finished = false;
    return clone;
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    ContentBox cb = (ContentBox) super.clone();
    cb.compositionStrategy = compositionStrategy.getInstance();
    cb.elements = (ArrayList) elements.clone();
    return cb;
  }

  public boolean isEmpty ()
  {
    return elements.isEmpty();
  }

  /**
   * This flag signals, that is element cannot compute its layout yet. For an element to
   * be layoutable, at least the width (in latin layouts) must be known.
   * <p/>
   * After we know the width, determining the height should not be a problem.
   *
   * @return true, if the element can be layouted (even if it is temporary) or false, if
   *         we need more content for the layouting process.
   */
  public boolean isLayoutable ()
  {
    ContentBox parent = getParent();
    if (parent != null)
    {
      return layoutable && parent.isLayoutable();
    }
    return layoutable;
  }
}
