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
 * AbstractOutputTarget.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.BreakIterator;
import java.util.Vector;

/**
 * The abstract OutputTarget implements common code for all OutputTargets. It contains
 * functions to manage the cursor, the pageformat and the line breaking of strings.
 */
public abstract class AbstractOutputTarget implements OutputTarget
{
  /** A literal used for lines that get shortened by the linebreak implementation. */
  public static final String RESERVED_LITERAL = " ...";

  /**
   * The strict mode triggers an alternate linebreak behaviour. In this mode, a line which
   * cannot be displayed fully, is cut and gets the RESERVED_LITERAL (" ...") appended.
   *
   * This mode is currently false by default. Trigger it on and see example3 for the difference.
   */
  private boolean STRICT_MODE = false;

  /** The pageFormat for this target */
  private PageFormat pageformat;

  /** The cursor for the target. */
  private BandCursor cursor;

  /**
   * Creates the outputtarget and adds a default cursor to this band by calling createCursor().
   * Override createCursor to define a different cursor.
   *
   * @param format the pageformat used by this target.
   * @throws NullPointerException if the format is null
   */
  public AbstractOutputTarget (PageFormat format)
  {
    cursor = createCursor ();
    setPageFormat (format);
  }

  /**
   * Creates a new cursor for this band. The cursor is used to calculate the position of an
   * element within its band. This is basicly the same as AffineTransform.translate, but it
   * also works with the PDFOutputTarget.
   */
  protected BandCursor createCursor ()
  {
    return new BandCursor ();
  }

  /**
   * Returns the page format for the target.
   *
   * @return The page format.
   */
  public PageFormat getPageFormat ()
  {
    return pageformat;
  }

  /**
   * Sets the page format for the target.
   *
   * @param format The page format.
   */
  public void setPageFormat (PageFormat format)
  {
    if (format == null) throw new NullPointerException ();
    this.pageformat = format;
  }

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return The left edge of the page.
   */
  public float getPageX ()
  {
    return 0;
  }

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return The top edge of the page.
   */
  public float getPageY ()
  {
    return 0;
  }

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return The page width.
   */
  public float getPageWidth ()
  {
    return (float) (getPageFormat ().getWidth ());
  }

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return The page height.
   */
  public float getPageHeight ()
  {
    return (float) (getPageFormat ().getHeight ());
  }

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return The left edge of the printable area of the page.
   */
  public float getUsableX ()
  {
    return (float) (getPageFormat ().getImageableX ());
  }

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return The top edge of the printable area of the page.
   */
  public float getUsableY ()
  {
    return (float) (getPageFormat ().getImageableY ());
  }

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return The width of the printable area of the page.
   */
  public float getUsableWidth ()
  {
    return (float) (getPageFormat ().getImageableWidth ());
  }

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return The height of the printable area of the page.
   */
  public float getUsableHeight ()
  {
    return (float) (getPageFormat ().getImageableHeight ());
  }

  /**
   * Returns the current clipping area of this band. The clipping area is not needed to
   * be enforced, but elements should not violate the bounds on drawing or the results may
   * not look like expected.
   *
   * @return the bounds for the current band.
   */
  public Rectangle2D getClippingArea ()
  {
    return getCursor ().getBandBounds ();
  }

  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   */
  public void setClippingArea (Rectangle2D bounds)
  {
    getCursor ().setBandBounds (bounds);
  }

