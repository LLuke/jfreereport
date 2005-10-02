/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * VolatilePdfState.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: VolatilePdfState.java,v 1.3 2005/09/07 14:25:11 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.pdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfShading;
import com.lowagie.text.pdf.PdfShadingPattern;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.ObjectUtilities;

public class VolatilePdfState
{
  private PdfGState[] fillGState;
  private PdfGState[] strokeGState;
  private int currentFillGState;
  private int currentStrokeGState;

  private PdfContentByte contentByte;
  private BasicStroke stroke;
  private Paint fillPaint;
  private Paint strokePaint;
  private float height;
  private StrictBounds currentBounds;

  public VolatilePdfState (final PdfContentByte contentByte,
                           final float height)
  {
    if (contentByte == null)
    {
      throw new NullPointerException("ContentByte must not be null");
    }
    this.stroke = new BasicStroke(1);
    this.contentByte = contentByte;
    this.fillGState = new PdfGState[256];
    this.strokeGState = new PdfGState[256];
    this.currentFillGState = 255;
    this.currentStrokeGState = 255;
    this.height = height;
    this.currentBounds = new StrictBounds();
  }

  public StrictBounds getCurrentBounds ()
  {
    return (StrictBounds) currentBounds.clone();
  }

  public void setCurrentBounds (final StrictBounds currentBounds)
  {
    this.currentBounds.setRect(currentBounds.getX(), currentBounds.getY(),
            currentBounds.getWidth(), currentBounds.getHeight());
  }

  public void setStroke (final Stroke stroke)
  {
    if (stroke == null)
    {
      throw new NullPointerException();
    }
    if (stroke instanceof BasicStroke == false)
    {
      return;
    }
    setStrokeDiff(this.stroke, (BasicStroke) stroke);
    this.stroke = (BasicStroke) stroke;
  }

  public void setPaint (final Paint paint, final boolean fill)
  {
    if (fill)
    {
      if (ObjectUtilities.equal(this.fillPaint, paint))
      {
        return;
      }
    }
    else
    {
      if (ObjectUtilities.equal(this.strokePaint, paint))
      {
        return;
      }
    }

    if (paint instanceof Color)
    {
      setColor((Color) paint, fill);
    }
    else if (paint instanceof GradientPaint)
    {
      setGradientPaint((GradientPaint) paint, fill);
    }
    else if (paint instanceof TexturePaint)
    {
      setTexturePaint((TexturePaint) paint, fill);
    }
    else
    {
      createTextureFromPaint(paint, fill);
    }
    if (fill)
    {
      this.fillPaint = paint;
    }
    else
    {
      this.strokePaint = paint;
    }
  }

  private void createTextureFromPaint (final Paint paint,
                                       final boolean fill)
  {
    try
    {
      final int type;
      if (paint.getTransparency() == Transparency.OPAQUE)
      {
        type = BufferedImage.TYPE_3BYTE_BGR;
      }
      else
      {
        type = BufferedImage.TYPE_4BYTE_ABGR;
      }

      final float x = (float) StrictGeomUtility.toExternalValue(currentBounds.getX());
      final float y = (float) StrictGeomUtility.toExternalValue(currentBounds.getY());
      final float width = (float) StrictGeomUtility.toExternalValue(currentBounds.getWidth());
      final BufferedImage img = new BufferedImage((int) width, (int) height, type);
      final Graphics2D g = (Graphics2D) img.getGraphics();
      final AffineTransform transform =
              AffineTransform.getTranslateInstance(x, y);
      g.transform(transform);
      final AffineTransform inv = transform.createInverse();
      Shape fillRect = new Rectangle2D.Double(0, 0, img.getWidth(), img.getHeight());
      fillRect = inv.createTransformedShape(fillRect);
      g.setPaint(paint);
      g.fill(fillRect);
      g.dispose();

      final Image image = Image.getInstance(img, null);
      final PdfPatternPainter pattern = contentByte.createPattern(width, height);
      image.setAbsolutePosition(0, 0);
      pattern.addImage(image);
      if (fill)
      {
        contentByte.setPatternFill(pattern);
      }
      else
      {
        contentByte.setPatternStroke(pattern);
      }
    }
    catch(Exception e)
    {
      setColor(Color.gray, fill);
    }
  }

  private float normalizeY (final float y)
  {
    return this.height - y;
  }

