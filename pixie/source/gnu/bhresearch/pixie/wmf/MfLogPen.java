// MfLogPen.java - A Windows metafile logical pen object.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import java.awt.Color;
import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;
import java.awt.Stroke;
import java.awt.BasicStroke;

/**
	A Windows metafile logical pen object.
*/
public class MfLogPen implements WmfObject, PenConstants
{

  private int style;
  private int endCap;
  private int joinType;
  private int width;
  private Color color;


  /** The default pen for a new DC. */
  public MfLogPen()
  {
    style = PS_SOLID;
    width = 0;
    color = Color.black;
  }

  /** Return one of the PS_ styles. */
  public int getStyle()
  {
    return style;
  }

  public void setStyle (int style)
  {
    this.style  = style & 0x000000FF;
    this.endCap = style & 0x00000F00;
    this.joinType = style & 0x0000F000;
  }
  
  /** Return width. */
  public int getWidth()
  {
    return width;
  }
  
  public void setWidth (int width)
  {
    this.width = width;
  }

  /** Return color of the current pen, or null. */
  public Color getColor()
  {
    return color;
  }

  public void setColor (Color color)
  {
    this.color = color;
  }

  /** True if  not a dashed or dotted style. */
  public boolean isSimpleStyle()
  {
    switch (style)
    {
      case PS_SOLID:
      case PS_NULL:
      case PS_INSIDEFRAME:
        return true;
      default:
        return false;
    }
  }

  public boolean isVisible ()
  {
    return getStyle() != PS_NULL;
  }
  
  public int getType ()
  {
    return OBJ_PEN;
  }

  public Stroke getStroke ()
  {
    if (isSimpleStyle ())
    {
      return new BasicStroke (getWidth(), getEndCap (), getJoinType(), 0);
    }
    return new BasicStroke (getWidth(), getEndCap (), getJoinType(), 0, getDashes(), 0);
  }
  
  private int getJoinType ()
  {
    switch (joinType)
    {
      case PS_JOIN_ROUND: return BasicStroke.JOIN_ROUND;
      case PS_JOIN_BEVEL: return BasicStroke.JOIN_BEVEL;
      case PS_JOIN_MITER: return BasicStroke.JOIN_MITER;
      default: return BasicStroke.JOIN_ROUND;
    }
  }
  
  private int getEndCap ()
  {
    switch (endCap)
    {
      case PS_ENDCAP_ROUND: return BasicStroke.CAP_ROUND;
      case PS_ENDCAP_SQUARE: return BasicStroke.CAP_SQUARE;
      case PS_ENDCAP_FLAT: return BasicStroke.CAP_BUTT;
      default: return BasicStroke.CAP_ROUND;
    }
  }
  
  private float[] getDashes ()
  {
    switch (getStyle ())
    {
      case PS_DASH: return DASH_DASH; 
      case PS_DOT: return DASH_DOT; 
      case PS_DASHDOT: return DASH_DASHDOT; 
      case PS_DASHDOTDOT: return DASH_DASHDOTDOT; 
      default: throw new IllegalStateException ("Illegal Pen defined");
    }
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("MfLogPen:=");
    b.append (" width=");
    b.append (getWidth ());
    b.append (" style=");
    b.append (getStyle ());
    b.append (" color=");
    b.append (getColor ());
    return b.toString ();
  }

}
