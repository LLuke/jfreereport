/**
 * Date: Jan 29, 2003
 * Time: 2:30:07 PM
 *
 * $Id: PlainTextPage.java,v 1.1 2003/01/29 18:37:12 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PlainTextPage
{
  public static final byte CARRIAGE_RETURN = 0x0D;
  public static final byte LINE_FEED = 0x0A;
  public static final byte FORM_FEED = 0x0C;
  public static final byte SPACE = 0x20;

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
  private int width;
  private int height;

  public PlainTextPage(int w, int h)
  {
    if (w <= 0) throw new IllegalArgumentException("W <= 0");
    if (h <= 0) throw new IllegalArgumentException("W <= 0");
    pageBuffer = new TextDataChunk[w][h];
    width = w;
    height = h;
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
      else
      {
        Log.debug ("Character already filled!");
      }
    }
  }

  protected TextDataChunk getChunk (int x, int y)
  {
    return pageBuffer[x][y];
  }

  protected void startLine (OutputStream out)
    throws IOException
  {
  }

  protected void endLine (OutputStream out)
    throws IOException
  {
    // CR = (ASCII #13) reset the print position to the start of the line
    // LF = (ASCII #10) scroll down a new line (? Auto-LF feature ?)
    out.write(CARRIAGE_RETURN);
    out.write(LINE_FEED);
  }

  protected void printEmptyChunk (OutputStream out)
      throws IOException
  {
    out.write (SPACE);
  }

  protected void startPage (OutputStream out)
      throws IOException
  {
  }

  protected void endPage (OutputStream out)
      throws IOException
  {
    out.write(FORM_FEED);
  }

  protected void printChunk (OutputStream out, TextDataChunk chunk, int x)
    throws IOException
  {
    if (chunk.getX() == x)
    {
      byte[] text = chunk.getText().getBytes();

      byte[] data = new byte[chunk.getWidth()];
      Arrays.fill(data, SPACE);
      System.arraycopy(text, 0, data, 0, Math.min (text.length, data.length));
      out.write (data);
    }
  }

  public void writePage (OutputStream out)
    throws IOException
  {
    startPage(out);
    for (int y = 0; y < height; y++)
    {
      startLine(out);
      for (int x = 0; x < width; x++)
      {
        TextDataChunk chunk = getChunk(x,y);
        if (chunk == null)
        {
          printEmptyChunk(out);
        }
        else
        {
          printChunk(out, chunk, x);
        }
      }
      endLine(out);
    }
    endPage(out);
    out.flush();
  }
}
