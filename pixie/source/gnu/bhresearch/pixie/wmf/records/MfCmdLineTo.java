package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * Draws a line from the current CursorPosition to the destination.
 * The cursor is set to the destination after drawing.
 */
public class MfCmdLineTo extends MfCmd
{
  private int destX;
  private int destY;
  private int scaled_destX;
  private int scaled_destY;

  public MfCmdLineTo ()
  {
  }

  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();

    int cx = state.getCurPosX ();
    int cy = state.getCurPosY ();

    Point destP = getScaledDestination ();

    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (new Line2D.Double (cx, cy, destP.x, destP.y));
      state.postDraw ();

    }
    state.setCurPos (destP.x, destP.y);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdLineTo ();
  }

  public int getFunction ()
  {
    return MfType.LINE_TO;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[LINE_TO] destination=");
    b.append (getDestination ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    destX = x;
    destY = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getDestination ()
  {
    return new Point (destX, destY);
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_destX, scaled_destY);
  }

  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setDestination (x, y);
  }

  protected void scaleXChanged ()
  {
    scaled_destX = getScaledX (destX);
  }

  protected void scaleYChanged ()
  {
    scaled_destY = getScaledY (destY);
  }
}
