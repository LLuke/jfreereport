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
 * ImageContentFactoryModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageContentFactoryModule.java,v 1.10 2003/06/29 16:59:28 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package org.jfree.report.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ImageReference;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.StringUtil;

/**
 * The ImageContentFactoryModule creates image content from the given element.
 * The content type of the used element should fit "image/*".
 *
 * @author Thomas Morgner
 */
public class ImageContentFactoryModule implements ContentFactoryModule
{
  /**
   * DefaultConstructor.
   */
  public ImageContentFactoryModule()
  {
  }

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
    return (StringUtil.startsWithIgnoreCase(contentType, "image/"));
  }

  /**
   * Creates a content wrapper for the element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   */
  public Content createContentForElement(final Element e, final ElementLayoutInformation bounds,
                                         final LayoutSupport ot)
  {
    final ImageReference ir = (ImageReference) e.getValue();
    // there is no content?
    if (ir == null)
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

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE))
    {
      final double w = ir.getImageWidth();
      final double h = ir.getImageHeight();
      double scaleX = 1;
      double scaleY = 1;

      if (w != 0)
      {
        scaleX = iBounds.getWidth() / w;
      }
      if (h != 0)
      {
        scaleY = iBounds.getHeight() / h;
      }
      if (scaleX != 1 || scaleY != 1)
      {
        if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO))
        {
          final float scale = (float) Math.min(scaleX, scaleY);
          ir.setScaleX(scale);
          ir.setScaleY(scale);
        }
        else
        {
          ir.setScaleX((float) scaleX);
          ir.setScaleY((float) scaleY);
        }
      }
    }
    final Rectangle2D irBounds = new Rectangle2D.Float((float) point.getX(),
        (float) point.getY(),
        (float) ir.getBoundsScaled().getWidth(),
        (float) ir.getBoundsScaled().getHeight());
    return new ImageContent(ir, irBounds);
  }
}
