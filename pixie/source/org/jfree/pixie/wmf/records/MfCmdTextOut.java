package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Graphics2D;
import java.awt.Point;

public class MfCmdTextOut extends MfCmd
{
  private int x;
  private int y;
  private String text;
  private int count;
  private int scaled_x;
  private int scaled_y;

  public MfCmdTextOut ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    Point p = getScaledDestination ();
    int x = p.x;
    int y = p.y;

    Graphics2D graphics = file.getGraphics2D ();
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();

    state.prepareDrawText ();
    graphics.drawString (text, x, y);
    state.postDrawText ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdTextOut ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.TEXT_OUT;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int count = record.getParam (0);
    byte[] text = new byte[count];
    for (int i = 0; i < count; i++)
    {
      text[i] = (byte) record.getByte (record.RECORD_HEADER_SIZE + 2 + i);
    }
    String sText = new String (text);
    int y = record.getParam ((int) (Math.ceil (count / 2) + 2));
    int x = record.getParam ((int) (Math.ceil (count / 2) + 4));
    setCount (count);
    setDestination (x, y);
    setText (sText);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[TEXT_OUT] text=");
    b.append (getText ());
    b.append (" destination=");
    b.append (getDestination ());
    b.append (" count=");
    b.append (getCount ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public Point getDestination ()
  {
    return new Point (x, y);
  }

  public void setText (String text)
  {
    this.text = text;
  }

  public String getText ()
  {
    return text;
  }

  public int getCount ()
  {
    return count;
  }

  public void setCount (int count)
  {
    this.count = count;
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_x, scaled_y);
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
  }
}
