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
 * ------------------------------
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.peer.view;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.layout.doc.DocumentNode;

public abstract class AbstractCompositeView extends AbstractNodeView implements CompositeView
{
  protected static class FastArrayList extends ArrayList
  {
    public FastArrayList ()
    {
    }

    /**
     * Removes from this List all of the elements whose index is between fromIndex,
     * inclusive and toIndex, exclusive.  Shifts any succeeding elements to the left
     * (reduces their index). This call shortens the list by <tt>(toIndex - fromIndex)</tt>
     * elements. (If <tt>toIndex==fromIndex</tt>, this operation has no effect.)
     *
     * @param fromIndex index of first element to be removed.
     * @param toIndex   index after last element to be removed.
     */
    public void removeRange (final int fromIndex, final int toIndex)
    {
      super.removeRange(fromIndex, toIndex);
    }
  }

  private FastArrayList childViews;

  public AbstractCompositeView (final DocumentNode node)
  {
    super(node);
    childViews = new FastArrayList();
  }

  public void removeAll ()
  {
    replace(0, childViews.size(), new NodeView[0]);
  }

  public void insert (final int index, final NodeView view)
  {
    replace(index, 0, new NodeView[]{ view});
  }

  public void append (final NodeView view)
  {
    replace(childViews.size(), 0, new NodeView[]{ view});
  }

  /**
   * Replace child views. If there are no views to remove this acts as an
   * insert. If there are no views to add this acts as a remove. Views being
   * removed will have the parent set to null, and the internal reference
   * to them removed so that they can be garbage collected.
   *
   * @param index the starting index into the child views to insert the
   * new views >= 0
   * @param length the number of existing child views to remove >= 0
   * @param views the child views to add
   */
  public void replace (final int index, final int length, final NodeView[] views)
  {
    childViews.removeRange(index, index + length);
    childViews.addAll(index, Arrays.asList(views));
    invalidateLayout();
  }

  public int getViewCount ()
  {
    return childViews.size();
  }

  public NodeView getView (final int index)
  {
    return (NodeView) childViews.get(index);
  }

  public CompositeView getLogicalView ()
  {
    return this;
  }
}
