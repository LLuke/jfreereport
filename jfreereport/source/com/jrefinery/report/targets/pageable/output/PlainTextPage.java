/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * PlainTextPage.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextPage.java,v 1.11 2003/02/27 10:35:40 mungady Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.pageable.output;

import java.io.IOException;

import com.jrefinery.report.targets.FontDefinition;

/**
 * The plain text page is used to buffer a complete page and to write the
 * buffered data when the page is closed.
 *
 * @author Thomas Morgner
 */
public class PlainTextPage
{
  /**
   * A data carrier to collect and store text data for the output.
   */
  protected class TextDataChunk
  {
    /** The text that should be printed. */
    private String text;

    /** The font definition stores the font style. */
    private FontDefinition font;

    /** the column where the text starts. */
    private int x;

    /** the row of the text. */
    private int y;

    /** the text width. */
    private int width;

    /**
     * Creates a new text data chunk.
     *
     * @param text the text that should be printed
     * @param font the font style for the text
     * @param x the column where the text starts
     * @param y the row of the text
     * @param w the number of characters of the text that should be printed.
     */
    public TextDataChunk(String text, FontDefinition font, int x, int y, int w)
    {
      this.text = text;
      this.font = font;
      this.x = x;
      this.y = y;
      this.width = w;
    }

    /**
     * Gets the text stored in this chunk.
     *
     * @return the text
     */
    public String getText()
    {
      return text;
    }

    /**
     * Gets the font definition used to define the text style.
     *
     * @return the font definition.
     */
    public FontDefinition getFont()
    {
      return font;
    }

    /**
     * The column of the text start.
     *
     * @return the column of the first character.
     */
    public int getX()
    {
      return x;
    }

    /**
     * Gets the row where to print the text.
     *
     * @return the row.
     */
    public int getY()
    {
      return y;
    }

    /**
     * Gets the width of the text, the number of character which should be printed.
     *
     * @return the number of printable characters.
     */
    public int getWidth()
    {
      return width;
    }
  }

  /** the page buffer is used to store all TextDataChunks. */
  private TextDataChunk[][] pageBuffer;

  /** The commandset that is used to finally print the content. */
  private PrinterCommandSet commandSet;

  /** The width of the page in characters. */
  private int width;

  /** the height of the page in lines. */
  private int height;

  /**
   * Creates a new PlainTextPage with the given dimensions and the specified
   * PrinterCommandSet.
   *
   * @param w the number of columns on the page
   * @param h the number of rows on the page
   * @param commandSet the commandset for printing and formating the text.
   */
  public PlainTextPage(int w, int h, PrinterCommandSet commandSet)
  {
    if (w <= 0)
    {
      throw new IllegalArgumentException("W <= 0");
    }
    if (h <= 0)
    {
      throw new IllegalArgumentException("W <= 0");
    }
    pageBuffer = new TextDataChunk[w][h];
    width = w;
    height = h;
    this.commandSet = commandSet;
  }

  /**
   * Returns the page width in characters.
   * @return the page width.
   */
  public int getWidth()
  {
    return width;
  }

  /**
   * Returns the page height in lines.
   * @return the page height.
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * Adds a new text chunk to this PlainTextPage. A chunk consists of
   * a single line of text.
   *
   * @param x the column of the first character of the text
   * @param y the row where to print the text
   * @param w the number of characters to print.
   * @param text the text that should be printed.
   * @param format the fontdefinition used to format the text.
   */
  public void addTextChunk(int x, int y, int w, String text, FontDefinition format)
  {
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
      throw new IllegalArgumentException("X+W > bufferWidth");
    }
    if (y > height)
    {
      throw new IllegalArgumentException("Y > bufferHeight: " + text + " y=" + y + " h=" + height);
    }

    TextDataChunk chunk = new TextDataChunk(text, format, x, y, w);
    for (int i = 0; i < w; i++)
    {
      if (pageBuffer[x + i][y] == null)
      {
        pageBuffer[x + i][y] = chunk;
      }
    }
  }

  /**
   * returns the chunk stored at the given position or null, if no chunk was
   * stored there.
   *
   * @param x the column
   * @param y the line
   * @return the text chunk or null.
   */
  protected TextDataChunk getChunk(int x, int y)
  {
    return pageBuffer[x][y];
  }

  /**
   * Writes the contents of the page using the printer command set.
   *
   * @throws IOException if an I/O error occured while writing the page.
   */
  public void writePage()
      throws IOException
  {
    commandSet.resetPrinter();
    commandSet.startPage();
    for (int y = 0; y < height; y++)
    {
      commandSet.startLine();
      for (int x = 0; x < width; x++)
      {
        TextDataChunk chunk = getChunk(x, y);
        if (chunk == null)
        {
          commandSet.printEmptyChunk();
        }
        else
        {
          commandSet.printChunk(chunk, x);
        }
      }
      commandSet.endLine();
    }
    commandSet.endPage();
    commandSet.flush();
  }
}
