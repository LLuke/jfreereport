/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * AlignedLogicalPage.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: AlignedLogicalPage.java,v 1.1 2004/03/16 15:38:48 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 *
 */
package org.jfree.report.modules.output.pageable.base;

import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.PageDefinition;

/**
 * The AlignedLogicalPage corrects all values of the page format
 * so that the bounds are aligned for a given LayoutSupport.
 *
 * @author Thomas Morgner
 */
public strictfp class AlignedLogicalPage implements LogicalPage
{
  /** the layout support implementation which defines the alignments. */
  private LayoutSupport layoutSupport;
  private PageDefinition pageDefinition;

  /**
   * Creates a AlignedLogicalPageWrapper, which wraps the logicalpage to obey
   * to the alignment defined by the LayoutSupport.
   *
   * @param support the layout support which defines the alignment.
   * @throws NullPointerException if one of the parameters is null.
   */
  public AlignedLogicalPage(final LayoutSupport support, final PageDefinition pageDefinition)
  {
    if (support == null)
    {
      throw new NullPointerException();
    }
    if (pageDefinition == null)
    {
      throw new NullPointerException();
    }
    this.layoutSupport = support;
    this.pageDefinition = pageDefinition;
  }

  /**
   * A helper function which alignes the given value along the boundry.
   *
   * @param value the value that should be aligned
   * @param boundary the alignment boundary
   * @return the aligned value
   */
  private float alignDown(final float value, final float boundary)
  {
    if (boundary == 0)
    {
      return value;
    }
    return (float) Math.floor(value / boundary) * boundary;
  }

  /**
   * Returns the page width, aligned by the horizontal alignment of the layout support.
   *
   * @return the page width.
   */
  public float getWidth()
  {
    return alignDown(pageDefinition.getWidth(), layoutSupport.getHorizontalAlignmentBorder());
  }

  /**
   * Returns the page height, aligned by the vertical alignment of the layout support.
   *
   * @return the page height.
   */
  public float getHeight()
  {
    return alignDown(pageDefinition.getHeight(), layoutSupport.getVerticalAlignmentBorder());
  }

  /**
   * Gets the output target metrics.
   *
   * @return the output target metrics.
   */
  public LayoutSupport getLayoutSupport()
  {
    return layoutSupport;
  }
}