  private void setTexturePaint (final TexturePaint tp,
                                final boolean fill)
  {
    try
    {
      final BufferedImage img = tp.getImage();
      final Rectangle2D rect = tp.getAnchorRect();
      final Image image = Image.getInstance(img, null);
      final PdfPatternPainter pattern = contentByte.createPattern(image.width(), image.height());
      final AffineTransform inverse = getInverseTransform();
      inverse.translate(rect.getX(), rect.getY());
      inverse.scale(rect.getWidth() / image.width(), -rect.getHeight() / image.height());

      final double[] mx = new double[6];
      inverse.getMatrix(mx);
      pattern.setPatternMatrix
              ((float) mx[0], (float) mx[1], (float) mx[2],
                      (float) mx[3], (float) mx[4], (float) mx[5]);
      image.setAbsolutePosition(0, 0);
      pattern.addImage(image);
      if (fill)
      {
        contentByte.setPatternFill(pattern);
      }
      else
      {
        contentByte.setPatternStroke(pattern);
      }
    }
    catch (Exception e)
    {
      setColor(Color.gray, fill);
    }
  }

  private AffineTransform getInverseTransform ()
  {
    final double[] mx = new double[]{
      1, 0, 0,
      -1, 1, height
    };
    return new AffineTransform(mx);
  }

  private void setGradientPaint (final GradientPaint gp,
                                 final boolean fill)
  {
    final float x = (float) StrictGeomUtility.toExternalValue(currentBounds.getX());
    final float y = (float) StrictGeomUtility.toExternalValue(currentBounds.getY());


    final Point2D p1 = gp.getPoint1();
    p1.setLocation(p1.getX() + x, p1.getY() + y);
    final Point2D p2 = gp.getPoint2();
    p2.setLocation(p2.getX() + x, p2.getY() + y);
    final Color c1 = gp.getColor1();
    final Color c2 = gp.getColor2();
    final PdfShading shading = PdfShading.simpleAxial(contentByte.getPdfWriter(),
            (float) p1.getX(), normalizeY((float) p1.getY()),
            (float) p2.getX(), normalizeY((float) p2.getY()), c1, c2);
    final PdfShadingPattern pat = new PdfShadingPattern(shading);
    if (fill)
    {
      contentByte.setShadingFill(pat);
    }
    else
    {
      contentByte.setShadingStroke(pat);
    }
  }

  private void setColor (final Color color,
                         final boolean fill)
  {
    final int alpha = color.getAlpha();
    if (fill)
    {
      if (alpha != currentFillGState)
      {
        currentFillGState = alpha;
        PdfGState gs = fillGState[alpha];
        if (gs == null)
        {
          gs = new PdfGState();
          gs.setFillOpacity((float) alpha / 255f);
          fillGState[alpha] = gs;
        }
        contentByte.setGState(gs);
      }
      contentByte.setColorFill(color);
    }
    else
    {
      if (alpha != currentStrokeGState)
      {
        currentStrokeGState = alpha;
        PdfGState gs = strokeGState[alpha];
        if (gs == null)
        {
          gs = new PdfGState();
          gs.setStrokeOpacity((float) alpha / 255f);
          strokeGState[alpha] = gs;
        }
        contentByte.setGState(gs);
      }
      contentByte.setColorStroke(color);
    }
  }


  private void setStrokeDiff (final BasicStroke nStroke,
                              final BasicStroke oStroke)
  {
    if (nStroke == oStroke)
    {
      return;
    }

    if (nStroke.getLineWidth() != oStroke.getLineWidth())
    {
      contentByte.setLineWidth(nStroke.getLineWidth());
    }
    if (nStroke.getEndCap() != oStroke.getEndCap())
    {
      switch (nStroke.getEndCap())
      {
        case BasicStroke.CAP_BUTT:
          contentByte.setLineCap(PdfContentByte.LINE_CAP_BUTT);
          break;
        case BasicStroke.CAP_SQUARE:
          contentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
          break;
        default:
          contentByte.setLineCap(PdfContentByte.LINE_CAP_ROUND);
      }
    }
    if (nStroke.getLineJoin() != oStroke.getLineJoin())
    {
      switch (nStroke.getLineJoin())
      {
        case BasicStroke.JOIN_MITER:
          contentByte.setLineJoin(0);
          break;
        case BasicStroke.JOIN_BEVEL:
          contentByte.setLineJoin(2);
          break;
        default:
          contentByte.setLineJoin(1);
      }
    }
    if (nStroke.getMiterLimit() != oStroke.getMiterLimit())
    {
      contentByte.setMiterLimit(nStroke.getMiterLimit());
    }

    if (isMakeDash(nStroke, oStroke))
    {
      final float dash[] = nStroke.getDashArray();
      contentByte.setLineDash(dash, nStroke.getDashPhase());
    }
  }

  private boolean isMakeDash (final BasicStroke nStroke, final BasicStroke oStroke)
  {
    if (nStroke.getDashArray() != null)
    {
      if (nStroke.getDashPhase() != oStroke.getDashPhase())
      {
        return true;
      }
      return !Arrays.equals(nStroke.getDashArray(), oStroke.getDashArray());
    }
    return oStroke.getDashArray() != null;
  }

  public Paint getFillPaint ()
  {
    return fillPaint;
  }

  public Paint getStrokePaint ()
  {
    return fillPaint;
  }

  public BasicStroke getStroke ()
  {
    return stroke;
  }
}
