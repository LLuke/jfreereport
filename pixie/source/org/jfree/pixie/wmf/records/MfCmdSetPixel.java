package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class MfCmdSetPixel extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;
  private Color color;

  public MfCmdSetPixel ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    // Not yet implemented
    Point p = getScaledTarget ();
    Graphics2D g = file.getGraphics2D ();
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();

    state.prepareDraw ();
    g.drawLine (p.x, p.y, p.x, p.y);
    state.postDraw ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetPixel ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int c = record.getLongParam (0);
    Color color = new org.jfree.pixie.wmf.GDIColor (c);
    int y = record.getParam (2);
    int x = record.getParam (3);
    setTarget (x, y);
    setColor (color);
  }

  public Point getTarget ()
  {
    return new Point (x, y);
  }

  public Point getScaledTarget ()
  {
    return new Point (scaled_x, scaled_y);
  }

  public void setTarget (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public void setColor (Color c)
  {
    this.color = c;
  }

  public Color getColor ()
  {
    return color;
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_PIXEL;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_PIXEL] target=");
    b.append (getTarget ());
    b.append (" color=");
    b.append (getColor ());
    return b.toString ();
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
