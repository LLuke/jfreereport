package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import java.awt.Point;
import java.awt.Rectangle;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

/**
 * Moves the current Clipping Region (@see CreateRegion) to the specified
 * position
 */
public class MfCmdOffsetClipRgn extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;
  
  public MfCmdOffsetClipRgn ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Rectangle clipRect = state.getClipRegion ();
    Point p = getScaledDestination ();
    clipRect.x = p.x;
    clipRect.y = p.y;
    state.setClipRegion (clipRect);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdOffsetClipRgn ();
  }
  
  public Point getDestination ()
  {
    return new Point (x,y);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OFFSET_CLIP_RECT] destination=");
    b.append (getDestination());
    return b.toString();
  }
  
  public void setDestination (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();
    
  }
  
  public int getFunction ()
  {
    return MfType.OFFSET_CLIP_RGN;
  }
  
  public void setRecord (MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setDestination (x,y);
  }

  public Point getScaledDestination ()
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
