package gnu.bhresearch.pixie.wmf.bitmap;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.GDIColor;
import java.io.InputStream;
import java.io.IOException;
import java.awt.Color;

public class GDIPalette
{
  private int noColors = 0;
  private int[] colors = null;

  public void setNoOfColors (int colors)
  {
    this.noColors = colors;
  }
  
  public void setNoOfImportantColors (int colors)
  {
    if (colors > noColors)
      throw new IllegalArgumentException ("There may be not more important colors than colors defined in the palette.");
  }
  
  public void readPalette (InputStream in) 
    throws IOException
  {
    colors = new int[noColors];
    for (int i = 0; i < noColors; i++)
    {
      colors[i] = readNextColor (in);
    }
  }
  
  private int readNextColor (InputStream in)
    throws IOException
  {
    int b = in.read ();
    int g = in.read ();
    int r = in.read ();
    int filler = in.read ();
    return b + (g << 8) + (r << 16);
  }
  
  public int lookupColor (int color)
  {
    if (noColors == 0)
    {
      // Convert from BGR (windows) format to RGB (java) format
      int b = (color & 0x00ff0000) >> 16;
      int g = (color & 0x0000ff00);
      int r = (color & 0x000000ff);
      return b + g + (r << 16);
    }
      
    return colors[color];
  }
}