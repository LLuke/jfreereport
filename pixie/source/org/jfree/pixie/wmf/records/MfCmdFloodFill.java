package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;
import java.awt.Point;

public class MfCmdFloodFill extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;
  private Color color;

  public MfCmdFloodFill ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdFloodFill ();
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

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(4);
    record.setLongParam(0, org.jfree.pixie.wmf.GDIColor.translateColor(getColor()));
    Point target = getTarget();
    record.setParam(2, (int) target.getY());
    record.setParam(3, (int) target.getX());
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[FLOOD_FILL] color=");
    b.append (getColor ());
    b.append (" target=");
    b.append (getTarget ());
    return b.toString ();
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
    return org.jfree.pixie.wmf.MfType.FLOOD_FILL;
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
