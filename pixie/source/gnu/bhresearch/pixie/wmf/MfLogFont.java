// MfLogFont.java - A Windows metafile logical font object.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import java.awt.Font;
import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;
import java.awt.geom.AffineTransform;

/**
	A Windows metafile logical font object.
*/
public class MfLogFont implements WmfObject
{
  private String face;
  private int size;
  private int style;
  private boolean strikeout;
  private boolean underline;
  private double rotation;
  
  /** Construct from a metafile record. */
  public MfLogFont()
  {
  }

  public void setFace (String face)
  {
    this.face = face;
  }
  
  /** The name of the font face. */
  public String getFace()
  {
    return face;
  }

  public void setSize (int size)
  {
    this.size = size;
  }


  /** The size, in logical units. */
  public int getSize()
  {
    return size;
  }

  /** The font style. */
  public int getStyle()
  {
    return style;
  }

  public void setStyle (int style)
  {
    this.style = style;
  }

  /** True if this is an underlined font. */
  public boolean isUnderline()
  {
    return underline;
  }
  
  public void setUnderline (boolean underline)
  {
    this.underline = underline;
  }
  
  public boolean isStrikeOut ()
  {
    return strikeout;
  }
  
  public void setStrikeOut (boolean b)
  {
    this.strikeout = b;
  }
  
  public Font createFont ()
  {
    Font retfont = new Font (getFace(), getStyle(), getSize());
    double rot = Math.sin (Math.toRadians(-rotation));
    return retfont.deriveFont (AffineTransform.getRotateInstance(rot));
  }

  public int getType ()
  {
    return OBJ_FONT;
  }
  
  public double getRotation ()
  {
    return rotation;
  }
  
  public void setRotation (double d)
  {
    this.rotation = d;
  }
}
