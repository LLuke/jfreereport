/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * --------------------
 * PDFOutputTarget.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PDFOutputTarget.java,v 1.3 2002/05/23 22:32:22 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 24-Apr-2002 : Support for Images and MultiLineElements.
 * 07-May-2002 : Small change for source of JFreeReport info to set creator of PDF document (DG);
 * 16-May-2002 : Interface of drawShape changhed so we can draw different line width (JS)
 *
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.util.Log;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * An output target for the report engine that generates a PDF file using the iText class library
 * (see http://www.lowagie.com/itext).
 */
public class PDFOutputTarget extends AbstractOutputTarget
{

  /** The output stream. */
  private OutputStream out;

  /** The document. */
  private Document document;

  /** The document writer. */
  private PdfWriter writer;

  /** Embed fonts? */
  private boolean embedFonts;

  /** Storage for BaseFont objects created. */
  private Map baseFonts;

  /** The current base font. */
  private BaseFont baseFont;

  private Font awtFont;

  /** The current font size. */
  private int fontSize;

  /** The stroke used for shapes */
  private Stroke awtStroke;

  private Paint awtPaint;

  /**
   * The PDFBandCursor is used to translate between the band specific coordinate space
   * and the page coordinate space.
   */
  private class PDFBandCursor extends BandCursor
  {
    public Rectangle2D getDrawBounds ()
    {
      Rectangle2D bbounds = getBandBounds ();
      Rectangle2D bounds = getElementBounds ();
      double x = bounds.getX () + bbounds.getX ();
      double y = bounds.getY () + bbounds.getY ();
      double w = bounds.getWidth ();
      double h = bounds.getHeight ();
      return new Rectangle2D.Double (x, y, w, h);
    }

    public void setDrawBounds (Rectangle2D rect)
    {
      Rectangle2D bbounds = getBandBounds ();
      super.setElementBounds (
              new Rectangle2D.Double (
                      rect.getX () - bbounds.getX (),
                      rect.getY () - bbounds.getY (),
                      rect.getWidth (),
                      rect.getHeight ()));
    }
  }

  /**
   * The pdf state is used to store and restore the current state of this outputtarget.
   */
  private class PDFState
  {
    private Font myfont;
    private Stroke mystroke;
    private Paint mypaint;

    public PDFState (PDFOutputTarget source)
    {
      save (source);
    }

    public void save (PDFOutputTarget source)
    {
      this.myfont = source.getFont ();
      this.mystroke = source.getStroke ();
      this.mypaint = source.getPaint ();
    }

    public void restore (PDFOutputTarget target) throws OutputTargetException
    {
      target.setFont (myfont);
      target.setStroke (mystroke);
      target.setPaint (mypaint);
    }
  }

  /**
   * Creates a PDFBandCursor to support coordinate space transformation.
   */
  public BandCursor createCursor ()
  {
    return new PDFBandCursor ();
  }

  /**
   * Constructs a PDFOutputTarget.
   *
   * @param out The output stream.
   * @param pageFormat The page format.
   * @param embedFonts Embed fonts?
   */
  public PDFOutputTarget (OutputStream out, PageFormat pageFormat, boolean embedFonts)
  {

    super (pageFormat);
    this.out = out;
    this.embedFonts = embedFonts;
    this.baseFonts = new TreeMap ();
  }


  /**
   * Returns the coordinate of the left edge of the page.
   */
  public float getPageX ()
  {
    return this.document.getPageSize ().left ();
  }

  /**
   * Returns the coordinate of the top of the page.
   * <P>
   * NOTE: JFreeReport works with y values increasing as you move down the page, whereas iText
   * works with y values increasing as you move up the page - this method translates the
   * iText value to match JFreeReport.
   */
  public float getPageY ()
  {
    return this.document.getPageSize ().bottom ();
  }

  /**
   * Returns the total width of the page.
   */
  public float getPageWidth ()
  {
    return this.document.getPageSize ().width ();
  }

  /**
   * Returns the total height of the page.
   */
  public float getPageHeight ()
  {
    return this.document.getPageSize ().height ();
  }

  /**
   * Returns the coordinate of the left hand edge of the usable area on the page (after taking
   * into account the left margin).
   */
  public float getUsableX ()
  {
    return this.document.getPageSize ().left () + this.document.leftMargin ();
  }

  /**
   * Returns the coordinate of the top edge of the usable area on the page (after taking
   * into account the top margin and translating to JFreeReport's orientation).
   */
  public float getUsableY ()
  {
    return this.document.getPageSize ().bottom () + this.document.topMargin ();
  }

  /**
   * Returns the width of the usable area on the page (after taking into account margins).
   */
  public float getUsableWidth ()
  {
    return this.document.getPageSize ().width ()
            - this.document.leftMargin ()
            - this.document.rightMargin ();
  }

  /**
   * Returns the height of the usable area on the page (after taking into account margins).
   */
  public float getUsableHeight ()
  {
    return this.document.getPageSize ().height ()
            - this.document.topMargin ()
            - this.document.bottomMargin ();
  }

  /**
   * Returns the currently active AWT-Font.
   */
  public Font getFont ()
  {
    return awtFont;
  }

  /**
   * Returns the current fonts fontsize.
   */
  public int getFontSize ()
  {
    return fontSize;
  }

  /**
   * Sets the current font. The font is mapped to pdf specific fonts if possible.
   * If no basefont could be created, an OutputTargetException is thrown.
   */
  public void setFont (Font font) throws OutputTargetException
  {
    if (font == null) throw new NullPointerException ();

    // do nothing if this font is already set
    if (awtFont != null && awtFont.equals (font)) return;

    this.awtFont = font;
    this.fontSize = font.getSize ();

    // use the Java logical font name to map to a predefined iText font.

    String fontKey = null;
    String logicalName = font.getName ();

    if (logicalName.equalsIgnoreCase("DialogInput") || (logicalName.equalsIgnoreCase ("Monospaced")))
    {

      if (font.isItalic ())
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.COURIER_BOLDOBLIQUE;
        }
        else
        {
          fontKey = BaseFont.COURIER_OBLIQUE;
        }
      }
      else
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.COURIER_BOLD;
        }
        else
        {
          fontKey = BaseFont.COURIER;
        }
      }
    }

    else if (logicalName.equalsIgnoreCase ("Serif"))
    {

      if (font.isItalic ())
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.TIMES_BOLDITALIC;
        }
        else
        {
          fontKey = BaseFont.TIMES_ITALIC;
        }
      }
      else
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.TIMES_BOLD;
        }
        else
        {
          fontKey = BaseFont.TIMES_ROMAN;
        }
      }

    }

    else
    { // default, this catches Dialog and SansSerif

      if (font.isItalic ())
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.HELVETICA_BOLDOBLIQUE;
        }
        else
        {
          fontKey = BaseFont.HELVETICA_OBLIQUE;
        }
      }
      else
      {
        if (font.isBold ())
        {
          fontKey = BaseFont.HELVETICA_BOLD;
        }
        else
        {
          fontKey = BaseFont.HELVETICA;
        }
      }

    }

    BaseFont f = (BaseFont) this.baseFonts.get (fontKey);

    if (f == null)
    {
      try
      {
        f = BaseFont.createFont (fontKey, BaseFont.WINANSI, this.embedFonts);
      }
      catch (Exception e)
      {
        Log.warn ("BaseFont.createFont failed.", e);
      }
    }

    if (f == null)
    {
      throw new OutputTargetException ("Null font = " + fontKey);
    }

    this.baseFont = f;

  }

  /**
   * Draws an Image from this imageReference. The image is directly embedded into the
   * pdf file to provide the best scaling support.
   */
  public void drawImage (ImageReference imageRef) throws OutputTargetException
  {
    try
    {
      Rectangle2D bounds = getCursor ().getDrawBounds ();

      Image image = Image.getInstance (imageRef.getSourceURL ());
      image.setAbsolutePosition ((float) bounds.getX (),
              (float) (getPageHeight () - bounds.getY () - bounds.getHeight ()));
      image.scaleAbsolute ((float) bounds.getWidth (), (float) bounds.getHeight ());

      document.add (image);
    }
    catch (BadElementException be)
    {
      throw new OutputTargetException ("BadElementException", be);
    }
    catch (DocumentException de)
    {
      throw new OutputTargetException ("DocumentException", de);
    }
    catch (MalformedURLException mf)
    {
      throw new OutputTargetException ("Invalid URL in ImageReference", mf);
    }
    catch (IOException mf)
    {
      throw new OutputTargetException ("URL Content could not be read", mf);
    }
  }


  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is not filled.
   */
  public void drawShape (Shape shape)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();
    float ycorr = (float) (getPageHeight () - bounds.getY ());
    float xcorr = (float) bounds.getX ();

    PathIterator pit = shape.getPathIterator (null);
    PdfContentByte cb = this.writer.getDirectContent ();

    Stroke stroke = getStroke ();
    if (stroke instanceof BasicStroke)
    {
      BasicStroke bstroke = (BasicStroke) stroke;
      cb.setLineWidth (bstroke.getLineWidth ());
    }

    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone () == false)
    {
      int cmd = pit.currentSegment (params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo (params[0] + xcorr, ycorr - params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3],
                    params[4] + xcorr, ycorr - params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo (params[0] + xcorr, ycorr - params[1],
                    params[2] + xcorr, ycorr - params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath ();
          }
      }
      pit.next ();
    }
    cb.stroke ();
  }

  /**
   * Draws a shape at the specified location. The shape is drawn using a PathIterator. All
   * Shapes are supported. Set a stroke and a paint before drawing. The shape is filled using
   * the current paint and no outline is drawn.
   */
  public void fillShape (Shape shape)
  {
    PathIterator pit = shape.getPathIterator (null);
    PdfContentByte cb = this.writer.getDirectContent ();

    int windingRule = pit.getWindingRule ();
    float[] params = new float[6];
    // How to apply this? This should be needed in fillShape
    while (pit.isDone () == false)
    {
      int cmd = pit.currentSegment (params);
      switch (cmd)
      {
        case PathIterator.SEG_MOVETO:
          {
            cb.moveTo (params[0], params[1]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            cb.lineTo (params[0], params[1]);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            cb.curveTo (params[0], params[1], params[2], params[3], params[4], params[5]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            cb.curveTo (params[0], params[1], params[2], params[3]);
            break;
          }
        case PathIterator.SEG_CLOSE:
          {
            cb.closePath ();
          }
      }
      pit.next ();
    }
    if (windingRule == PathIterator.WIND_EVEN_ODD)
    {
      cb.eoFill ();
    }
    else
    {
      cb.fill ();
    }
  }

  /**
   * This method is called when the page is ended.
   */
  public void endPage () throws OutputTargetException
  {
    try
    {
      this.document.newPage ();
    }
    catch (Exception e)
    {
      throw new OutputTargetException ("Failed to end page", e);
    }
  }

  /**
   * Opens the document.  The report title and author are added to the PDF document properties.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author) throws OutputTargetException
  {
    PageFormat pageFormat = getPageFormat ();
    float urx = (float) pageFormat.getWidth ();
    float ury = (float) pageFormat.getHeight ();

    float marginLeft = (float) pageFormat.getImageableX ();
    float marginRight =
            (float) (pageFormat.getWidth ()
            - pageFormat.getImageableWidth ()
            - pageFormat.getImageableX ());
    float marginTop = (float) pageFormat.getImageableY ();
    float marginBottom =
            (float) (pageFormat.getHeight ()
            - pageFormat.getImageableHeight ()
            - pageFormat.getImageableY ());
    Rectangle pageSize = new Rectangle (urx, ury);

    try
    {
      if (pageFormat.getOrientation () != PageFormat.PORTRAIT)
      {
        pageSize.rotate ();
      }
      else if (pageFormat.getOrientation () == PageFormat.PORTRAIT)
      {
        pageSize.rotate ();
      }
      this.document = new Document (pageSize, marginLeft, marginRight, marginTop, marginBottom);
      document.addTitle (title);
      document.addAuthor (author);
      document.addCreator (
              JFreeReport.INFO.getName () + " version " + JFreeReport.INFO.getVersion ());
      document.addCreationDate ();

      writer = PdfWriter.getInstance (document, out);
      this.document.open ();

      try
      {
        setFont (Band.DEFAULT_FONT);
        setPaint (Band.DEFAULT_PAINT);
        setStroke (ShapeElement.DEFAULT_STROKE);
      }
      catch (OutputTargetException oe)
      {
        Log.error ("Should not happen", oe);
      }
    }
    catch (Exception e)
    {
      throw new OutputTargetException ("Opening Document failed.", e);
    }

  }

  /**
   * Closes the document.
   */
  public void close ()
  {
    this.document.close ();
  }

  /**
   * Draws the band onto the specified graphics device. The Text is printed on the
   * bottom of the elements bounds.
   *
   * @param mytext The text to be printed.
   * @param align The vertical alignment for the text.
   */
  public void drawString (
          String text,
          int alignment)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();

    PdfContentByte cb = this.writer.getDirectContent ();
    cb.beginText ();
    cb.setFontAndSize (this.baseFont, this.fontSize);

    float y2 = (float) (bounds.getY () + bounds.getHeight ());
    if (alignment == Element.LEFT)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_LEFT,
              text,
              (float) bounds.getX (),
              this.getPageHeight () - y2,
              0);
    }
    else if (alignment == Element.CENTER)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_CENTER,
              text,
              (float) (bounds.getX () + (bounds.getWidth () / 2)),
              this.getPageHeight () - y2,
              0);
    }
    else if (alignment == Element.RIGHT)
    {
      cb.showTextAligned (
              PdfContentByte.ALIGN_RIGHT,
              text,
              (float) (bounds.getX () + bounds.getWidth ()),
              this.getPageHeight () - y2,
              0);
    }
    cb.endText ();
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   * @returns the width of the given string in 1/72" dpi.
   */
  protected float getStringBounds (String text, int lineStartPos, int endPos)
  {
    return (float) baseFont.getWidthPoint (text.substring (lineStartPos, endPos), getFontSize ());
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   */
  protected float getFontHeight ()
  {
    return getFontSize ();
  }

  /**
   * Defines the stroke used to draw shapes. If the stroke is of an invalid type, an
   * OutputTargetException is thrown. Currently only BasicStroke is supported.
   *
   * @throws OutputTargetException
   */
  public void setStroke (Stroke stroke) throws OutputTargetException
  {
    if (stroke == null) throw new NullPointerException ();
    if (stroke instanceof BasicStroke == false) throw new OutputTargetException ("Unable to handle this stroke type");

    // If this stroke is already set, do nothing
    if (awtStroke != null && awtStroke.equals (stroke)) return;

    this.awtStroke = stroke;
    BasicStroke bstroke = (BasicStroke) stroke;
    PdfContentByte cb = this.writer.getDirectContent ();
    cb.setLineWidth (bstroke.getLineWidth ());
  }

  /**
   * Returns the currently set stroke.
   */
  public Stroke getStroke ()
  {
    return awtStroke;
  }

  /**
   * Sets the paint. If the paint could not be converted into a pdf object, an OutputTargetException
   * is thrown. This implementation currently supports java.awt.Color as the only valid paint.
   *
   * @throws OutputTargetException if the paint is invalid.
   */
  public void setPaint (Paint paint) throws OutputTargetException
  {
    if (paint == null) throw new NullPointerException ();

    if (paint instanceof Color == false)
    {
      throw new OutputTargetException ("Unsupported paint type, currently only color is supported");
    }

    // If this paint is already set, do nothing
    if (awtPaint != null && awtPaint.equals (paint)) return;

    this.awtPaint = paint;
    PdfContentByte cb = this.writer.getDirectContent ();
    PdfPatternPainter painter = null;

    cb.setColorStroke ((Color) paint);
    cb.setColorFill ((Color) paint);
  }

  /**
   * Returns the currently set paint.
   */
  public Paint getPaint ()
  {
    return awtPaint;
  }

  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   */
  public void setClippingArea (Rectangle2D bounds)
  {
    super.setClippingArea (bounds);
  }

  /**
   * Restores the state of this target.
   *
   * @throws OutputTargetException if the given state object is not valid.
   */
  public void restoreState (Object o) throws OutputTargetException
  {
    if (o instanceof PDFState == false) throw new OutputTargetException ("Need a pdf state");
    PDFState state = (PDFState) o;
    state.restore (this);
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @returns the state container.
   */
  public Object saveState () throws OutputTargetException
  {
    return new PDFState (this);
  }
}