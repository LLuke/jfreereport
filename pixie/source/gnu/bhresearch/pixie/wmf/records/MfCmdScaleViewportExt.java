package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

public class MfCmdScaleViewportExt extends MfCmd
{
  private int yNum;
  private int yDenom;
  private int xNum;
  private int xDenom;
  
  public MfCmdScaleViewportExt ()
  {
  }

  public void replay (WmfFile file)
  {
    // Not yet implemented 
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdScaleViewportExt ();
  }
  
  public int getFunction ()
  {
    return MfType.SCALE_VIEWPORT_EXT;
  }
  
  public void setRecord (MfRecord record)
  {
    int yD = record.getParam (0);
    int yN = record.getParam (1);
    int xD = record.getParam (0);
    int xN = record.getParam (1);
    setXScale (xN, xD);
    setYScale (yN, xD);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SCALE_VIEWPORT] scaleX=");
    b.append (getXNum());
    b.append ("/");
    b.append (getXDenom());
    b.append (" scaley=");
    b.append (getYNum());
    b.append ("/");
    b.append (getYDenom());
    return b.toString();
  }
  
  public void setXScale (int xNum, int xDenom)
  {
    this.xNum = xNum;
    this.xDenom = xDenom;
  }

  public void setYScale (int yNum, int yDenom)
  {
    this.yNum = yNum;
    this.yDenom = yDenom;
  }
  
  public double getXScale ()
  {
    return (double) xNum / xDenom;
  }
  
  public int getXNum ()
  {
    return xNum;
  }
  
  public int getXDenom ()
  {
    return xDenom;
  }
  
  public double getYScale ()
  {
    return (double) yNum / yDenom;
  }
  
  public int getYNum ()
  {
    return yNum;
  }
  
  public int getYDenom ()
  {
    return yDenom;
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
}
