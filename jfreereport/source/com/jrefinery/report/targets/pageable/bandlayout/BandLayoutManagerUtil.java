/**
 * Date: Nov 27, 2002
 * Time: 4:31:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.bandlayout;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.pageable.OutputTarget;

import java.awt.geom.Rectangle2D;

public class BandLayoutManagerUtil
{
  public static BandLayoutManager getLayoutManager (Element e, OutputTarget ot)
  {
    BandLayoutManager lm =
        (BandLayoutManager) e.getStyle().getStyleProperty(
            BandLayoutManager.LAYOUTMANAGER);

    if (lm == null)
    {
      lm = ot.getDefaultLayoutManager();
      e.getStyle().setStyleProperty(BandLayoutManager.LAYOUTMANAGER, lm);
    }
    return lm;
  }

  public static Rectangle2D getBounds (Element e, Rectangle2D bounds)
  {
    if (bounds == null)
    {
      bounds = new Rectangle2D.Float ();
    }
    bounds.setRect((Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));
    return bounds;
  }

  public static void setBounds (Element e, Rectangle2D bounds)
  {
    e.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds.getBounds2D());
  }
}
