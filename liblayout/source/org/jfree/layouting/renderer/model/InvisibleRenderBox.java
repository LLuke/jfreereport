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
 * InvisibleRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: InvisibleRenderBox.java,v 1.1 2006/07/11 13:51:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

/**
 * An invisible render-box is a placeholder for an floating or absoluting
 * element. Although the content of such an element defines its own flow, the
 * box may be positioned relative to the expected position in the normal flow.
 *
 * @author Thomas Morgner
 */
public class InvisibleRenderBox extends RenderBox
{

  public InvisibleRenderBox()
  {
    super(new EmptyBoxDefinition());
  }

  public long getMinimumSize(int axis)
  {
    return 0;
  }

  public long getPreferredSize(int axis)
  {
    return 0;
  }

  public long getMaximumSize(int axis)
  {
    return 0;
  }

  public void validate()
  {
    // ignore that event ..
    setWidth(0);
    setHeight(0);
    setState(RenderNodeState.FINISHED);
  }

  /**
   * The reference point corresponds to the baseline of an box. For now, we
   * define only one reference point per box. The reference point of boxes
   * corresponds to the reference point of the first linebox.
   *
   * @param axis
   * @return
   */
  public long getReferencePoint(int axis)
  {
    return 0;
  }
}
