/**
 * Date: Feb 7, 2003
 * Time: 10:14:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.ShapeTransform;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.StringUtil;

import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class ShapeContentFactoryModule implements ContentFactoryModule
{
  public ShapeContentFactoryModule()
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
    return (StringUtil.startsWithIgnoreCase(contentType, "shape/"));
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
   * @throws ContentCreationException if there is a problem with the OutputTarget.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, LayoutSupport ot)
      throws ContentCreationException
  {
    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    Shape s = ShapeTransform.transformShape((Shape) e.getValue(),
                                  e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE),
                                  e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO),
                                  point,
                                  iBounds);
    return new ShapeContent (s);
  }
}
