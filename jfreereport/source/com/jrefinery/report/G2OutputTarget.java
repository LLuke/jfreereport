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
 * -------------------
 * G2OutputTarget.java
 * -------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: G2OutputTarget.java,v 1.1.1.1 2002/04/25 17:02:19 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : MultilineText is working again, ImageElement support
 *
 */

package com.jrefinery.report;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.text.BreakIterator;
import java.util.Vector;

/**
 * A report output target that uses a Graphics2D object to draw the report.  This allows reports
 * to be printed on the screen and on the printer.
 */
public class G2OutputTarget implements OutputTarget
{

  /** The graphics device. */
  private Graphics2D g2;

  /** The page format. */
  private PageFormat pageFormat;

  /**
   * Constructs an OutputTarget for drawing to a Java Graphics2D object.
   *
   * @param g2 The graphics device.
   * @param pageFormat The page format.
   */
  public G2OutputTarget (Graphics2D g2, PageFormat pageFormat)
  {
    this.g2 = g2;
    this.pageFormat = pageFormat;
  }

  /**
   * Sets the graphics device for this output target.
   *
   * @param g2 The graphics device.
   */
  public void setGraphics2D (Graphics2D g2)
  {
    if (g2 == null)
      throw new NullPointerException ("Graphics must not be null");

    this.g2 = g2;
  }

  public Graphics2D getGraphics2D ()
  {
    return g2;
  }

  /**
   * Opens the target.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author)
  {
    // do nothing.
  }

  /**
   * Closes the target.
   */
  public void close ()
  {
    // do nothing.
  }

  /**
   * Returns the page format.
   *
   * @return The page format.
   */
  public PageFormat getPageFormat ()
  {
    return this.pageFormat;
  }

  /**
   * Sets the page format.
   *
   * @param format The page format.
   */
  public void setPageFormat (PageFormat format)
  {
    this.pageFormat = format;
  }

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return The left edge of the page.
   */
  public float getPageX ()
  {
    return 0.0f;
  }

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return The top edge of the page.
   */
  public float getPageY ()
  {
    return 0.0f;
  }

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return The page width.
   */
  public float getPageWidth ()
  {
    return (float) (pageFormat.getWidth ());
  }

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return The page height.
   */
  public float getPageHeight ()
  {
    return (float) (pageFormat.getHeight ());
  }

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return The left edge of the printable area of the page.
   */
  public float getUsableX ()
  {
    return (float) (pageFormat.getImageableX ());
  }

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return The top edge of the printable area of the page.
   */
  public float getUsableY ()
  {
    return (float) (pageFormat.getImageableY ());
  }

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return The width of the printable area of the page.
   */
  public float getUsableWidth ()
  {
    return (float) (pageFormat.getImageableWidth ());
  }

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return The height of the printable area of the page.
   */
  public float getUsableHeight ()
  {
    return (float) (pageFormat.getImageableHeight ());
  }

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   * @param x1 The x-coordinate for the upper left corner.
   * @param y1 The y-coordinate for the upper left corner.
   * @param x2 The x-coordinate for the lower right corner.
   * @param y2 The y-coordinate for the lower right corner.
   * @param alignment The horizontal alignment.
   */
  public void drawString (String text,
                          float x1, float y1, float x2, float y2,
                          int alignment)
  {

    float x = x1;
    float baseline = y2;

    if (alignment == Element.LEFT)
    {
      // no adjustment required
    }
    else if (alignment == Element.CENTER)
    {
      FontRenderContext frc = g2.getFontRenderContext ();
      Rectangle2D textBounds = g2.getFont ().getStringBounds (text, frc);
      x = ((x1 + x2) / 2) - ((float) (textBounds.getWidth ()) / 2);
    }
    else if (alignment == Element.RIGHT)
    {
      FontRenderContext frc = g2.getFontRenderContext ();
      Rectangle2D textBounds = g2.getFont ().getStringBounds (text, frc);
      x = x2 - (float) (textBounds.getWidth ());
    }
    g2.drawString (text, x, baseline);

  }

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont (Font font)
  {
    g2.setFont (font);
  }

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  public void setPaint (Paint paint)
  {
    g2.setPaint (paint);
  }

  /**
   * Draws a shape.
   * <P>
   * For lines, we handle a special case where (x1, y1) equals (x2, y2).  In this case, we want
   * to draw a line extending from one side of the page to the other.  This is achieved by
   * setting x1 = getUsableX() and setting x2 = x1+getUsableWidth().
   *
   * @param shape The shape.
   * @param x The x coordinate.
   * @param y The y coordinate.
   */
  public void drawShape (Shape shape, float x, float y)
  {

    AffineTransform saved = g2.getTransform ();
    g2.transform (AffineTransform.getTranslateInstance (x, y));
    g2.draw (shape);
    g2.setTransform (saved);

  }

  /**
   * This method is called when the page is ended.  Here we ignore this.
   */
  public void endPage ()
  {
  }

  public void drawImage (ImageReference image, float x, float y)
  {
    AffineTransform saved = g2.getTransform ();
    g2.transform (AffineTransform.getTranslateInstance (x, y));
    g2.drawImage (image.getImage (), (int) (image.getX ()), (int) (image.getY ()), (int) image.getWidth (), (int) image.getHeight (), null);
    g2.setTransform (saved);
  }

  /**
   * Draws the band onto the specified graphics device.
   * @param g2 The graphics device.
   * @param x The x-coordinate for the band.
   * @param y The y-coordinate for the band.
   */
  public void drawMultiLineText (String mytext, float x1, float y1, float x2, float y2, int align)
  {
    FontRenderContext frc = g2.getFontRenderContext ();
    FontMetrics fm = g2.getFontMetrics ();

    Vector lines = breakLines (mytext, frc, x2 - x1);
    int fontheight = fm.getHeight ();

    for (int i = 0; i < lines.size (); i++)
    {
      String line = (String) lines.elementAt (i);
      drawString (line, x1, y1 + i * fontheight, x2, y2, align);
    }

  }

  /**
   * Breaks the text into multiple lines
   */
  private Vector breakLines (String mytext, FontRenderContext frc, float w)
  {
    BreakIterator breakit = BreakIterator.getWordInstance ();
    breakit.setText (mytext);

    Font font = g2.getFont ();

    Vector lines = new Vector ();
    int pos = 0;
    int len = mytext.length ();

    while (pos < len)
    {
      int last = breakit.next ();
      float x = 0;

      while (x < w && last != BreakIterator.DONE)
      {
        Rectangle2D textBounds = font.getStringBounds (mytext, pos, last, frc);
        x = (float) textBounds.getWidth ();
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