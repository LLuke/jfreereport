/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------------------------
 * ImageOperationModul.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.contents.ImageContent;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ImageOperationModul extends OperationModul
{
  public ImageOperationModul()
  {
    super("image/*");
  }

  public List createOperations (Element e, Content value, Rectangle2D bounds)
  {
    // Paint
    Paint paint = (Paint) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ImageContent ic = (ImageContent) value.getContentForBounds(bounds);

    ArrayList ops = new ArrayList();
    ops.add (new PhysicalOperation.SetBoundsOperation (bounds));
    ops.add (new PhysicalOperation.SetPaintOperation(paint));
    ops.add (new PhysicalOperation.PrintImageOperation(ic.getContent()));
    return ops;
  }

  public Content createContentForElement(Element e, Rectangle2D bounds, OutputTarget ot)
  {
    ImageReference ir = (ImageReference) e.getValue();
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE))
    {
      double w = ir.getImageWidth();
      double h = ir.getImageHeight();
      double scaleX = 1;
      double scaleY = 1;

      if (w != 0)
      {
        scaleX = bounds.getWidth() / w;
      }
      if (h != 0)
      {
        scaleY = bounds.getHeight() / h;
      }
      ir.setScaleX((float)scaleX);
      ir.setScaleY((float)scaleY);
    }
    return new ImageContent(ir, bounds);
  }

}
