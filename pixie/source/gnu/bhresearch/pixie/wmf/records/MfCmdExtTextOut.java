package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.BrushConstants;
import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfLogFont;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.TextConstants;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
META_EXTTEXTOUT
<h1>NEAREST API CALL</h1>
<pre>#include &lt;windows.h&gt;
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
<h1>DESCRIPTION</h1>
<TABLE BORDER =1>
<TR>
<TD>U16</TD>

<TD>Value</TD>
</TR>
<TR>
<TD>0</TD>
<TD>y</TD>
</TR>
<TR>
<TD>1</TD>
<TD>x</TD>
</TR>
<TR>
<TD>2</TD>
<TD>count</TD>
</TR>
<TR>
<TD>3</TD>
<TD>flag</TD>
</TR>
</table>

If flag & ETO_CLIPPED is true then the following parameters are...
<table border=1>
<TR>
<TD>U16</TD>

<TD>Value</TD>
</TR>
<TR>
<TD>4</TD>
<TD>x1</TD>
</TR>
<TR>
<TD>5</TD>
<TD>y1</TD>
</TR>
<TR>
<TD>6</TD>
<TD>x2</TD>
</TR>
<TR>
<TD>7</TD>
<TD>y2</TD>
</TR>
</TABLE>

In either case the next parameters are

<table border=1>
<TR>
<TD>U16</TD>

<TD>Value</TD>
</TR>
<TR>
<TD>next parameter, for a count of parameter[2]/2</TD>
<TD>two chars of the string</TD>
</TR>
</TABLE>

There may be extra parameters after the end of the string , this
array is known as lpDx and each element is the extra distance
from one character to the next.
<table border=1>
<TR>
<TD>U16</TD>

<TD>Value</TD>
</TR>
<TR>
<TD>each next parameter, until the end of the record</TD>
<TD></TD>
</TR>
</TABLE>

count is the number of characters in the string,
as each char is only a 8bit number, two chars exist in each
16bit parameter entry.<br>

flag is one or both of ETO_OPAQUE and ETO_CLIPPED,
the first means that the background of the box that the text
is typed in is OPAQUE, and the usual opaque rules apply,
(@see MfCmdSetBkMode).
The second means that the text is clipped to the rectangle supplied.
x1, y1, x2 and y2, are the points of the optional clipping rectangle,
included if flags has ETO_CLIPPED set<br>

Then the string follows, and then there is an optional array of widths, each
16bit width is the distance from one character to the next.
 */

public class MfCmdExtTextOut extends MfCmd
{
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

  public MfCmd getInstance ()
  {
    return new MfCmdExtTextOut ();
  }

  public int getFunction ()
  {
    return MfType.EXT_TEXT_OUT;
  }

  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    int count = record.getParam (2);
    int flag = record.getParam (3);
    int parCnt = 0;

    int cx = 0;
    int cy = 0;
    int cw = 0;
    int ch = 0;

    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      cx = record.getParam (4);
      cy = record.getParam (5);
      cw = record.getParam (6);
      ch = record.getParam (7);
      parCnt = 8 * 2 + record.RECORD_HEADER;
    }
    else
    {
      parCnt = 4 * 2 + record.RECORD_HEADER;
    }

    byte[] data = new byte[count];
    int len = count;

    for (int i = 0; i < count; i++)
    {
      data[i] = (byte) record.getByte (i + parCnt);
    }
    String text = new String (data);

    setOrigin (x, y);
    setText (text);
    setClippingRect (cx, cy, cw, ch);
    setFlags (flag);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    int flag = getFlags();
    int parcnt = 4;
    String text = getText();
    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      parcnt = 8;
    }
    int recordLength = (int) Math.ceil((text.length() + parcnt) / 2);
    MfRecord record = new MfRecord(recordLength);

    Point origin = getOrigin();
    record.setParam(0, (int)origin.getY());
    record.setParam(1, (int)origin.getX());
    record.setParam(2, text.length());
    record.setParam(3, flag);
    if ((flag & ETO_CLIPPED) == ETO_CLIPPED)
    {
      Rectangle rect = getClippingRect();
      record.setParam(4, rect.x);
      record.setParam(5, rect.y);
      record.setParam(6, rect.width);
      record.setParam(7, rect.height);
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
