/**
 * Date: Mar 5, 2003
 * Time: 6:38:16 PM
 *
 * $Id: DrawableContentFactoryModule.java,v 1.3 2003/03/08 20:28:46 taqua Exp $
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.Element;
import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.layout.LayoutSupport;

import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

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
  public boolean canHandleContent(String contentType)
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
  public Content createContentForElement(Element e, ElementLayoutInformation bounds,
                                         LayoutSupport ot)
      throws ContentCreationException
  {
    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    // basic drawable object don't have own bounds, so they cannot define
    // scaling or keep-aspect ratio.
    //
    // this could be a show-stopper for WMF-Drawables, so we'll start subclassing
    // the drawable stuff soon ...

    DrawableContainer drawable = (DrawableContainer) e.getValue();
    Rectangle2D drawableBounds = new Rectangle2D.Float(0, 0,
                                                       (float) iBounds.getWidth(),
                                                       (float) iBounds.getHeight());
    Log.debug ("Created Container for: " + drawableBounds);
    DrawableContainer con = new DrawableContainer(drawable.getDrawable(), iBounds, drawableBounds);
    return new DrawableContent(con, point);
  }
}
