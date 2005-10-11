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
 * ---------------------------------
 * DrawableContentFactoryModule.java
 * ---------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DrawableContentFactoryModule.java,v 1.12 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Added standard header (DG);
 *
 */
package org.jfree.report.content;

import java.awt.Dimension;

import org.jfree.report.Element;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictPoint;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.ui.ExtendedDrawable;

/**
 * A factory module for drawable content.
 *
 * @author Thomas Morgner.
 */
public class DrawableContentFactoryModule implements ContentFactoryModule
{
  /**
   * DefaultConstructor.
   */
  public DrawableContentFactoryModule ()
  {
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType the content type.
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent (final String contentType)
  {
    return contentType.startsWith("drawable/");
  }

  /**
   * Creates content for an element.
   *
   * @param e      the element.
   * @param bounds the bounds.
   * @param ot     the output target.
   * @return the content.
   *
   * @throws ContentCreationException if there is a problem with the Content creation.
   */
  public Content createContentForElement (final Element e,
                                          final ElementLayoutInformation bounds,
                                          final LayoutSupport ot)
          throws ContentCreationException
  {
    final Object value = e.getValue();
    final Drawable drawable = (Drawable) value;
    if (drawable == null)
    {
      return EmptyContent.getDefaultEmptyContent();
    }

    final StrictPoint point = bounds.getAbsolutePosition();
    final StrictDimension iBounds;

    final StrictDimension preferredSize = bounds.getPreferredSize();
    if (preferredSize == null && drawable instanceof ExtendedDrawable)
    {
      final ExtendedDrawable extDrawable = (ExtendedDrawable) drawable;
      final Dimension extDim = extDrawable.getPreferredSize();
      final StrictDimension drawableSize = StrictGeomUtility.createDimension
              (extDim.getWidth(), extDim.getHeight());
      iBounds = ElementLayoutInformation.unionMin
              (bounds.getMaximumSize(), drawableSize);
    }
    else
    {
      iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                    preferredSize);
    }

    if (iBounds.getWidth() == 0 && iBounds.getHeight() == 0)
    {
      return EmptyContent.getDefaultEmptyContent();
    }
    // basic drawable object don't have own bounds, so they cannot define
    // scaling or keep-aspect ratio.
    //
    // this could be a show-stopper for WMF-Drawables, so we'll start subclassing
    // the drawable stuff soon ...

    final StrictBounds drawableBounds = new StrictBounds
            (point.getX(), point.getY(),
                    iBounds.getWidth(), iBounds.getHeight());
    return new DrawableContent(drawable, drawableBounds);
  }
}
