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
 * AlignmentTools.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 15.02.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.content.Content;
import org.jfree.report.modules.output.meta.MetaElement;

public final class AlignmentTools
{
  private AlignmentTools()
  {
  }

  /**
   * Translates the given element alignment into a vertical alignment object.
   *
   * @param va the element alignment.
   * @param bounds the bounds of the element
   * @return the created alignment object.
   */
  public static VerticalBoundsAlignment getVerticalLayout
    (final ElementAlignment va, final Rectangle2D bounds)
  {
    if (va.equals(ElementAlignment.TOP))
    {
      return new TopAlignment(bounds);
    }
    else if (va.equals(ElementAlignment.MIDDLE))
    {
      return new MiddleAlignment(bounds);
    }
    else
    {
      return new BottomAlignment(bounds);
    }
  }

  /**
   * Translates the given element alignment into a horizontal alignment object.
   *
   * @param ha the element alignment.
   * @param bounds the bounds of the element
   * @return the created alignment object.
   */
  public static HorizontalBoundsAlignment getHorizontalLayout
      (final ElementAlignment ha, final Rectangle2D bounds)
  {
    if (ha.equals(ElementAlignment.CENTER))
    {
      return new CenterAlignment(bounds);
    }
    else if (ha.equals(ElementAlignment.RIGHT))
    {
      return new RightAlignment(bounds);
    }
    else
    {
      return new LeftAlignment(bounds);
    }
  }

  /**
   * Computes the alignment for the given element and content.
   *
   * @param e the element that was used to create the content.
   * @return the aligned content bounds.
   */
  public static Rectangle2D computeAlignmentBounds (final MetaElement e)
  {
    final Content content = e.getContent();
    final Rectangle2D bounds = e.getBounds();
    final ElementAlignment va
        = (ElementAlignment) e.getProperty(ElementStyleSheet.VALIGNMENT);
    final VerticalBoundsAlignment vba = getVerticalLayout(va, bounds);

    final ElementAlignment ha
        = (ElementAlignment) e.getProperty(ElementStyleSheet.ALIGNMENT);

    final HorizontalBoundsAlignment hba = getHorizontalLayout(ha, bounds);

    final Rectangle2D abounds = vba.align(hba.align(content.getBounds()));
    return abounds;
  }
}
