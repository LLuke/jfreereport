/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ------------------------
 * ImageOperationModule.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageOperationModule.java,v 1.2 2003/02/02 23:43:51 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ImageContent;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.operations.OperationModule;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of operations that relate to images.
 *
 * @author Thomas Morgner
 */
public class ImageOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public ImageOperationModule()
  {
    super("image/*");
  }

  /**
   * Creates a list of operations.
   *
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   *
   * @return a list of operations.
   */
  public List createOperations (Element e, Content value, Rectangle2D bounds)
  {
    Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ImageContent ic = (ImageContent) value.getContentForBounds(bounds);

    ArrayList ops = new ArrayList();
    ops.add (new PhysicalOperation.SetBoundsOperation (bounds));
    ops.add (new PhysicalOperation.SetPaintOperation(paint));
    ops.add (new PhysicalOperation.PrintImageOperation(ic.getContent()));
    return ops;
  }
}
