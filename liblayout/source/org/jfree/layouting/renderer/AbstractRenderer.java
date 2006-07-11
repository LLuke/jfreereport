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
 * AbstractRenderer.java
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
package org.jfree.layouting.renderer;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageSizeValue;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.renderer.page.PageAreaType;
import org.jfree.layouting.renderer.page.DefaultPageGrid;
import org.jfree.layouting.renderer.page.PageGrid;
import org.jfree.util.Log;

/**
 * Creation-Date: 16.06.2006, 14:10:40
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRenderer implements Renderer
{
  private LayoutProcess layoutProcess;
  private PageGrid pageGrid;

  protected AbstractRenderer(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    Log.debug ("Creating " + getClass());
    this.layoutProcess = layoutProcess;
  }

  /**
   * This is called *after* we detected a pagebreak.
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext)
  {
    // initialize the page areas
    // first, grab the core page; this may hold the size.
    final LayoutStyle style = pageContext.getAreaDefinition
            (PageAreaType.CONTENT);
    final CSSValue rawSize = style.getValue(PageStyleKeys.SIZE);

    final PageSize pageSize =
            layoutProcess.getOutputMetaData().getDefaultPageSize();
    long pageWidth = StrictGeomUtility.toInternalValue(pageSize.getWidth());
    long pageHeight = StrictGeomUtility.toInternalValue(pageSize.getHeight());

    if (rawSize instanceof PageSizeValue)
    {
      PageSizeValue psv = (PageSizeValue) rawSize;
      CSSValue width = psv.getWidth();
      CSSValue height = psv.getWidth();
      // todo: Parse the @page rules and define what we get here ...
    }

    pageGrid = new DefaultPageGrid(pageWidth, pageHeight, 1, 1);

  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }
}
