package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

public class MfCmdRoundRect extends MfCmd
{
  private int x;
  private int y;
  private int width;
  private int height;
  private int roundWidth;
  private int roundHeight;

  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;
  private int scaled_roundWidth;
  private int scaled_roundHeight;

  public MfCmdRoundRect ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    Rectangle rec = getScaledBounds ();
    Dimension dim = getScaledRoundingDim ();
    RoundRectangle2D shape = new RoundRectangle2D.Double ();
    shape.setRoundRect (rec.x, rec.y, rec.width, rec.height, dim.width, dim.height);
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();

    if (state.getLogBrush ().isVisible ())
    {
      state.preparePaint ();
      graph.fill (shape);
      state.postPaint ();
    }
    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (shape);
      state.postDraw ();
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdRoundRect ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[ROUND_RECTANGLE] bounds=");
    b.append (getBounds ());
    b.append (" roundingDim=");
    b.append (getRoundingDim ());
    return b.toString ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int rHeight = record.getParam (0);
    int rWidth = record.getParam (1);
    int bottom = record.getParam (2);
    int right = record.getParam (3);
    int top = record.getParam (4);
    int left = record.getParam (5);
    setBounds (left, top, right - left, bottom - top);
    setRoundingDim (rWidth, rHeight);
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, width, height);
  }

  public Rectangle getScaledBounds ()
  {
    return new Rectangle (scaled_x, scaled_y, scaled_width, scaled_height);
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

  public void setRoundingDim (int w, int h)
  {
    this.roundWidth = w;
    this.roundHeight = h;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Dimension getRoundingDim ()
  {
    return new Dimension (roundWidth, roundHeight);
  }

  public Dimension getScaledRoundingDim ()
  {
    return new Dimension (scaled_roundWidth, scaled_roundHeight);
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_width = getScaledX (width);
    scaled_roundWidth = getScaledX (roundWidth);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_height = getScaledY (height);
    scaled_roundHeight = getScaledY (roundHeight);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.ROUND_RECT;
  }
}
