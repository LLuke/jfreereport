package gnu.bhresearch.pixie.wmf;

import java.awt.Color;

public class GDIColor extends Color
{
  private int flags;

  public static final int PC_RESERVED = 0x01;
  public static final int PC_EXPLICIT = 0x02;
  public static final int PC_NOCOLLAPSE = 0x04;

  public GDIColor (int colorref)
  {
    this (getR (colorref), getG (colorref), getB (colorref), getFlags (colorref));
  }

  public GDIColor (int r, int g, int b, int flags)
  {
    super (r, g, b);
    this.flags = flags;
  }

  private static final int getR (int ref)
  {
    int retval = (ref & 0x000000ff);
    if (retval < 0)
    {
      retval = (retval + 256);
    }
//    System.out.println ("red: " + retval);
    return retval;
  }

  private static final int getG (int ref)
  {
    int retval = (ref & 0x0000ff00) >> 8;
//    System.out.println ("Green: " + retval);
    return retval;
  }

  private static final int getB (int ref)
  {
    int retval = (ref & 0x00ff0000) >> 16;
//    System.out.println ("Blue: " + retval);
    return retval;
  }

  private static final int getFlags (int ref)
  {
    return (ref & 0xff000000) >> 24;
  }

  public boolean isReserved ()
  {
    return (this.flags & PC_RESERVED) == PC_RESERVED;
  }

  public boolean isExplicit ()
  {
    return (this.flags & PC_EXPLICIT) == PC_EXPLICIT;
  }

  public boolean isNoCollapse ()
  {
    return (this.flags & PC_NOCOLLAPSE) == PC_NOCOLLAPSE;
  }

  public int getFlags ()
  {
    return flags;
  }

  public String toString ()
  {
    return super.toString ();
  }

  public static int translateColor (Color c)
  {
    int red = c.getRed();
    int green = c.getGreen();
    int blue = c.getBlue();
    int flags = 0;

    if (c instanceof GDIColor)
    {
      GDIColor gc = (GDIColor) c;
      flags = gc.getFlags();
    }

    int retval = flags;
    retval = (retval << 8) + blue;
    retval = (retval << 8) + green;
    retval = (retval << 8) + red;
    return retval;
  }
}
