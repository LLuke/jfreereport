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

import org.jfree.layout.doc.DocumentNode;

/**
 * The layouting is done as follows: first, query the minor axis values
 * (minimum, maximum, preferred) and chose a size for that axis. Then
 * check the maximum axis, query the values and define the size for that
 * too.
 * <p>
 * When printing bands, which span multiple pages, we have to take the
 * pageboundaries into account. For this, the document builder should introduce
 * a mechanism for splitting the views at these boundaries.
 * <p>
 * <i>Note (A) Introcude a grid-style view implementation for that?</i>
 * <p>
 * <i>Note (B) Most of the ideas used here are derived from the document model
 * of javax.swing.text.</i>
 *
 */
public interface NodeView
{
  public static final int X_AXIS = 1;
  public static final int Y_AXIS = 2;

  public static final int ForcedBreakWeight = 300;
  public static final int ExcellentBreakWeight = 200;
  public static final int GoodBreakWeight = 100;
  public static final int BadBreakWeight = 0;


  public int getBreakWeight (final int axis,
                             final float len);

  /**
   * Breaks the current view into partial views. If <code>force</force> is not
   * set to true, then the implementation should choose a suitable location for
   * breaking the content or return the unmodified content if the content is not
   * suitable for breaking widthin the given width. When <code>force</code> is
   * set to true, then the content must be broken at all costs. If this is not
   * possible, then return the smallest possible entity - this entity will be
   * rendered regardless whether the outcome will be correct.
   *
   * @param axis
   * @param startOffset
   * @param width
   * @param forceBreak
   * @return the new view
   */
  public NodeView breakView (int axis, float startOffset,
                             float width, boolean forceBreak);

  public float getMinimumSpan(int axis);
  public float getPreferredSpan(int axis);
  public float getMaximumSpan(int axis);
  public float getAlignment(int axis);

  public float getSpanSize (int axis);
  public void setSpanSize (int axis, float value);

  public int getMajorAxis();
  public int getMinorAxis();

  public void setLocation (float x, float y);
  public float getX();
  public float getY();

  public DocumentNode getNode();
  public ViewFactory getViewFactory();
  public NodeView getParent();
}
