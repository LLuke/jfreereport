// MfLogBrush.java - A Windows metafile logical brush object
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;

/**
	A Windows metafile logical brush object.
*/
public class MfLogBrush implements WmfObject
{
  private static final boolean WHITE = false;
  private static final boolean BLACK = true;

  private static final boolean [] IMG_HS_HORIZONTAL = 
  {
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, 
    BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, 
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
  };
  
  private static final boolean [] IMG_HS_VERTICAL = 
  {
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
  };
  
  private static final boolean [] IMG_HS_FDIAGONAL = 
  {
    BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, WHITE,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK,
    BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK,
  };
  
  private static final boolean [] IMG_HS_BDIAGONAL = 
  {
    BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK,
    WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK,
    WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, WHITE,
    WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE, WHITE,
    WHITE, BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, WHITE,
    BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
  };
  
  private static final boolean [] IMG_HS_CROSS = 
  {
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, 
    BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, 
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
  };
  
  private static final boolean [] IMG_HS_DIAGCROSS = 
  {
    BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK,
    WHITE, BLACK, BLACK, WHITE, WHITE, BLACK, BLACK, WHITE,
    WHITE, WHITE, BLACK, BLACK, BLACK, BLACK, WHITE, WHITE,
    WHITE, WHITE, WHITE, BLACK, BLACK, WHITE, WHITE, WHITE,
    WHITE, WHITE, BLACK, BLACK, BLACK, BLACK, WHITE, WHITE,
    WHITE, BLACK, BLACK, WHITE, WHITE, BLACK, BLACK, WHITE,
    BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, BLACK, BLACK,
    BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, BLACK,
  };

  public static final int COLOR_FULL_ALPHA = 0x00FFFFFF;

  // Brush styles.
  public static final int BS_SOLID = 0;
  public static final int BS_NULL = 1;
  public static final int BS_HATCHED = 2;
  public static final int BS_PATTERN = 3;
  public static final int BS_INDEXED = 4;
  public static final int BS_DIBPATTERN = 5;

  // Hatch styles.
  public static final int HS_HORIZONTAL = 0;
  public static final int HS_VERTICAL = 1;
  public static final int HS_FDIAGONAL = 2;
  public static final int HS_BDIAGONAL = 3;
  public static final int HS_CROSS = 4;
  public static final int HS_DIAGCROSS = 5;

  private int style;
  private Color color;
  private Color bgColor;
  private int hatch;
  private Paint lastPaint;
  private BufferedImage bitmap;

  /** The default brush for a new DC. */
  public MfLogBrush()
  {
    style = BS_SOLID;
    color = Color.white;
    bgColor = new Color (COLOR_FULL_ALPHA);
    hatch = HS_HORIZONTAL;
    
  }

  public boolean isVisible ()
  {
    return getStyle() != BS_NULL;
  }
  
  public int getType ()
  {
    return OBJ_BRUSH;
  }

  /** The style of this brush. */
  public int getStyle()
  {
    return style;
  }

  public void setStyle (int style)
  {
    this.style = style;
  }
  
  /** Return the color of the current brush, or null. */
  public Color getColor()
  {
    return color;
  }

  public void setColor (Color color)
  {
    this.color = color;
    lastPaint = null;
  }

  /** The hatch style of this brush. */
  public int getHatchedStyle()
  {
    return hatch;
  }

  public void setHatchedStyle (int hstyle)
  {
    this.hatch = hstyle;
    lastPaint = null;
  }
  
  public Paint getPaint ()
  {
    if (lastPaint != null)
      return lastPaint;
    
    switch (getStyle ())
    {
      case BS_SOLID: lastPaint = getColor (); break;
      case BS_NULL:  lastPaint = new GDIColor (COLOR_FULL_ALPHA);
      case BS_HATCHED: 
      {
        BufferedImage image = createHatchStyle ();
        lastPaint = new TexturePaint (image, new Rectangle (0,0,image.getWidth(), image.getHeight())); 
        break;
      }
      case BS_DIBPATTERN:
      {
        if (bitmap == null)
        {
          lastPaint = new GDIColor (COLOR_FULL_ALPHA);
        }
        else
        {
          lastPaint = new TexturePaint (bitmap, new Rectangle (0,0,bitmap.getWidth(), bitmap.getHeight())); 
        }
        break;
      }
      default:
      {
    
        System.out.println ("Unknown Paint Mode:" + getStyle());
        lastPaint = new GDIColor (COLOR_FULL_ALPHA);
      }
    }
    return lastPaint;
  }
  
  private BufferedImage createHatchStyle ()
  {
    int style = getHatchedStyle();

    BufferedImage image = new BufferedImage (8,8,BufferedImage.TYPE_INT_ARGB);
    switch (style)
    {
      case HS_HORIZONTAL: 
        image.setRGB (0,0,8,8,transform (IMG_HS_HORIZONTAL), 0,8); 
        break;
      case HS_VERTICAL:   
        image.setRGB (0,0,8,8,transform (IMG_HS_VERTICAL), 0,8);   
        break;
      case HS_FDIAGONAL:  
        image.setRGB (0,0,8,8,transform (IMG_HS_FDIAGONAL), 0,8);  
        break;
      case HS_BDIAGONAL:  
        image.setRGB (0,0,8,8,transform (IMG_HS_BDIAGONAL), 0,8);  
        break;
      case HS_CROSS:      
        image.setRGB (0,0,8,8,transform (IMG_HS_CROSS), 0,8);      
        break;
      case HS_DIAGCROSS:  
        image.setRGB (0,0,8,8,transform (IMG_HS_DIAGCROSS), 0,8);  
        break;
      default: throw new IllegalArgumentException ();
    }
    return image;
  }
  
  public int[] transform (boolean[] data)
  {
    int color = getColor().getRGB ();
    int bgColor = getBackgroundColor().getRGB();

    int [] retval = new int[data.length];
    for (int i = 0; i < retval.length; i++)
    {
      if (data[i] == true)
      {
        retval[i] = color;
      }
      else
      {
        retval[i] = bgColor;
      }
    }
    return retval;
  }
  
  public void setBackgroundColor (Color bg)
  {
    this.bgColor = bg;
    lastPaint = null;
  }
  
  public Color getBackgroundColor ()
  {
    return bgColor;
  }
  
  public void setBitmap (BufferedImage bitmap)
  {
    this.bitmap = bitmap;
  }
}
