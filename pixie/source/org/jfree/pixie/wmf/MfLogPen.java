/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * MfLogPen.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfLogPen.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

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
  public MfLogPen ()
  {
    style = PS_SOLID;
    width = 0;
    color = Color.black;
  }

  /** Return one of the PS_ styles. */
  public int getStyle ()
  {
    return style;
  }

  public void setStyle (int style)
  {
    this.style = style & 0x000000FF;
    this.endCap = style & 0x00000F00;
    this.joinType = style & 0x0000F000;
  }

  /** Return width. */
  public int getWidth ()
  {
    return width;
  }

  public void setWidth (int width)
  {
    this.width = width;
  }

  /** Return color of the current pen, or null. */
  public Color getColor ()
  {
    return color;
  }

  public void setColor (Color color)
  {
    this.color = color;
  }

  /** True if  not a dashed or dotted style. */
  public boolean isSimpleStyle ()
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
    return getStyle () != PS_NULL;
  }

  public int getType ()
  {
    return OBJ_PEN;
  }

  public Stroke getStroke ()
  {
    if (isSimpleStyle ())
    {
      return new BasicStroke (getWidth (), getEndCap (), getJoinType (), 0);
    }
    return new BasicStroke (getWidth (), getEndCap (), getJoinType (), 0, getDashes (), 0);
  }

  private int getJoinType ()
  {
    switch (joinType)
    {
      case PS_JOIN_ROUND:
        return BasicStroke.JOIN_ROUND;
      case PS_JOIN_BEVEL:
        return BasicStroke.JOIN_BEVEL;
      case PS_JOIN_MITER:
        return BasicStroke.JOIN_MITER;
      default:
        return BasicStroke.JOIN_ROUND;
    }
  }

  private int getEndCap ()
  {
    switch (endCap)
    {
      case PS_ENDCAP_ROUND:
        return BasicStroke.CAP_ROUND;
      case PS_ENDCAP_SQUARE:
        return BasicStroke.CAP_SQUARE;
      case PS_ENDCAP_FLAT:
        return BasicStroke.CAP_BUTT;
      default:
        return BasicStroke.CAP_ROUND;
    }
  }

  private float[] getDashes ()
  {
    switch (getStyle ())
    {
      case PS_DASH:
        return DASH_DASH;
      case PS_DOT:
        return DASH_DOT;
      case PS_DASHDOT:
        return DASH_DASHDOT;
      case PS_DASHDOTDOT:
        return DASH_DASHDOTDOT;
      default:
        throw new IllegalStateException ("Illegal Pen defined");
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
