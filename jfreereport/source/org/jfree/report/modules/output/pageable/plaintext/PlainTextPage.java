/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------
 * PlainTextPage.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PlainTextPage.java,v 1.12 2005/08/08 15:36:34 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 31-Jan-2004 : Removed leftBorder attribute as it is not used.
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;

import org.jfree.report.style.FontDefinition;

/**
 * The plain text page is used to buffer a complete page and to write the buffered data
 * when the page is closed.
 *
 * @author Thomas Morgner
 */
public class PlainTextPage
{

  /**
   * the page buffer is used to store all TextDataChunks.
   */
  private PlaintextDataChunk[][] pageBuffer;

  /**
   * The commandset that is used to finally print the content.
   */
  private PrinterDriver driver;

  /**
   * The width of the page in characters.
   */
  private int width;

  /**
   * the height of the page in lines.
   */
  private int height;

  private Paper paper;

  private String defaultEncoding;

  /**
   * Creates a new PlainTextPage with the given dimensions and the specified
   * PrinterCommandSet.
   *
   * @param driver the commandset for printing and formating the text.
   */
  public PlainTextPage (final PageFormat pageFormat,
                        final PrinterDriver driver,
                        final String defaultEncoding)
  {
    if (driver == null)
    {
      throw new NullPointerException("PrinterCommandSet must be defined.");
    }
    if (pageFormat == null)
    {
      throw new NullPointerException("PageFormat must be defined.");
    }
    if (defaultEncoding == null)
    {
      throw new NullPointerException("DefaultEncoding must be defined.");
    }

    final float characterWidthInPoint = (72f / driver.getCharactersPerInch());
    final float characterHeightInPoint = (72f / driver.getLinesPerInch());

    final int currentPageHeight = PlainTextOutputTarget.correctedDivisionFloor
            ((float) (pageFormat.getImageableHeight()), characterHeightInPoint);
    final int currentPageWidth = PlainTextOutputTarget.correctedDivisionFloor
            ((float) (pageFormat.getImageableWidth()), characterWidthInPoint);

    // Log.debug("Created page with " + currentPageWidth + ", " + currentPageHeight);
    pageBuffer = new PlaintextDataChunk[currentPageWidth][currentPageHeight];
    width = currentPageWidth;
    height = currentPageHeight;
    paper = pageFormat.getPaper();
    this.driver = driver;
    this.defaultEncoding = defaultEncoding;
  }

  /**
   * Returns the page width in characters.
   *
   * @return the page width.
   */
  public int getWidth ()
  {
    return width;
  }

  /**
   * Returns the page height in lines.
   *
   * @return the page height.
   */
  public int getHeight ()
  {
    return height;
  }

  /**
   * Adds a new text chunk to this PlainTextPage. A chunk consists of a single line of
   * text.
   *
   * @param x      the column of the first character of the text
   * @param y      the row where to print the text
   * @param w      the number of characters to print.
   * @param text   the text that should be printed.
   * @param format the fontdefinition used to format the text.
   */
  public void addTextChunk (final int x, final int y,
                            final int w, final String text,
                            final FontDefinition format)
  {
    if (text.length() == 0)
    {
      return;
    }
    if (x < 0)
    {
      throw new IllegalArgumentException("X < 0");
    }
    if (y < 0)
    {
      throw new IllegalArgumentException("y < 0");
    }
    if (w < 0)
    {
      throw new IllegalArgumentException("w < 0");
    }
    if (x + w > width)
    {
      throw new IllegalArgumentException("X+W [" + (x + w) + "] > bufferWidth [" + width + "]");
    }
    if (y >= height)
    {
      throw new IllegalArgumentException("Y > bufferHeight: " + text + " y=" + y + " h=" + height);
    }
    final PlaintextDataChunk chunk =
            new PlaintextDataChunk(text, format, defaultEncoding, x, y, w);
    for (int i = 0; i < w; i++)
    {
      if (pageBuffer[x + i][y] == null)
      {
        pageBuffer[x + i][y] = chunk;
      }
    }
  }

  /**
   * returns the chunk stored at the given position or null, if no chunk was stored
   * there.
   *
   * @param x the column
   * @param y the line
   * @return the text chunk or null.
   */
  private PlaintextDataChunk getChunk (final int x, final int y)
  {
    return pageBuffer[x][y];
  }

  /**
   * Writes the contents of the page using the printer command set.
   *
   * @throws java.io.IOException if an I/O error occured while writing the page.
   */
  public void writePage ()
          throws IOException
  {
    driver.startPage(paper, defaultEncoding);
    for (int y = 0; y < height; y++)
    {
      driver.startLine();
      int emptyChunkCount = 0;
      boolean overflow = false;

      for (int x = 0; x < width; x++)
      {
        final PlaintextDataChunk chunk = getChunk(x, y);
        if (chunk == null)
        {
          emptyChunkCount += 1;
        }
        else if (chunk.getX() == x)
        {
          if (emptyChunkCount != 0)
          {
            driver.printEmptyChunk(emptyChunkCount);
            emptyChunkCount = 0;
          }

          //Log.debug ("Print Chunk At " + x);
          driver.printChunk(chunk);
          x += (chunk.getWidth() - 1);
          // we reached the end of the line ...
          if (x == (width - 1))
          {
            overflow = true;
          }
        }
      }

      // end the page on the last line.
      if (y == (height - 1))
      {
        driver.endPage(overflow);
      }
      else
      {
        driver.endLine(overflow);
      }
    }
    driver.flush();
  }
}
