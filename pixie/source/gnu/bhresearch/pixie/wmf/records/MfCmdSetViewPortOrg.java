package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import java.awt.Point;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

/**
 * Defines the upper left corner of the viewport. The size of the
 * viewport is defined using setViewportExt.
 */
public class MfCmdSetViewPortOrg extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdSetViewPortOrg ()
  {
  }

  /**
   * Implemented!!
   */
  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Point p = getScaledTarget ();
    state.setViewportOrg (p.x, p.y);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetViewPortOrg ();
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_VIEWPORT_ORG] target=");
    b.append (getTarget());
    return b.toString();
  }
  
  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setTarget (x,y);
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
  
  public Point getScaledTarget ()
  {
    return new Point (scaled_x, scaled_y);
  }
  
  public int getFunction ()
  {
    return MfType.SET_VIEWPORT_ORG;
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
