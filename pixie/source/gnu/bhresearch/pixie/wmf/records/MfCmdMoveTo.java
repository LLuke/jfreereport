package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Graphics2D;
import java.awt.Point;


/**
 * The cursor is set to the destination point.
 */
public class MfCmdMoveTo extends MfCmd
{
  private int destX;
  private int destY;
  private int scaled_destX;
  private int scaled_destY;

  public MfCmdMoveTo ()
  {
  }

  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();

    Point p = getScaledDestination ();
    state.setCurPos (p.x, p.y);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdMoveTo ();
  }

  public int getFunction ()
  {
    return MfType.MOVE_TO;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[MOVE_TO] destination=");
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
