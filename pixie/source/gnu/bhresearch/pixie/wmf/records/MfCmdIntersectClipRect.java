package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

/**
 * top, left, right and bottom define the points of the clipping region, 
 * the resultant clipping region is the intersection of this region and the 
 * original region. 
 */
public class MfCmdIntersectClipRect extends MfCmd
{
  private int x;
  private int y;
  private int width;
  private int height;

  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  public MfCmdIntersectClipRect ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    Rectangle rect = state.getClipRegion ();
    Rectangle2D rec2 = rect.createIntersection (getScaledIntersectClipRect ());
    state.setClipRegion (
      new Rectangle (
        (int) rec2.getX (), 
        (int) rec2.getY(), 
        (int) rec2.getWidth(), 
        (int) rec2.getHeight()));
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdIntersectClipRect ();
  }
  
  public int getFunction ()
  {
    return MfType.INTERSECT_CLIP_RECT;
  }
  
  public Rectangle getIntersectClipRect ()
  {
    return new Rectangle (x, y, width, height);
  }

  public Rectangle getScaledIntersectClipRect ()
  {
    return new Rectangle (scaled_x, scaled_y, scaled_width, scaled_height);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[INTERSECT_CLIP_RECT] bounds=");
    b.append (getIntersectClipRect());
    return b.toString();
  }
  
  public void setIntersectClipRect (int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width  = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
  }
  
  public void setRecord (MfRecord record)
  {
    int bottom = record.getParam (0);
    int right = record.getParam (1);
    int top = record.getParam (2);
    int left = record.getParam (3);
    setIntersectClipRect (left, top, right - left, bottom - top);
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
    scaled_width = getScaledX(width);
  }
  
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
    scaled_height = getScaledY(height);
  }
}
