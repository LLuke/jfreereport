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
 * PhysicalOperation.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public abstract class PhysicalOperation
{
  public static class SetFontOperation extends PhysicalOperation
  {
    private Font font;

    public SetFontOperation(Font font)
    {
      if (font == null) throw new NullPointerException();
      this.font = font;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      if (font.equals(ot.getFont()) == false)
      {
        ot.setFont(font);
      }
    }
  }

  public static class AddComment extends PhysicalOperation
  {
    private String comment;

    public AddComment(String comment)
    {
      if (comment == null) throw new NullPointerException();
      this.comment = comment;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      //Log.debug ("Physical Operation Comment: " + comment);
    }
  }

  public static class SetPaintOperation extends PhysicalOperation
  {
    private Paint paint;

    public SetPaintOperation(Paint paint)
    {
      if (paint == null) throw new NullPointerException();
      this.paint = paint;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      if (paint.equals(ot.getPaint()) == false)
      {
        ot.setPaint(paint);
      }
    }
  }

  public static class SetStrokeOperation extends PhysicalOperation
  {
    private Stroke stroke;

    public SetStrokeOperation(Stroke stroke)
    {
      if (stroke == null) throw new NullPointerException();
      this.stroke = stroke;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      if (stroke.equals(ot.getStroke()) == false)
      {
        ot.setStroke(stroke);
      }
    }
  }

  public static class SetBoundsOperation extends PhysicalOperation
  {
    private Rectangle2D bounds;

    public SetBoundsOperation(Rectangle2D bounds)
    {
      if (bounds == null) throw new NullPointerException();

      this.bounds = bounds;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      ot.setOperationBounds(bounds);
    }

    public String toString()
    {
      return "SetBoundsOperation: " + bounds;
    }
  }

  public static class PrintTextOperation extends PhysicalOperation
  {
    private String text;

    public PrintTextOperation(String text)
    {
      if (text == null) throw new NullPointerException();
      this.text = text;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      ot.drawString(text); // left aligned / deprecated
    }

    public String toString ()
    {
      return "PrintTextOperation: "  + text;
    }
  }

  public static class PrintImageOperation extends PhysicalOperation
  {
    private ImageReference image;

    public PrintImageOperation(ImageReference image)
    {
      if (image == null) throw new NullPointerException();
      this.image = image;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      ot.drawImage(image);
    }
  }

  public static class PrintShapeOperation extends PhysicalOperation
  {
    private Shape shape;

    public PrintShapeOperation(Shape shape)
    {
      if (shape == null) throw new NullPointerException();
      this.shape = shape;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      ot.drawShape(shape);
    }
  }

  public static class PrintFilledShapeOperation extends PhysicalOperation
  {
    private Shape shape;

    public PrintFilledShapeOperation(Shape shape)
    {
      if (shape == null) throw new NullPointerException();
      this.shape = shape;
    }

    public void performOperation(OutputTarget ot) throws OutputTargetException
    {
      ot.fillShape(shape);
    }
  }

  public abstract void performOperation(OutputTarget ot) throws OutputTargetException;
}
