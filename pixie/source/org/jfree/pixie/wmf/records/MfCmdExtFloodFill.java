package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;
import java.awt.Point;

public class MfCmdExtFloodFill extends MfCmd
{
  /* ExtFloodFill style flags */
  public static final int FLOODFILLBORDER = 0;
  public static final int FLOODFILLSURFACE = 1;

  private int filltype;
  private Color color;
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdExtFloodFill ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[EXT_FLOOD_FILL] filltype=");
    b.append (getFillType ());
    b.append (" color=");
    b.append (getColor ());
    b.append (" target=");
    b.append (getTarget ());
    return b.toString ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdExtFloodFill ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int filltype = record.getParam (0);
    int c = record.getLongParam (1);
    Color color = new org.jfree.pixie.wmf.GDIColor (c);
    int y = record.getParam (3);
    int x = record.getParam (4);
    setTarget (x, y);
    setColor (color);
    setFillType (filltype);
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(5);
    record.setParam(0, getFillType());
    record.setLongParam(1, org.jfree.pixie.wmf.GDIColor.translateColor(getColor()));
    Point target = getTarget();
    record.setParam(3, (int) target.getY());
    record.setParam(4, (int) target.getX());
    return null;
  }

  public void setFillType (int filltype)
  {
    this.filltype = filltype;
  }

  public int getFillType ()
  {
    return filltype;
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.EXT_FLOOD_FILL;
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

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
  }
}
