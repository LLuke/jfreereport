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
 * MfCmdExtTextOut.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdEllipse.java,v 1.2 2003/03/14 20:06:06 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.BrushConstants;
import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfLogFont;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.TextConstants;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * The ExtTextOut function draws text using the currently selected font,
 * background color, and text color. You can optionally provide dimensions
 * to be used for clipping, opaquing, or both.
 * <p>
 * META_EXTTEXTOUT
 * <h1>NEAREST API CALL</h1>
 * <pre>#include &lt;windows.h&gt;
    BOOL32 ExtTextOutA
    (
     HDC32 hdc,
     INT32 x,
     INT32 y,
     UINT32 flags,
     const RECT32 *lprect,
     LPCSTR str,
     UINT32 count,
     const INT32 *lpDx
    );
    </pre>
 *
 */

public class MfCmdExtTextOut extends MfCmd
{
  private static final int POS_Y = 0;
  private static final int POS_X = 1;
  private static final int POS_CHAR_COUNT = 2;
  private static final int POS_FLAGS = 3;
  private static final int POS_CLIP_X = 4;
  private static final int POS_CLIP_Y = 5;
  private static final int POS_CLIP_W = 6;
  private static final int POS_CLIP_H = 7;
  private static final int RECORD_BASE_SIZE_CLIPPED = 8;
  private static final int RECORD_BASE_SIZE_STANDARD = 4;

  public static final int ETO_OPAQUE = 0x0002;
  public static final int ETO_CLIPPED = 0x0004;
  public static final int ETO_GLYPH_INDEX = 0x0010;
  public static final int ETO_RTLREADING = 0x0080;
  public static final int ETO_IGNORELANGUAGE = 0x1000;

  private int flags;
  private int x;
  private int y;
  private int cx;
  private int cy;
  private int cw;
  private int ch;
  private int scaled_x;
  private int scaled_y;
  private int scaled_cx;
  private int scaled_cy;
  private int scaled_cw;
  private int scaled_ch;
  private String text;

  public MfCmdExtTextOut ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    Graphics2D graphics = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();
    MfLogFont lFont = state.getLogFont ();

    state.prepareDrawText ();
    FontMetrics metrics = graphics.getFontMetrics ();
    int textWidth = metrics.stringWidth (text);
    Point p = getScaledOrigin ();
    int x = p.x + calcDeltaX (state.getVerticalTextAlignment (), textWidth);
    int y = p.y + calcDeltaY (state.getHorizontalTextAlignment (), metrics);

    if (isOpaque () || state.getBkMode () != BrushConstants.TRANSPARENT)
    {
      Rectangle background = new Rectangle (x, y - metrics.getAscent (), textWidth, metrics.getHeight ());
      graphics.setColor (state.getBkColor ());
      graphics.fill (background);
      graphics.setColor (state.getTextColor ());
    }
    graphics.drawString (text, x, y);
    state.postDrawText ();

