package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MfCmdRectangle extends MfCmd
{
  private int x;
  private int y;
  private int width;
  private int height;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  public MfCmdRectangle ()
  {
  }

  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    Rectangle rec = getScaledBounds ();

    MfDcState state = file.getCurrentState ();

    if (state.getLogBrush ().isVisible ())
    {
      state.preparePaint ();
      graph.fill (rec);
      state.postPaint ();
    }
    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (rec);
      state.postDraw ();
    }

  }

  public MfCmd getInstance ()
  {
    return new MfCmdRectangle ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[RECTANGLE] bounds=");
    b.append (getBounds ());
    return b.toString ();
  }

  public void setRecord (MfRecord record)
  {
    int bottom = record.getParam (0);
    int right = record.getParam (1);
    int top = record.getParam (2);
    int left = record.getParam (3);
    setBounds (left, top, right - left, bottom - top);

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

  public int getFunction ()
  {
    return MfType.RECTANGLE;
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_width = getScaledX (width);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_height = getScaledY (height);
  }

}
