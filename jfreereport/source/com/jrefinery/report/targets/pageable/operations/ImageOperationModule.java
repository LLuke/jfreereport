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
 * $Id: ImageOperationModule.java,v 1.5 2003/02/07 22:40:41 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 21-Jan-2003 : Image scaling and dynamic content
 * 07-Feb-2003 : ContentCreation extracted into separate package
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ImageContent;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the required operations to display/print image content in the output target.
 *
 * @see ImageContent
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
  public List createOperations(Element e, Content value, Rectangle2D bounds)
  {
    Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ImageContent ic = (ImageContent) value.getContentForBounds(bounds);

    ArrayList ops = new ArrayList();
    ops.add(new PhysicalOperation.SetBoundsOperation(bounds));
    ops.add(new PhysicalOperation.SetPaintOperation(paint));
    ops.add(new PhysicalOperation.PrintImageOperation(ic.getContent()));
    return ops;
  }
}