    if (lFont.isUnderline ())
    {	// Underline.
      y += metrics.getDescent () / 8 + 1;
      state.prepareDraw ();
      graphics.drawLine (x, y, x + textWidth, y);
      state.postDraw ();
    }
    if (lFont.isStrikeOut ())
    {	// Underline.
      state.prepareDraw ();
      y -= metrics.getDescent () / 2 + 1;
      graphics.drawLine (x, y, x + textWidth, y);
      state.postDraw ();
    }
  }

  protected int calcDeltaX (int valign, int textWidth)
  {
    if (valign == TextConstants.TA_LEFT)
    {
      return 0;
    }
    else if (valign == TextConstants.TA_CENTER)
    {
      return textWidth / -2;
    }
    else
    {
      return textWidth * -1;
    }
  }

  protected int calcDeltaY (int halign, FontMetrics fm)
  {
    if (halign == TextConstants.TA_TOP)
      return (fm.getAscent () * -1);
    else if (halign == TextConstants.TA_BOTTOM)
      return (fm.getDescent ());
    else
      return 0;
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdExtTextOut ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.EXT_TEXT_OUT;
  }

  /**
   * Reads the command data from the given record and adjusts the internal
   * parameters according to the data parsed.
   * <p>
   * After the raw record was read from the datasource, the record is parsed
   * by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (MfRecord record)
  {
    int y = record.getParam (POS_Y);
    int x = record.getParam (POS_X);
    int count = record.getParam (POS_CHAR_COUNT);
    int flag = record.getParam (POS_FLAGS);
    int stringOffset = 0;

    int cx = 0;
    int cy = 0;
    int cw = 0;
    int ch = 0;
    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      cx = record.getParam (POS_CLIP_X);
      cy = record.getParam (POS_CLIP_Y);
      cw = record.getParam (POS_CLIP_W);
      ch = record.getParam (POS_CLIP_H);
      stringOffset = RECORD_BASE_SIZE_CLIPPED;
    }
    else
    {
      stringOffset = RECORD_BASE_SIZE_STANDARD;
    }
    String text = record.getStringParam(stringOffset, count);

    setOrigin (x, y);
    setText (text);
    setClippingRect (cx, cy, cw, ch);
    setFlags (flag);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   * This writer does not write a char-spacing record.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    String text = getText();
    int flag = getFlags();
    int parcnt;
    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      parcnt = RECORD_BASE_SIZE_CLIPPED;
    }
    else
    {
      parcnt =  RECORD_BASE_SIZE_STANDARD;
    }

    int recordLength = (int) (Math.ceil(text.length() / 2) * 2) + parcnt;
    MfRecord record = new MfRecord(recordLength);

    Point origin = getOrigin();
    record.setParam(POS_Y, (int)origin.getY());
    record.setParam(POS_X, (int)origin.getX());
    record.setParam(POS_CHAR_COUNT, text.length());
    record.setParam(POS_FLAGS, flag);
    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      Rectangle rect = getClippingRect();
      record.setParam(POS_CLIP_X, rect.x);
      record.setParam(POS_CLIP_Y, rect.y);
      record.setParam(POS_CLIP_W, rect.width);
      record.setParam(POS_CLIP_H, rect.height);
    }
    record.setStringParam(parcnt, text);
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[EXT_TEXT_OUT] text=");
    b.append (getText ());
    b.append (" origin=");
    b.append (getOrigin ());
    b.append (" clippingRect=");
    b.append (getClippingRect ());
    b.append (" flags=");
    b.append (getFlags ());
    return b.toString ();
  }

  public void setOrigin (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getOrigin ()
  {
    return new Point (x, y);
  }

  public Point getScaledOrigin ()
  {
    return new Point (scaled_x, scaled_y);
  }

  public boolean isClipped ()
  {
    return (flags & ETO_CLIPPED) == ETO_CLIPPED;
  }

  public boolean isOpaque ()
  {
    return (flags & ETO_OPAQUE) == ETO_OPAQUE;
  }

  public int getFlags ()
  {
    return flags;
  }

  public void setFlags (int flags)
  {
    this.flags = flags;
  }

  public void setClippingRect (int cx, int cy, int cw, int ch)
  {
    this.cx = cx;
    this.cy = cy;
    this.cw = cw;
    this.ch = ch;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Rectangle getClippingRect ()
  {
    return new Rectangle (cx, cy, cw, ch);
  }

  public Rectangle getScaledClippingRect ()
  {
    return new Rectangle (scaled_cx, scaled_cy, scaled_cw, scaled_ch);
  }

  public void setText (String text)
  {
    this.text = text;
  }

  public String getText ()
  {
    return text;
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_cx = getScaledX (cx);
    scaled_cw = getScaledX (cw);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_cy = getScaledY (cy);
    scaled_ch = getScaledY (ch);
  }
}
