package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import java.awt.Rectangle;
import gnu.bhresearch.pixie.wmf.WmfFile;

public class MfCmdPatBlt extends MfCmd
{
  private int rop;
  private int height;
  private int width;
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;
  
  public MfCmdPatBlt ()
  {
  }

  public void replay (WmfFile file)
  {
    /// Need affine Transform, not yet implemented
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdPatBlt ();
  }
  
  public int getROP ()
  {
    return rop;
  }
  
  public void setROP (int rop)
  {
    this.rop = rop;
  }
  
  public void setRecord (MfRecord record)
  {
    int rop = record.getLongParam (0);
    int height = record.getParam (2);
    int width = record.getParam (3);
    int top = record.getParam (4);
    int left = record.getParam (5);
    setBounds (left, top, width, height);
    setROP (rop);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[PAT_BLT] rop=");
    b.append (getROP());
    b.append (" bounds=");
    b.append (getBounds());
    return b.toString();
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
    this.width  = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
    
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
  
  public int getFunction ()
  {                      
    return MfType.PAT_BLT;
  }
}
