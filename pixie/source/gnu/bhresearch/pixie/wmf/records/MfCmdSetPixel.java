package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
  {
    // Not yet implemented
    Point p = getScaledTarget ();
    Graphics2D g = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();

    state.prepareDraw ();
    g.drawLine (p.x, p.y, p.x, p.y);
    state.postDraw ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetPixel ();
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
    return MfType.SET_PIXEL;
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
