/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * $Id: ImageContentFactoryModule.java,v 1.7 2003/06/10 12:11:55 taqua Exp $
 *
 * Changes
 * -------
 * 07-Feb-2003 : Initial version, extracted from OperationFactoryModule
 */
package com.jrefinery.report.targets.base.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.StringUtil;

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
  public boolean canHandleContent(String contentType)
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
  public Content createContentForElement(Element e, ElementLayoutInformation bounds,
                                         LayoutSupport ot)
  {
    ImageReference ir = (ImageReference) e.getValue();
    // there is no content?
    if (ir == null)
    {
      return null;
    }

    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    if (iBounds.getWidth() == 0 && iBounds.getHeight() == 0)
    {
      return null;
    }

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE))
    {
      double w = ir.getImageWidth();
      double h = ir.getImageHeight();
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
          float scale = (float) Math.min (scaleX, scaleY);
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
    Rectangle2D irBounds = new Rectangle2D.Float((float) point.getX(),
                                                  (float) point.getY(),
                                                  (float) ir.getBoundsScaled().getWidth(),
                                                  (float) ir.getBoundsScaled().getHeight());
    return new ImageContent(ir, irBounds);
  }
}
