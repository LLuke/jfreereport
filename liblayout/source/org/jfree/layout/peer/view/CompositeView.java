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

/**
 * A composite view has child views. Sometimes, a view has to split
 * the childs to fit them into the available space. In this case, the
 * displayed (physical) childs will differ from the logical document
 * structure.
 * <p>
 * For such cases, the logical view can be addressed using the
 * getLogicalView method. If the NodeView has no logical view, then
 * the self-reference (this) is returned.
 *
 * @see javax.swing.text.CompositeView
 */
public interface CompositeView extends NodeView
{
  public void removeAll ();

  public void insert (int index, NodeView view);

  public void append (NodeView view);

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
  public void replace (int index, int length, NodeView[] views);

  public int getViewCount ();

  public NodeView getView (int index);

  public CompositeView getLogicalView();
}