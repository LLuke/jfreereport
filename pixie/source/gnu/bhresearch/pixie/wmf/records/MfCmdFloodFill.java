package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdFloodFill ();
  }

  public void setRecord (MfRecord record)
  {
    int c = record.getLongParam (0);
    Color color = new GDIColor (c);
    int y = record.getParam (2);
    int x = record.getParam (3);
    setTarget (x, y);
    setColor (color);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(4);
    record.setLongParam(0, GDIColor.translateColor(getColor()));
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
    return MfType.FLOOD_FILL;
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
