package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class MfCmdPie extends MfCmd
{
  private int x;
  private int y;
  private int width;
  private int height;

  private int xstart;
  private int ystart;
  private int xend;
  private int yend;

  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  private int scaled_xstart;
  private int scaled_ystart;
  private int scaled_xend;
  private int scaled_yend;

  public MfCmdPie ()
  {
  }

  public void replay (WmfFile file)
  {
    BufferedImage img = file.getImage ();
    Graphics2D graph = file.getGraphics2D ();
    Rectangle rec = getScaledBounds ();
    Point start = getScaledStartingIntersection ();
    Point end = getScaledEndingIntersection ();

    Arc2D arc = new Arc2D.Double ();
    arc.setArcType (Arc2D.PIE);
    arc.setFrame (rec.x, rec.y, rec.width, rec.height);
    arc.setAngles (start.x, start.y, end.x, end.y);

    MfDcState state = file.getCurrentState ();

    if (state.getLogBrush ().isVisible ())
    {
      state.preparePaint ();
      graph.fill (arc);
      state.postPaint ();
    }
    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (arc);
      state.postDraw ();
    }
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_width = getScaledX (width);
    scaled_xstart = getScaledX (xstart);
    scaled_xend = getScaledX (xend);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_height = getScaledY (height);
    scaled_ystart = getScaledY (xstart);
    scaled_yend = getScaledY (xend);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdPie ();
  }

  public int getFunction ()
  {
    return MfType.PIE;
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, width, height);
  }

  public Rectangle getScaledBounds ()
  {
    return new Rectangle (scaled_x, scaled_y, scaled_width, scaled_height);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[PIE] bounds=");
    b.append (getBounds ());
    b.append (" startIntersection=");
    b.append (getStartingIntersection ());
    b.append (" endingIntersection=");
    b.append (getEndingIntersection ());
    return b.toString ();
  }

  public void setBounds (int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();

  }

  public void setStartingIntersection (int x, int y)
  {
    xstart = x;
    ystart = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getStartingIntersection ()
  {
    return new Point (xstart, ystart);
  }

  public Point getScaledStartingIntersection ()
  {
    return new Point (scaled_xstart, scaled_ystart);
  }

  public void setEndingIntersection (int x, int y)
  {
    xend = x;
    yend = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public Point getEndingIntersection ()
  {
    return new Point (xend, yend);
  }

  public Point getScaledEndingIntersection ()
  {
    return new Point (scaled_xend, scaled_yend);
  }

  public void setRecord (MfRecord record)
  {
    int xend = record.getParam (0);
    int yend = record.getParam (1);
    int xstart = record.getParam (2);
    int ystart = record.getParam (3);
    int bottom = record.getParam (4);
    int right = record.getParam (5);
    int top = record.getParam (6);
    int left = record.getParam (7);
    setBounds (left, top, right - left, bottom - top);
    setStartingIntersection (xstart, ystart);
    setEndingIntersection (xend, yend);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(8);
    Rectangle bounds = getBounds();
    Point start = getStartingIntersection();
    Point end = getEndingIntersection();

    record.setParam(7, (int) bounds.getX());
    record.setParam(6, (int) bounds.getY());
    record.setParam(5, (int)(bounds.getX() + bounds.getWidth()));
    record.setParam(4, (int)(bounds.getY() + bounds.getHeight()));
    record.setParam(3, (int)(start.getY()));
    record.setParam(2, (int)(start.getX()));
    record.setParam(1, (int)(end.getY()));
    record.setParam(0, (int)(end.getX()));
    return record;
  }

}
