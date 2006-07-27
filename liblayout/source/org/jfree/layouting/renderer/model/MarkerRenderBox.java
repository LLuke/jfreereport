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
 * MarkerBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MarkerRenderBox.java,v 1.2 2006/07/26 11:52:07 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.list.ListStyleKeys;
import org.jfree.layouting.input.style.keys.list.ListStylePosition;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;

/**
 * A marker box is a special box, which may live outside the normal-flow,
 * but is tightly coupled with a list-item element.
 *
 * @author Thomas Morgner
 */
public class MarkerRenderBox extends InlineRenderBox
{
  private boolean outside;

  public MarkerRenderBox(final BoxDefinition boxDefinition,
                         final LayoutContext layoutContext)
  {
    super(boxDefinition, layoutContext.getStyle().getValue
            (LineStyleKeys.VERTICAL_ALIGN));
    CSSValue position =
            layoutContext.getStyle().getValue(ListStyleKeys.LIST_STYLE_POSITION);
    this.outside = ListStylePosition.OUTSIDE.equals(position);
  }

  public boolean isOutside()
  {
    return outside;
  }

  protected boolean isIgnorableForMargins()
  {
    // if the marker is defined as inside-marker, then it behaves like an
    // ordinary inline element
    if (outside)
    {
      return outside;
    }
    return super.isIgnorableForMargins();
  }

  protected void invalidateMargins()
  {
    super.invalidateMargins();
  }
}
