package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Point;

/**
 * Moves the current Clipping Region (@see CreateRegion) to the specified
 * position
 */
public class MfCmdOffsetWindowOrg extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdOffsetWindowOrg ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Point p = getScaledDestination ();
    state.setWindowOrg (state.getWindowOrgX () + x, state.getWindowOrgY () + y);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdOffsetWindowOrg ();
  }

  public Point getDestination ()
  {
    return new Point (x, y);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OFFSET_WINDOWORG] destination=");
    b.append (getDestination ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    Point dest = getDestination();
    MfRecord record = new MfRecord(2);
    record.setParam(0, dest.y);
    record.setParam(1, dest.x);
    return record;
  }

  public int getFunction ()
  {
    return MfType.OFFSET_WINDOW_ORG;
  }

  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setDestination (x, y);
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_x, scaled_y);
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
