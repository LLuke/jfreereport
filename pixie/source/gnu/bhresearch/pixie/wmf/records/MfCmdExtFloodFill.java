package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
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

  public void setRecord (MfRecord record)
  {
    int filltype = record.getParam (0);
    int c = record.getLongParam (1);
    Color color = new GDIColor (c);
    int y = record.getParam (3);
    int x = record.getParam (4);
    setTarget (x, y);
    setColor (color);
    setFillType (filltype);
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
    return MfType.EXT_FLOOD_FILL;
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
