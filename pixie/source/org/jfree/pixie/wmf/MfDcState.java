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
 * MfDcState.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfDcState.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 Track the state of the DeviceContext of a Windows metafile.
 */
public class MfDcState implements Cloneable
{
  public class MfScale
  {
    private int yNum;
    private int xNum;
    private int yDenom;
    private int xDenom;

    public MfScale (int xNum, int xDenom, int yNum, int yDenom)
    {
      if (xNum == 0 || yNum == 0 || xDenom == 0 || yDenom == 0)
        throw new IllegalArgumentException ("Illegal Scaling");

      this.xNum = xNum;
      this.yNum = yNum;
      this.xDenom = xDenom;
      this.yDenom = yDenom;
    }

    public int scaleX (int coord)
    {
      return ((coord * xNum) / xDenom);
    }

    public int scaleY (int coord)
    {
      return ((coord * yNum) / yDenom);
    }
  }

  private int viewportOrgX = 0;
  private int viewportOrgY = 0;

  private int viewportExtX = 1;
  private int viewportExtY = 1;

  private int windowExtX = 1;
  private int windowExtY = 1;

  private MfScale viewportScale;
  private MfScale windowScale;

  private int curPosX = 0;
  private int curPosY = 0;

  private Color fgColor;
  private Color bkColor;
  private Color textColor;

  private int textAlign = 0;
  private int textCharExtra;
  private int breakCount;
  private int extraSpaceLength;

  private int bkmode;

  private MfLogBrush logBrush;
  private MfLogPen logPen;
  private MfLogFont logFont;
  private MfLogRegion logRegion;
  private MfLogPalette logPalette;

  private int polyFillMode;
  private int rop;
  private int mapMode;
  private int mapperFlag;
  private int stretchBltMode;

  private Rectangle clipRegion;
  private WmfFile parent;

  public MfDcState (WmfFile parent)
  {
    fgColor = Color.black;
    bkColor = Color.white;
    textColor = Color.black;

    this.logBrush = new MfLogBrush ();
    this.logPen = new MfLogPen ();
    this.parent = parent;
  }

  public MfDcState (MfDcState copy)
  {
    this.parent = copy.parent;
    this.windowExtX = copy.windowExtX;
    this.windowExtY = copy.windowExtY;

    this.viewportOrgX = copy.viewportOrgX;
    this.viewportOrgY = copy.viewportOrgY;

    this.viewportExtX = copy.viewportExtX;
    this.viewportExtY = copy.viewportExtY;

    this.curPosX = copy.curPosX;
    this.curPosY = copy.curPosY;

    this.fgColor = copy.fgColor;
    this.bkColor = copy.bkColor;

    this.textAlign = copy.textAlign;
    this.textCharExtra = copy.textCharExtra;
    this.textColor = copy.textColor;

    this.logBrush = copy.logBrush;
    this.logPen = copy.logPen;
    this.logFont = copy.logFont;

    this.mapMode = copy.mapMode;
    this.mapperFlag = copy.mapperFlag;
    this.stretchBltMode = copy.stretchBltMode;

    this.viewportScale = copy.viewportScale;
    this.windowScale = copy.windowScale;

    this.polyFillMode = copy.polyFillMode;
    this.rop = copy.rop;

    this.bkmode = copy.bkmode;
    this.breakCount = copy.breakCount;
    this.extraSpaceLength = copy.extraSpaceLength;

    this.clipRegion = new Rectangle (copy.clipRegion);
  }

  public void restoredState ()
  {
    Graphics2D graphic = parent.getGraphics2D ();
    if (logBrush != null)
    {
      updateBrushBackground ();
      graphic.setPaint (logBrush.getPaint ());
    }
    if (logPen != null)
      graphic.setStroke (logPen.getStroke ());

    if (logFont != null)
      graphic.setFont (logFont.createFont ());

  }

  // if no clipping region is set and the default clipping region
  // is not modified, return the current viewport
  //
  // btw. i dont have a clue, whether this is the correct implementation :)
  public Rectangle getClipRegion ()
  {
    if (clipRegion == null)
    {
      return new Rectangle (viewportOrgX, viewportOrgY, viewportExtX, viewportExtY);
    }
    return clipRegion;
  }

  public void setClipRegion (Rectangle clipRegion)
  {
    this.clipRegion = clipRegion;
  }

  public int getBkMode ()
  {
    return bkmode;
  }

  public void setBkMode (int bkmode)
  {
    if (this.bkmode != bkmode)
    {
      this.bkmode = bkmode;
      updateBrushBackground ();
    }
  }

  private void updateBrushBackground ()
  {
    if (bkmode == BrushConstants.TRANSPARENT)
    {
      logBrush.setBackgroundColor (new Color (MfLogBrush.COLOR_FULL_ALPHA, true));
    }
    else
    {
      logBrush.setBackgroundColor (getBkColor ());
    }
  }

  public int getBreakCount ()
  {
    return breakCount;
  }

  public int getExtraSpaceLength ()
  {
    return extraSpaceLength;
  }

  public void setTextJustification (int breakCount, int extraSpaceLength)
  {
    this.breakCount = breakCount;
    this.extraSpaceLength = extraSpaceLength;
  }

  public int getStretchBltMode ()
  {
    return stretchBltMode;
  }

  public void setStretchBltMode (int stretchBltMode)
  {
    this.stretchBltMode = stretchBltMode;
  }

  public Color getTextColor ()
  {
    return textColor;
  }

  public void setTextColor (Color textColor)
  {
    this.textColor = textColor;
  }

  public int getTextCharExtra ()
  {
    return textCharExtra;
  }

