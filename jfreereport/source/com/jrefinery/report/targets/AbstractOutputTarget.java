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
 * -------------------------
 * AbstractOutputTarget.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: AbstractOutputTarget.java,v 1.24 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 * 21-May-2002 : Initial version
 * 22-May-2002 : TextAlignment fixed
 * 23-May-2002 : Replaced System.out logging with Log-class
 * 30-May-2002 : Performance upgrade: One-line texts are not processed by linebreak-method
 * 08-Jun-2002 : Documentation
 * 17-Jul-2002 : Added NullPointer handling for drawText(). Whitespaces are now replaced by
 *               space (0x20) if the text to be printed fits on a single line
 * 20-Jul-2002 : created this changelog
 * 23-Aug-2002 : breakLines was broken, fixed and removed useless code ..
 * 23-Aug-2002 : removed the strictmode, the reserved literal is now always added
 * 26-Aug-2002 : Corrected Fontheight calculations.
 * 02-Oct-2002 : Bug: breakLines() got a corrected word breaking (Aleksandr Gekht)
 * 06-Nov-2002 : Bug: LineBreaking again: Handled multiple linebreaks and empty lines
 * 13-Nov-2002 : Deprecated getFontHeight() and replaced it with getLineHeight(), in the
 *               process of aligning text positioning in print preview and PDF (DG)
 *
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.TextElement;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * The abstract OutputTarget implements common code for all OutputTargets. It contains
 * functions to manage the cursor, the pageformat and the line breaking of strings.
 *
 * @author TM
 */
public abstract class AbstractOutputTarget implements OutputTarget
{
  /** A literal used for lines that get shortened by the linebreak implementation. */
  public static final String RESERVED_LITERAL = " ...";

  /** The pageFormat for this target */
  private PageFormat pageformat;

  /** The cursor for the target. */
  private BandCursor cursor;

  /** Storage for the output target properties. */
  private Hashtable properties;

  /**
   * Creates the outputtarget and adds a default cursor to this band by calling createCursor().
   * Override createCursor to define a different cursor.
   *
   * @param format the pageformat used by this target.
   * @throws NullPointerException if the format is null
   */
  public AbstractOutputTarget(PageFormat format)
  {
    properties = new Hashtable();
    cursor = createCursor();
    setPageFormat(format);
  }

