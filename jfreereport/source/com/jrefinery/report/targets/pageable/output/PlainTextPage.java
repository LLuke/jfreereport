/**
 * Date: Jan 29, 2003
 * Time: 2:30:07 PM
 *
 * $Id: PlainTextPage.java,v 1.3 2003/01/30 00:04:53 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.targets.FontDefinition;

import java.io.IOException;

public class PlainTextPage
{

  protected class TextDataChunk
  {
    private String text;
    private FontDefinition font;
    private int x;
    private int y;
    private int width;

    public TextDataChunk(String text, FontDefinition font, int x, int y, int w)
    {
      this.text = text;
      this.font = font;
      this.x = x;
      this.y = y;
      this.width = w;
    }

    public String getText()
    {
      return text;
    }

    public FontDefinition getFont()
    {
      return font;
    }

    public int getX()
    {
      return x;
    }

    public int getY()
    {
      return y;
    }

    public int getWidth()
    {
      return width;
    }
  }

  private TextDataChunk[][] pageBuffer;
  private PrinterCommandSet commandSet;
  private int width;
  private int height;

  public PlainTextPage(int w, int h, PrinterCommandSet commandSet)
  {
    if (w <= 0) throw new IllegalArgumentException("W <= 0");
    if (h <= 0) throw new IllegalArgumentException("W <= 0");
    pageBuffer = new TextDataChunk[w][h];
    width = w;
    height = h;
    this.commandSet = commandSet;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public void addTextChunk (int x, int y, int w, String text, FontDefinition format)
  {
    //Log.debug ("Add TextChunk: (" + x + ", " + y + ") -> " + w);
    //Log.debug ("    Backend  : (" + width + ", " + height + ")");
    if (x < 0) throw new IllegalArgumentException("X < 0");
    if (y < 0) throw new IllegalArgumentException("y < 0");
    if (w < 0) throw new IllegalArgumentException("w < 0");
    if (x+w > width) throw new IllegalArgumentException("X+W > bufferWidth");
    if (y > height) throw new IllegalArgumentException("Y > bufferHeight");

    TextDataChunk chunk = new TextDataChunk(text, format, x, y, w);
    for (int i = 0; i < w; i++)
    {
      if (pageBuffer[x+i][y] == null)
      {
        pageBuffer[x+i][y] = chunk;
      }
      /*
      else
      {
        Log.debug ("Character already filled!");
      }
      */
    }
  }

  protected TextDataChunk getChunk (int x, int y)
  {
    return pageBuffer[x][y];
  }

  public void writePage ()
    throws IOException
  {
    commandSet.resetPrinter();
    commandSet.startPage();
    for (int y = 0; y < height; y++)
    {
      commandSet.startLine();
      for (int x = 0; x < width; x++)
      {
        TextDataChunk chunk = getChunk(x,y);
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