  public void setTextCharExtra (int textCharExtra)
  {
    this.textCharExtra = textCharExtra;
  }


  public int getMapMode ()
  {
    return mapMode;
  }

  public void setMapMode (int mapMode)
  {
    this.mapMode = mapMode;
  }

  public int getMapperFlag ()
  {
    return mapperFlag;
  }

  public void setMapperFlag (int mapperFlag)
  {
    this.mapperFlag = mapperFlag;
  }


  public int getROP ()
  {
    return rop;
  }

  public void setROP (int rop)
  {
    this.rop = rop;
  }

  public int getPolyFillMode ()
  {
    return polyFillMode;
  }

  public void setPolyFillMode (int mode)
  {
    this.polyFillMode = mode;
  }

  public void setWindowOrg (int windowOrgX, int windowOrgY)
  {
    this.viewportOrgY = -windowOrgY;
    this.viewportOrgX = -windowOrgX;
  }

  public int getWindowOrgX ()
  {
    return viewportOrgX;
  }

  public int getWindowOrgY ()
  {
    return viewportOrgY;
  }

  public void setWindowExt (int windowExtX, int windowExtY)
  {
    this.windowExtY = windowExtY;
    this.windowExtX = windowExtX;
  }

  public int getWindowExtX ()
  {
    return windowExtX;
  }

  public int getWindowExtY ()
  {
    return windowExtY;
  }

  public void setViewportOrg (int viewportOrgX, int viewportOrgY)
  {
    this.viewportOrgX = viewportOrgX;
    this.viewportOrgY = viewportOrgY;
  }

  public int getViewportOrgX ()
  {
    return viewportOrgX;
  }

  public int getViewportOrgY ()
  {
    return viewportOrgY;
  }

  public void setViewportExt (int viewportExtX, int viewportExtY)
  {
    this.viewportExtY = viewportExtY;
    this.viewportExtX = viewportExtX;
  }

  public int getViewportExtX ()
  {
    return viewportExtX;
  }

  public int getViewportExtY ()
  {
    return viewportExtY;
  }

  public void setCurPos (int _curPosX, int _curPosY)
  {
    this.curPosY = _curPosY;
    this.curPosX = _curPosX;
  }

  public int getCurPosX ()
  {
    return curPosX;
  }

  public int getCurPosY ()
  {
    return curPosY;
  }

  public int getTextAlign ()
  {
    return textAlign;
  }

  public void setTextAlign (int textAlign)
  {
    this.textAlign = textAlign;
  }

  public void setFgColor (Color fgColor)
  {
    if (fgColor == null)
      throw new NullPointerException ();

    this.fgColor = fgColor;
    logBrush.setColor (fgColor);
    logPen.setColor (fgColor);
  }

  public void setBkColor (Color bkColor)
  {
    if (bkColor == null)
      throw new NullPointerException ();

    this.bkColor = bkColor;
    logBrush.setBackgroundColor (bkColor);
  }

  public Color getFgColor ()
  {
    return fgColor;
  }

  public Color getBkColor ()
  {
    return bkColor;
  }

  public MfLogFont getLogFont ()
  {
    return logFont;
  }

  public MfLogBrush getLogBrush ()
  {
    return logBrush;
  }

  public MfLogPen getLogPen ()
  {
    return logPen;
  }

  public void setLogFont (MfLogFont logFont)
  {
    if (logFont == null)
      throw new NullPointerException ();

    this.logFont = logFont;
  }

  public void setLogBrush (MfLogBrush logBrush)
  {
    if (logBrush == null)
      throw new NullPointerException ();

    this.logBrush = logBrush;
    updateBrushBackground ();
    parent.getGraphics2D ().setPaint (logBrush.getPaint ());
  }

  public void setLogPen (MfLogPen logPen)
  {
    if (logPen == null)
      throw new NullPointerException ();

    this.logPen = logPen;
    parent.getGraphics2D ().setStroke (logPen.getStroke ());
  }

  public void setLogPalette (MfLogPalette logPalette)
  {
    if (logPalette == null)
      throw new NullPointerException ();

    this.logPalette = logPalette;
  }

  public void setLogRegion (MfLogRegion logRegion)
  {
    if (logRegion == null)
      throw new NullPointerException ();

    this.logRegion = logRegion;
  }

  // No scaling yet?
  public int getPhysicalX (int logPointX)
  {
    return logPointX + viewportOrgX;
  }

  // No scaling yet?
  public int getPhysicalY (int logPointY)
  {
    return logPointY + viewportOrgY;
  }

  public void prepareDraw ()
  {
    parent.getGraphics2D ().setPaint (logPen.getColor ());
  }

  public void postDraw ()
  {
    parent.getGraphics2D ().setPaint (logBrush.getPaint ());
  }

  public void prepareDrawText ()
  {
    parent.getGraphics2D ().setPaint (textColor);
    parent.getGraphics2D ().setFont (logFont.createFont ());
  }

  public void postDrawText ()
  {
    parent.getGraphics2D ().setPaint (logBrush.getPaint ());
  }

  public void preparePaint ()
  {
    parent.getGraphics2D ().setPaint (logBrush.getPaint ());
  }

  public void postPaint ()
  {

  }

  public int getVerticalTextAlignment ()
  {
    return (textAlign & TextConstants.TA_CENTER);
  }

  public int getHorizontalTextAlignment ()
  {
    return (textAlign & TextConstants.TA_BASELINE);
  }

  public MfLogRegion getLogRegion()
  {
    return logRegion;
  }

  public MfLogPalette getLogPalette()
  {
    return logPalette;
  }
}