  /**
   * Returns the cursor for this outputtarget. The cursor is a readonly property. if you want
   * to define your own cursor, override createCursor() to return an suitable instance.
   */
  public BandCursor getCursor ()
  {
    return cursor;
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is being started,
   * others can simply ignore this message.
   */
  public void beginPage () throws OutputTargetException
  {
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   */
  public void endPage () throws OutputTargetException
  {
  }

  /**
   * Breaks the text into multiple lines. The given string is broken either when a manual
   * linebreak is encountered or when the current lines rendered width exceeds the given limit.
   * If the text contains more than <code>linecount</code> lines, all lines over the limit
   * are ignored.
   * <p>
   * If more than one line is expected or the strictmode is enabled and there are more lines
   * in the given text than space is available, the text is cut down to the given number of
   * lines and the RESERVED_LITERAL is appended to signal the cut down.
   * <p>
   * The text is broken on word boundries.
   *
   * @param mytext the text to be broken down
   * @param width the boundry on which the text is broken if no manual linebreak is encountered.
   * @param linecount the number of lines to be displayed. If linecount is lesser than 0, a 1 is
   * assumed.
   */
  protected Vector breakLines (String mytext, final float width, int linecount)
  {
    // Correct the linecount. We display at least a single line
    if (linecount < 1) linecount = 1;

    Vector bareLines = new Vector ();
    try
    {
      BufferedReader reader = new BufferedReader (new StringReader (mytext));
      String readLine = reader.readLine ();
      while (readLine != null)
      {
        bareLines.add (readLine);
        readLine = reader.readLine ();
      }
      reader.close ();
    }
    catch (IOException ioe)
    {
      // Will not happen
    }
    Log.debug ("The Line count: " + bareLines.size ());

    /**
     * Reserve some space for the last line if there is more than one line to display.
     * If there is only one line, don't cut the line yet. Perhaps we intruduce the strict
     * mode later, but without any visual editing it would be cruel to any report designer.
     */
    float reserved = 0;
    if (linecount > 1 || STRICT_MODE == false)
    {
      reserved = getStringBounds (RESERVED_LITERAL, 0, RESERVED_LITERAL.length ());
    }

    BreakIterator breakit = BreakIterator.getLineInstance ();
    Vector returnLines = new Vector ();

    int linesToDo = Math.min (linecount, bareLines.size ());

    for (int i = 0; i < linesToDo; i++)
    {
      String currentLine = (String) bareLines.elementAt (i);
      breakit.setText (currentLine);

      int lineStartPos = 0;
      int lineLength = currentLine.length ();
      while (lineStartPos < lineLength)
      {
        int startPos = lineStartPos;
        int endPos = breakit.next ();
        float x = 0;

        float w = width;
        if (i == (linecount - 1))
        {
          w -= reserved;
        }

        while (endPos != BreakIterator.DONE)
        {
//          x = (float) getStringBounds (currentLine, lineStartPos, endPos);
          x += (float) getStringBounds (currentLine, startPos, endPos);
          if (x >= w)
          {
            break;
          }

          startPos = endPos;
          endPos = breakit.next ();
        }

        if (endPos == BreakIterator.DONE || ((STRICT_MODE == false) && linecount == 1))
        {
          Log.debug ("Adding : " + lineStartPos + " to End of Line: " + currentLine.substring (lineStartPos));
          returnLines.add (currentLine.substring (lineStartPos));
          lineStartPos = lineLength;
        }
        else
        {

          if (i == (linecount - 1))
          {
            Log.debug ("Adding : " + lineStartPos + " to " + startPos + " : " + currentLine.substring (lineStartPos, startPos));
            returnLines.add (currentLine.substring (lineStartPos, startPos) + RESERVED_LITERAL);
            return returnLines;
          }
          else
          {
            Log.debug ("Adding : " + lineStartPos + " to " + startPos + " : " + currentLine.substring (lineStartPos, startPos));
            returnLines.add (currentLine.substring (lineStartPos, startPos));
            lineStartPos = startPos;
          }
        }
      }
    }
    return returnLines;
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   * @returns the width of the given string in 1/72" dpi.
   */
  protected abstract float getStringBounds (String text, int lineStartPos, int endPos);

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   */
  protected abstract float getFontHeight ();

  /**
   * Draws the band onto the specified graphics device.
   * @param mytext The text to be displayed.
   * @param align The alignment of a text line
   */
  public void drawMultiLineText (
          String mytext, int align)
  {
    if (false)
    {
      drawString (mytext, align);
      return;
    }
    Rectangle2D bounds = getCursor ().getDrawBounds ();
    float fontheight = getFontHeight ();
    int maxLinesToDisplay = (int) (bounds.getHeight () / fontheight);
    Vector lines = null;
    if (maxLinesToDisplay <= 1)
    {
      lines = new Vector ();
      lines.add (mytext);
    }
    else
    {
      lines = breakLines (mytext, (float) bounds.getWidth (), maxLinesToDisplay);
    }

    Rectangle2D lineBounds = new Rectangle2D.Float ();

    /**
     * If this is the only line to print, always draw. This is a simplification to ease the report
     * definition building process.
     */
    if (lines.size () == 1)
    {
      lineBounds.setRect ((float) bounds.getX (), bounds.getY (), bounds.getWidth (), fontheight);
      getCursor ().setDrawBounds (lineBounds);
      drawString ((String) lines.elementAt (0), align);
    }
    else
    {
      /**
       * There is more than one line. Calculate the positions and print line for line.
       */
      for (int linecount = 0; linecount < lines.size (); linecount++)
      {
        String line = (String) lines.elementAt (linecount);
        float linePos = (float) (linecount * fontheight + bounds.getY ());

        lineBounds.setRect ((float) bounds.getX (), linePos, bounds.getWidth (), fontheight);
        getCursor ().setDrawBounds (lineBounds);
        drawString (line, align);
      }
    }
  }

}
