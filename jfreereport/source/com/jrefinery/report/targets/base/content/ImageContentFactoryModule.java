/**
 * Date: Feb 7, 2003
 * Time: 10:14:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ImageContentFactoryModule implements ContentFactoryModule
{
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
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, LayoutSupport ot)
      throws ContentCreationException
  {
    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    ImageReference ir = (ImageReference) e.getValue();
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
    Rectangle2D irBounds = new Rectangle2D.Double(point.getX(),
                                                  point.getY(),
                                                  ir.getBoundsScaled().getWidth(),
                                                  ir.getBoundsScaled().getHeight());
    return new ImageContent(ir, irBounds);
  }
}
