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
 * $Id: PDFOutputTarget.java,v 1.3 2002/05/14 21:35:05 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 24-Apr-2002 : Support for Images and MultiLineElements.
 * 07-May-2002 : Small change for source of JFreeReport info to set creator of PDF document (DG);
 */

package com.jrefinery.report.pdf;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.OutputTarget;
import com.jrefinery.report.util.Log;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.print.PageFormat;
import java.io.OutputStream;
import java.text.BreakIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * An output target for the report engine that generates a PDF file using the iText class library
 * (see http://www.lowagie.com/itext).
 */
public class PDFOutputTarget implements OutputTarget
{

  /** The output stream. */
  protected OutputStream out;

  /** The page format. */
  protected PageFormat pageFormat;

  /** The document. */
  protected Document document;

  /** The document writer. */
  protected PdfWriter writer;

  /** Embed fonts? */
  protected boolean embedFonts;

  /** Storage for BaseFont objects created. */
  protected Map baseFonts;

  /** The current base font. */
  protected BaseFont baseFont;

  /** The current font size. */
  protected int fontSize;

  /**
   * Constructs a PDFOutputTarget.
   *
   * @param out The output stream.
   * @param pageFormat The page format.
   * @param embedFonts Embed fonts?
   */
  public PDFOutputTarget (OutputStream out, PageFormat pageFormat, boolean embedFonts)
  {

    this.out = out;
    setPageFormat (pageFormat);
    this.embedFonts = embedFonts;
    this.baseFonts = new TreeMap ();

  }

  /**
   * Returns the page format.
   */
  public PageFormat getPageFormat ()
  {
    return this.pageFormat;
  }

  /**
   * Sets the page format.
   *
   * @param pageFormat The page format.
   */
  public void setPageFormat (PageFormat pageFormat)
  {
    this.pageFormat = pageFormat;
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
    return this.document.getPageSize ().width () - this.document.leftMargin ()
            - this.document.rightMargin ();
  }

  /**
   * Returns the height of the usable area on the page (after taking into account margins).
   */
  public float getUsableHeight ()
  {
    return this.document.getPageSize ().height () - this.document.topMargin ()
            - this.document.bottomMargin ();
  }

  /**
   * Draws a string within a rectangular area.
   */
  public void drawString (String text,
                          float x1, float y1, float x2, float y2, int alignment)
  {

    PdfContentByte cb = this.writer.getDirectContent ();
    cb.beginText ();
    cb.setFontAndSize (this.baseFont, this.fontSize);

    if (alignment == Element.LEFT)
    {
      cb.showTextAligned (PdfContentByte.ALIGN_LEFT, text,
              x1, this.getPageHeight () - y2, 0);
    }
    else if (alignment == Element.CENTER)
    {
      cb.showTextAligned (PdfContentByte.ALIGN_CENTER, text,
              (x1 + x2) / 2, this.getPageHeight () - y2, 0);
    }
    else if (alignment == Element.RIGHT)
    {
      float w = this.baseFont.getWidthPoint (text, this.fontSize);
      // use ALIGN_LEFT here because we are calculating the x position ourselves...
      cb.showTextAligned (PdfContentByte.ALIGN_LEFT, text,
              x2 - w, this.getPageHeight () - y2, 0);
    }

    cb.endText ();

  }

  /**
   * Sets the current font.
   */
  public void setFont (Font font)
  {

    this.fontSize = font.getSize ();

    // use the Java logical font name to map to a predefined iText font.
    String fontKey = null;
    String logicalName = font.getName ();

    if (logicalName.equals ("DialogInput") || (logicalName.equals ("Monospaced")))
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

    else if (logicalName.equals ("Serif"))
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
    {  // default, this catches Dialog and SansSerif

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
        f = BaseFont.createFont (fontKey, BaseFont.CP1252, this.embedFonts);
      }
      catch (Exception e)
      {
        Log.warn ("BaseFont.createFont failed.", e);
      }
    }

    if (f == null)
    {
      Log.warn ("Null font = " + fontKey);
    }

    this.baseFont = f;

  }

  /**
   * Sets the paint.
   */
  public void setPaint (Paint paint)
  {
    // haven't looked into how to do this with iText yet...
  }

  public void drawImage (ImageReference imageRef, float x, float y)
  {
    try
    {
      System.out.println (imageRef.getSourceURL ());
      com.lowagie.text.Image image = com.lowagie.text.Image.getInstance (imageRef.getSourceURL ());
      image.setAbsolutePosition (x + imageRef.getX (), getPageHeight () - y - imageRef.getY () - imageRef.getHeight ());
      image.scaleAbsolute (imageRef.getWidth (), imageRef.getHeight ());
      document.add (image);
    }
    catch (Exception be)
    {
      be.printStackTrace ();
    }
  }

  /**
   * Draws a shape at the specified location.  At the moment, this method only supports drawing
   * lines (instances of Line2D).
   */
  public void drawShape (Shape shape, float x, float y)
  {

    if (shape instanceof Line2D)
    {

      Line2D line = (Line2D) shape;
      try
      {
        PdfContentByte cb = this.writer.getDirectContent ();
        float x1 = (float) line.getX1 () + x;
        float y1 = this.getPageHeight () - ((float) line.getY1 () + y);
        float x2 = (float) line.getX2 () + x;
        float y2 = this.getPageHeight () - ((float) line.getY2 () + y);
        cb.moveTo (x1, y1);
        cb.lineTo (x2, y2);
        cb.stroke ();
      }
      catch (Exception e)
      {
        Log.error ("Drawing line failed." , e);
      }

    }

  }

  /**
   * This method is called when the page is ended.
   */
  public void endPage ()
  {
    try
    {
      this.document.newPage ();
    }
    catch (Exception e)
    {
      Log.error ("Failed to end page", e);
    }
  }

  /**
   * Opens the document.  The report title and author are added to the PDF document properties.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author)
  {

    float urx = (float) this.pageFormat.getWidth ();
    float ury = (float) this.pageFormat.getHeight ();

    float marginLeft = (float) this.pageFormat.getImageableX ();
    float marginRight = (float) (this.pageFormat.getWidth ()
            - this.pageFormat.getImageableWidth ()
            - this.pageFormat.getImageableX ());
    float marginTop = (float) this.pageFormat.getImageableY ();
    float marginBottom = (float) (this.pageFormat.getHeight ()
            - this.pageFormat.getImageableHeight ()
            - this.pageFormat.getImageableY ());
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
      this.document = new Document (pageSize, marginLeft, marginRight,
              marginTop, marginBottom);
      document.addTitle (title);
      document.addAuthor (author);
      document.addCreator (JFreeReport.INFO.getName () + " version " + JFreeReport.INFO.getVersion ());
      document.addCreationDate ();

      writer = PdfWriter.getInstance (document, out);
      this.document.open ();
    }
    catch (Exception e)
    {
      Log.error ("Opening Document failed.", e);
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
   * Draws the band onto the specified graphics device.
   * @param g2 The graphics device.
   * @param x The x-coordinate for the band.
   * @param y The y-coordinate for the band.
   */
  public void drawMultiLineText (String mytext, float x1, float y1, float x2, float y2, int align)
  {
    Vector lines = breakLines (mytext, x2 - x1);
    int fontheight = fontSize;

    for (int i = 0; i < lines.size (); i++)
    {
      String line = (String) lines.elementAt (i);
      drawString (line, x1, y1 + i * fontheight, x2, y2, align);
    }

  }

  /**
   * Breaks the text into multiple lines
   */
  private Vector breakLines (String mytext, float w)
  {
    BreakIterator breakit = BreakIterator.getWordInstance ();
    breakit.setText (mytext);

    //Font font = g2.getFont ();

    Vector lines = new Vector ();
    int pos = 0;
    int len = mytext.length ();

    while (pos < len)
    {
      int last = breakit.next ();
      float x = 0;

      while (x < w && last != BreakIterator.DONE)
      {
        x = (float) baseFont.getWidthPoint (mytext, fontSize);
        last = breakit.next ();
      }

      if (last == BreakIterator.DONE)
      {
        lines.add (mytext.substring (pos));
        pos = len;
      }
      else
      {
        lines.add (mytext.substring (pos, last));
        pos = last;
      }
    }
    return lines;
  }
}
