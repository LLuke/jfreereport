/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * RectangleShapeElement.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.geom.Rectangle2D;
import java.awt.Shape;

public class RectangleShapeElement extends ShapeElement
{
  public RectangleShapeElement ()
  {
  }

  public Rectangle2D getRectangle ()
  {
    return (Rectangle2D) getBounds();
  }

  public void setRectangle (Rectangle2D rect)
  {
    setShape(rect);
  }

  public void setShape (Shape shape)
  {
    super.setShape((Rectangle2D) shape);
    setBounds((Rectangle2D) shape);
  }

  public void draw(OutputTarget target, Band band) throws OutputTargetException
  {
    setShape(target.getCursor().getElementBounds());
    super.draw(target, band);
  }

  protected boolean shouldDraw ()
  {
    return false;
  }
}
