/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ----------------------
 * PhysicalOperation.java
 * ----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PhysicalOperation.java,v 1.3 2003/08/18 18:28:00 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.report.DrawableContainer;
import org.jfree.report.ImageReference;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;

/**
 * The base class for an operation that can be applied to an
 * {@link OutputTarget}.
 * These operations are typically added to a
 * {@link org.jfree.report.modules.output.pageable.base.physicals.PhysicalPage} 
 * in the process of being sent to the output target.
 * <P>
 * Refer to the subclasses for some examples.
 * <p>
 * A sequence of operations can be stored in a 
 * {@link org.jfree.report.modules.output.pageable.base.Spool}
 * and replayed at any time.
 *
 * @author Thomas Morgner
 */
public abstract class PhysicalOperation
{
  /**
   * An operation that sets the font for an
   * {@link OutputTarget}.
   */
  public static class SetFontOperation extends PhysicalOperation
  {
    /** The font. */
    private FontDefinition font;

    /**
     * Creates a new 'set font' operation.
     *
     * @param font  the font (null not permitted).
     */
    public SetFontOperation(final FontDefinition font)
    {
      if (font == null)
      {
        throw new NullPointerException();
      }
      this.font = font;
    }

    /**
     * Performs the operation, in this case setting the font for the output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      if (font.equals(ot.getFont()) == false)
      {
        ot.setFont(font);
      }
    }
  }

  /**
   * An operation that adds a comment to the debug log.
   */
  public static class AddComment extends PhysicalOperation
  {
    /** The comment. */
    private Object comment;

    /**
     * Creates a new 'add comment' operation.
     *
     * @param comment  the comment.
     */
    public AddComment(final Object comment)
    {
      if (comment == null)
      {
        throw new NullPointerException();
      }
      this.comment = comment;
    }

    /**
     * Adds a comment to the output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      Log.debug(new Log.SimpleMessage("Physical Operation Comment: ", comment));
    }
  }

  /**
   * An operation that sets the paint for an
   * {@link OutputTarget}.
   */
  public static class SetPaintOperation extends PhysicalOperation
  {
    /** The paint. */
    private Color paint;

    /**
     * Creates a new 'set paint' operation.
     *
     * @param paint  the paint.
     */
    public SetPaintOperation(final Color paint)
    {
      if (paint == null)
      {
        throw new NullPointerException();
      }
      this.paint = paint;
    }

    /**
     * Sets the paint for an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      if (paint.equals(ot.getPaint()) == false)
      {
        ot.setPaint(paint);
      }
    }
  }

  /**
   * An operation that sets the stroke for an
   * {@link OutputTarget}.
   */
  public static class SetStrokeOperation extends PhysicalOperation
  {
    /** The stroke. */
    private Stroke stroke;

    /**
     * Creates a new 'set stroke' operation.
     *
     * @param stroke  the stroke.
     */
    public SetStrokeOperation(final Stroke stroke)
    {
      if (stroke == null)
      {
        throw new NullPointerException();
      }
      this.stroke = stroke;
    }

    /**
     * Sets the stroke for an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      if (stroke.equals(ot.getStroke()) == false)
      {
        ot.setStroke(stroke);
      }
    }
  }

  /**
   * An operation that sets the bounds for an
   * {@link OutputTarget}.
   */
  public static class SetBoundsOperation extends PhysicalOperation
  {
    /** The bounds. */
    private Rectangle2D bounds;

    /**
     * Creates a new 'set bounds' operation.
     *
     * @param bounds  the bounds (null not permitted).
     */
    public SetBoundsOperation(final Rectangle2D bounds)
    {
      if (bounds == null)
      {
        throw new NullPointerException();
      }

      this.bounds = bounds;
    }

    /**
     * Sets the bounds for an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.setOperationBounds(bounds);
    }

    /**
     * Returns a string representing the operation, useful for debugging.
     *
     * @return a string.
     */
    public String toString()
    {
      return "SetBoundsOperation: " + bounds;
    }
  }

  /**
   * An operation that draws text on an {@link OutputTarget}.
   */
  public static class PrintTextOperation extends PhysicalOperation
  {
    /** The text. */
    private String text;

    /**
     * Creates a new 'print text' operation.
     *
     * @param text  the text.
     */
    public PrintTextOperation(final String text)
    {
      if (text == null)
      {
        throw new NullPointerException();
      }
      this.text = text;
    }

    /**
     * Draws text on an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.drawString(text);
    }

    /**
     * Returns a String representation of the operation, which can be useful for debugging.
     *
     * @return a string.
     */
    public String toString()
    {
      return "PrintTextOperation: " + text;
    }
  }

  /**
   * An operation that prints an Image on an
   * {@link OutputTarget}.
   */
  public static class PrintImageOperation extends PhysicalOperation
  {
    /** The image. */
    private ImageReference image;

    /**
     * Creates a new 'print image' operation.
     *
     * @param image  the image reference (null not permitted).
     */
    public PrintImageOperation(final ImageReference image)
    {
      if (image == null)
      {
        throw new NullPointerException();
      }
      this.image = image;
    }

    /**
     * Draws an image on an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.drawImage(image);
    }
  }

  /**
   * An operation that draws a shape on an
   * {@link OutputTarget}.
   */
  public static class PrintShapeOperation extends PhysicalOperation
  {
    /** The shape. */
    private Shape shape;

    /**
     * Creates a new 'print shape' operation.
     *
     * @param shape  the shape (null not permitted).
     */
    public PrintShapeOperation(final Shape shape)
    {
      if (shape == null)
      {
        throw new NullPointerException();
      }
      this.shape = shape;
    }

    /**
     * Draws a shape on an output target.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.drawShape(shape);
    }
  }

  /**
   * An operation that draws a filled shape on an
   * {@link OutputTarget}.
   */
  public static class PrintFilledShapeOperation extends PhysicalOperation
  {
    /** The shape. */
    private Shape shape;

    /**
     * Creates a new 'print filled shape' operation.
     *
     * @param shape  the shape.
     */
    public PrintFilledShapeOperation(final Shape shape)
    {
      if (shape == null)
      {
        throw new NullPointerException();
      }
      this.shape = shape;
    }

    /**
     * Draws a filled shape on an OutputTarget.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.fillShape(shape);
    }
  }

  /**
   * An operation that processes a Drawable on an
   * {@link OutputTarget}.
   */
  public static class ProcessDrawableOperation extends PhysicalOperation
  {
    /** The drawable. */
    private DrawableContainer drawableContainer;

    /**
     * Creates a new 'process Drawable' operation.
     *
     * @param drawableContainer the drawable.
     */
    public ProcessDrawableOperation(final DrawableContainer drawableContainer)
    {
      if (drawableContainer == null)
      {
        throw new NullPointerException();
      }
      this.drawableContainer = drawableContainer;
    }

    /**
     * Draws a filled shape on an OutputTarget.
     *
     * @param ot  the output target.
     *
     * @throws OutputTargetException if there is a problem performing the operation on the target.
     */
    public void performOperation(final OutputTarget ot) throws OutputTargetException
    {
      ot.drawDrawable(drawableContainer);
    }
  }

  /**
   * Performs an operation on an {@link OutputTarget}.
   *
   * @param ot  the output target.
   *
   * @throws OutputTargetException if there is a problem operating on the target.
   */
  public abstract void performOperation(OutputTarget ot) throws OutputTargetException;
}