  /**
   * Defines a property for this outputtarget. Properties are the standard way of configuring
   * an outputtarget.
   *
   * @param property the name of the property to set
   * @param value the value of the property. If value is null, the property is removed from the
   * OutputTarget
   *
   * @throws NullPointerException if property is null
   */
  public void setProperty(String property, Object value)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      properties.remove(property);
    }
    else
    {
      properties.put(property, value);
    }
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, <code>
   * null</code> is returned.
   *
   * @param property the name of the property to be queried
   *
   * @return the value stored under the given property name
   *
   * @throws NullPointerException if <code>property</code> is null
   */
  public Object getProperty(String property)
  {
    return getProperty(property, null);
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, the
   * default value is returned.
   *
   * @return the value stored under the given property name
   * @param property the name of the property to be queried
   * @param defaultValue the defaultvalue returned if there is no such property
   * @throws NullPointerException if <code>property</code> is null
   */
  public Object getProperty(String property, Object defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    Object retval = properties.get(property);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  /**
   * Creates a new cursor for this band. The cursor is used to calculate the position of an
   * element within its band. This is basicly the same as AffineTransform.translate, but it
   * also works with the PDFOutputTarget.
   *
   * @return the band cursor.
   */
  protected BandCursor createCursor()
  {
    return new BandCursor();
  }

  /**
   * Returns the page format for the target.
   *
   * @return The page format.
   */
  public PageFormat getPageFormat()
  {
    return pageformat;
  }

  /**
   * Sets the page format for the target.
   *
   * @param format The page format.
   */
  public void setPageFormat(PageFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException();
    }
    this.pageformat = format;
  }

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return The left edge of the page.
   */
  public float getPageX()
  {
    return 0;
  }

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return The top edge of the page.
   */
  public float getPageY()
  {
    return 0;
  }

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return The page width.
   */
  public float getPageWidth()
  {
    return (float) (getPageFormat().getWidth());
  }

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return The page height.
   */
  public float getPageHeight()
  {
    return (float) (getPageFormat().getHeight());
  }

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return The left edge of the printable area of the page.
   */
  public float getUsableX()
  {
    return (float) (getPageFormat().getImageableX());
  }

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return The top edge of the printable area of the page.
   */
  public float getUsableY()
  {
    return (float) (getPageFormat().getImageableY());
  }

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return The width of the printable area of the page.
   */
  public float getUsableWidth()
  {
    return (float) (getPageFormat().getImageableWidth());
  }

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return The height of the printable area of the page.
   */
  public float getUsableHeight()
  {
    return (float) (getPageFormat().getImageableHeight());
  }

  /**
   * Returns the current clipping area of this band. The clipping area is not needed to
   * be enforced, but elements should not violate the bounds on drawing or the results may
   * not look like expected.
   *
   * @return the bounds for the current band.
   */
  public Rectangle2D getClippingArea()
  {
    return getCursor().getBandBounds();
  }

  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   *
   * @param bounds  the clip bounds.
   */
  public void setClippingArea(Rectangle2D bounds)
  {
    getCursor().setBandBounds(bounds);
  }

  /**
   * Returns the cursor for this output target.
   * <p>
   * The cursor is a readonly property. if you want to define your own cursor, override
   * createCursor() to return an suitable instance.
   *
   * @return the cursor.
   */
  public BandCursor getCursor()
  {
    return cursor;
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is being
   * started, others can simply ignore this message.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void beginPage() throws OutputTargetException
  {
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void endPage() throws OutputTargetException
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
   * The text is broken on word boundaries.
   *
   * @param mytext  the text to be broken down
   * @param width  the boundary on which the text is broken if no manual linebreak is encountered.
   * @param maxLines  the number of lines to be displayed. If linecount is less than 1, an
   *                 unlimited textelement is assumed and the whole string is processed without
   *                 obeying to any limit.
   *
   * @return a list of lines.
   */
  protected List breakLines(String mytext, final float width, int maxLines)
  {
    /**
     * Reserve some space for the last line if there is more than one line to display.
     * If there is only one line, don't cut the line yet. Perhaps we intruduce the strict
     * mode later, but without any visual editing it would be cruel to any report designer.
     */
    float reserved = getStringBounds(RESERVED_LITERAL, 0, RESERVED_LITERAL.length());

    BreakIterator breakit = BreakIterator.getLineInstance();
    ArrayList returnLines = new ArrayList();
    List lines = new ArrayList();
    try
    {
      BufferedReader reader = new BufferedReader(new StringReader(mytext));
      String readLine = null;
      while ((readLine = reader.readLine()) != null)
      {
        lines.add(readLine);
      }
      reader.close();
    }
    catch (IOException ioe)
    {
      Log.info("This will not happen.", ioe);
    }


    for (int i = 0; i < lines.size(); i++)
    {
      String currentLine = (String) lines.get(i);
      breakit.setText(currentLine);

      int lineStartPos = 0;
      int lineLength = currentLine.length();
      if (lineLength == 0)
      {
        returnLines.add("");
        continue;
      }

      while (lineStartPos < lineLength)
      {
        int startPos = lineStartPos;
        int endPos = 0;
        float x = 0;

        float w = width;

        // add by leonlyong
        int wordCnt = 0;

        // check the complete line, break when the complete text is done or
        // the end of the line has been reached.
        while (((endPos = breakit.next()) != BreakIterator.DONE))
        {
          // add by leonlyong
          wordCnt++;

          x += (float) getStringBounds(currentLine, startPos, endPos);
          if ((maxLines != 0) && (returnLines.size() == (maxLines - 1)))
          {
            // here is the last line, check whether the reserved literal will fit in here.
            // fixme: check whether the string can be finished wo. appending the literal.
            if (x >= (w - reserved))
            {
              break;
            }
          }
          else
          {
            if (x >= w)
            {
              // add by leonlyong
              //when the first word of the line is too big
              if (wordCnt == 1)
              {
                while ((float) getStringBounds(currentLine, startPos, endPos) >= w)
                {
                  endPos--;
                }
                startPos = endPos;
                endPos = breakit.previous();
              }

              break;
            }
          }
          startPos = endPos;
        }

        // the complete text is finished, noting more to do here.
        if (endPos == BreakIterator.DONE)
        {
          String addString = currentLine.substring(lineStartPos);
          returnLines.add(addString);
          break;
        }

        // if this is the last allowed row, add the RESERVED_LITERAL to the string ..
        if ((maxLines != 0) && (returnLines.size() == (maxLines - 1)))
        {
          // altered:
          returnLines.add(appendReserveLit(currentLine, lineStartPos, startPos, width));
          return returnLines;
        }
        else
        {

          /**
           * Handle additional linebreaks within the string. A single linebreak is caught by the
           * BreakIterator, but if several LB's follow each other, the BreakIterator handles them
           * as a single linebreak.
           */
          String addString = currentLine.substring(lineStartPos, startPos);
          returnLines.add(addString);
          lineStartPos = startPos;
        }
      }
    }
    return returnLines;
  }

  private String appendReserveLit(String base, int lineStart, int start, float width)
  {
    if (start < 0) throw new IllegalArgumentException("Start must not be negative");
    if (width < 0) throw new IllegalArgumentException("Width must not be negative");
    if (lineStart < 0) throw new IllegalArgumentException("LineStart must not be negative");

    float reserved = getStringBounds(RESERVED_LITERAL, 0, RESERVED_LITERAL.length());
    String baseLine = base.substring(lineStart, start);
    float filler = width - (getStringBounds(baseLine, 0, baseLine.length())) - reserved;

    int maxFillerLength = base.length() - start;
    for (int i = 0; i < maxFillerLength; i++)
    {
      String fillString = base.substring(start, i + start);
      float fillerWidth = getStringBounds(fillString, 0, i);
      if (filler < fillerWidth)
      {
        return baseLine + fillString + RESERVED_LITERAL;
      }
    }
    return baseLine + RESERVED_LITERAL;
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   *
   * @return the width of the given string in 1/72" dpi.
   */
  protected abstract float getStringBounds(String text, int lineStartPos, int endPos);

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   *
   * @return the font height.
   *
   * @deprecated replaced with getLineHeight();
   */
  protected abstract float getFontHeight();

  /**
   * Returns the height of a line, using the current font.  This includes the ascent, descent
   * and leading.
   *
   * @return the line height.
   */
  protected abstract float getLineHeight();

  /**
   * Draws a string inside a rectangular area define by the cursor.
   * <P>
   * The text is split at the end of the line and continued in the next line.
   *
   * @param text  the text.
   * @param alignment  the horizontal alignment.
   */
  public void drawMultiLineText(String text, int alignment)
  {
    drawMultiLineText(text, alignment, false);
  }

  /**
   * Draws the band onto the specified graphics device.
   *
   * @param text  the text to be displayed.
   * @param alignment  the alignment.
   * @param dynamic  if true, the band size will be increased (if required) to accommodate the
   *                 text.
   */
  public void drawMultiLineText(String text, int alignment, boolean dynamic)
  {
    drawMultiLineText(text, alignment, TextElement.TOP, dynamic);
  }

  /**
   * Draws the band onto the specified graphics device.
   *
   * @param text  the text to be displayed.
   * @param horizontalAlignment  the horizontal alignment.
   * @param verticalAlignment  the vertical alignment.
   * @param dynamic  if true, the band size will be increased (if required) to accommodate the
   *                 text.
   */
  public void drawMultiLineText(String text,
                                int horizontalAlignment, int verticalAlignment,
                                boolean dynamic)
  {
    // do nothing if there is nothing to print
    if (text == null)
    {
      return;
    }

    Rectangle2D bounds = getCursor().getDrawBounds();
    float lineHeight = getLineHeight();

    List lines = null;
    if (dynamic == true)
    {
      // don't define a limit for the number of lines, the size of the band is adjusted.
      lines = breakLines(text, (float) bounds.getWidth(), 0);
    }
    else
    {
      int maxLinesToDisplay = (int) (bounds.getHeight() / lineHeight);
      lines = breakLines(text, (float) bounds.getWidth(), maxLinesToDisplay);
    }

    float newheight = lines.size() * lineHeight;
    float oldheight = (float) bounds.getHeight();
    if (newheight > oldheight)
    {
      bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), newheight);
      getCursor().setDrawBounds(bounds);
    }

    Rectangle2D lineBounds = new Rectangle2D.Float();

    // if this is the only line to print, always draw. This is a simplification to ease the report
    // definition building process
    if (lines.size() == 1)
    {
      drawString((String) lines.get(0), horizontalAlignment, verticalAlignment);
    }
    else
    {
      // there is more than one line. Calculate the positions and print line for line.
      // to-do: make this code respect vertical alignment...
      for (int linecount = 0; linecount < lines.size(); linecount++)
      {
        String line = (String) lines.get(linecount);
        float linePos = (float) (linecount * lineHeight + bounds.getY());

        lineBounds.setRect((float) bounds.getX(), linePos, bounds.getWidth(), lineHeight);
        getCursor().setDrawBounds(lineBounds);
        drawString(line, horizontalAlignment);
      }
    }
  }

  /**
   * Removes all whitespaces from the given String and replaces them with a space character.
   *
   * @param text  the string to process.
   *
   * @return  a string with all whitespace replaced by space characters.
   */
  protected String clearWhitespaces(String text)
  {
    char[] textdata = text.toCharArray();
    for (int i = 0; i < textdata.length; i++)
    {
      if (Character.isWhitespace(textdata[i]))
      {
        textdata[i] = ' ';
      }
    }
    return new String(textdata);
  }

  protected Enumeration getPropertyNames()
  {
    return properties.keys();
  }

}
