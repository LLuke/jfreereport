/**
 * Date: Feb 7, 2003
 * Time: 10:14:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TextContentFactoryModule implements ContentFactoryModule
{
  public TextContentFactoryModule()
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
    return contentType.equalsIgnoreCase("text/plain");
  }

  /**
   * Creates a content wrapper for the element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   * @throws ContentCreationException
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, LayoutSupport ot)
    throws ContentCreationException
  {
    Point2D point = bounds.getAbsolutePosition();

    // TextElement has a defined width (Max(MinSize, PrefSize).
    // and a maximum height (Min(MaxSize, PrefSize).

    Dimension2D wDim = ElementLayoutInformation.unionMax(bounds.getMinimumSize(),
                                                           bounds.getPreferredSize());
    Dimension2D hDim = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                           bounds.getPreferredSize());
    Dimension2D dim = new FloatDimension(wDim.getWidth(), hDim.getHeight());

    String text = (String) e.getValue();
    FontDefinition f = e.getStyle().getFontDefinitionProperty();
    Rectangle2D tBounds = new Rectangle2D.Double(point.getX(),
                                                  point.getY(),
                                                  dim.getWidth(),
                                                  dim.getHeight());

    Float lh = (Float) e.getStyle().getStyleProperty(ElementStyleSheet.LINEHEIGHT);
    try
    {
      TextContent tc = new TextContent(text, lh.floatValue(), tBounds, ot.createTextSizeCalculator(f));
      return tc;
    }
    catch (SizeCalculatorException se)
    {
      throw new ContentCreationException("Failed to create the TextSizeCalculator", se);
    }
  }
}
