/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ---------------------------------
 * DrawableContentFactoryModule.java
 * ---------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableContentFactoryModule.java,v 1.2 2003/08/18 18:27:58 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Added standard header (DG);
 *
 */
package org.jfree.report.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.DrawableContainer;
import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;

/**
 * A factory module for drawable content.
 *
 * @author Thomas Morgner.
 */
public class DrawableContentFactoryModule implements ContentFactoryModule
{
  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(final String contentType)
  {
    return contentType.startsWith("drawable/");
  }

  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement(final Element e, final ElementLayoutInformation bounds,
                                         final LayoutSupport ot)
      throws ContentCreationException
  {
    final DrawableContainer drawable = (DrawableContainer) e.getValue();
    if (drawable == null)
    {
      return null;
    }

    final Point2D point = bounds.getAbsolutePosition();
    final Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
        bounds.getPreferredSize());
    if (iBounds.getWidth() == 0 && iBounds.getHeight() == 0)
    {
      return null;
    }
    // basic drawable object don't have own bounds, so they cannot define
    // scaling or keep-aspect ratio.
    //
    // this could be a show-stopper for WMF-Drawables, so we'll start subclassing
    // the drawable stuff soon ...

    final Rectangle2D drawableBounds = new Rectangle2D.Float(0, 0,
        (float) iBounds.getWidth(),
        (float) iBounds.getHeight());
    final DrawableContainer con = new DrawableContainer
      (drawable.getDrawable(), iBounds, drawableBounds);
    return new DrawableContent(con, point);
  }
}
