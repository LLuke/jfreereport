package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import java.awt.Point;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

/**
 * Defines the upper left corner of the Window. The size of the
 * window is defined using setWindowExt.
 */
public class MfCmdSetWindowOrg extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdSetWindowOrg ()
  {
  }

  /**
   * Implemented!
   */
  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Point p = getScaledTarget ();
    state.setWindowOrg (p.x, p.y);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetWindowOrg ();
  }
  
  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setTarget (x,y);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_WINDOW_ORG] target=");
    b.append (getTarget());
    return b.toString();
  }
  
  public Point getTarget ()
  {
    return new Point (x,y);
  }
  
  public void setTarget (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();
    
  }
  
  public int getFunction ()
  {
    return MfType.SET_WINDOW_ORG;
  }

  public Point getScaledTarget ()
  {
    return new Point (scaled_x, scaled_y);
  }
  
  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
  }
  
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
  }

}
