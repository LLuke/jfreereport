package gnu.bhresearch.pixie.wmf.bitmap;

import java.io.IOException;
import java.io.InputStream;

public abstract class BitmapCompression
{
  private int height;
  private int width;
  private int bpp;
  private boolean topDown;

  public void setDimension (int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public int getHeight ()
  {
    return height;
  }

  public int getWidth ()
  {
    return width;
  }

  public int getBpp ()
  {
    return bpp;
  }

  public void setBpp (int bpp)
  {
    this.bpp = bpp;
  }

  public void setTopDown (boolean b)
  {
    this.topDown = b;
  }

  public boolean isTopDown ()
  {
    return topDown;
  }

  public abstract int[] decompress (InputStream in, GDIPalette palette)
          throws IOException;

  public static int[] expandMonocrome (int b, GDIPalette pal)
  {
    int tColor = pal.lookupColor (1);
    int fColor = pal.lookupColor (0);

    int[] retval = new int[8];
    if ((b & 0x01) == 0x01) retval[0] = tColor; else retval[0] = fColor;
    if ((b & 0x02) == 0x02) retval[1] = tColor; else retval[1] = fColor;
    if ((b & 0x04) == 0x04) retval[2] = tColor; else retval[2] = fColor;
    if ((b & 0x08) == 0x08) retval[3] = tColor; else retval[3] = fColor;
    if ((b & 0x10) == 0x10) retval[4] = tColor; else retval[4] = fColor;
    if ((b & 0x20) == 0x20) retval[5] = tColor; else retval[5] = fColor;
    if ((b & 0x40) == 0x40) retval[6] = tColor; else retval[6] = fColor;
    if ((b & 0x80) == 0x80) retval[7] = tColor; else retval[7] = fColor;
    return retval;
  }

  public static int[] expand4BitTuple (int b, GDIPalette pal)
  {
    int[] retval = new int[2];
    retval[0] = pal.lookupColor ((b & 0xF0) >> 4);
    retval[1] = pal.lookupColor (b & 0x0F);
    return retval;
  }
}