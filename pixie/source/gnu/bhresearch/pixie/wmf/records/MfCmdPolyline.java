package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * The polyline does not use the current cursor position as starting
 * point of the first line. The starting point is defined by the first
 * coordinate of the point-array.
 */
public class MfCmdPolyline extends MfCmd
{
  private int[] points_x;
  private int[] points_y;
  private int[] scaled_points_x;
  private int[] scaled_points_y;
  private int count;
  
  public MfCmdPolyline ()
  {
  }

  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    MfDcState state = file.getCurrentState ();
    int cx = state.getCurPosX ();
    int cy = state.getCurPosY ();
    int[] points_x = getScaledPointsX ();
    int[] points_y = getScaledPointsY ();
    
    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      cx = points_x[0]; 
      cy = points_y[0];
      for (int i = 1; i < count; i++)
      {
        int destX = points_x[i]; 
        int destY = points_y[i];
        graph.draw (new Line2D.Double (cx, cy, destX, destY));
        cx = destX;
        cy = destY;
      }
      state.preparePaint ();
    }
    state.setCurPos (cx,cy);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdPolyline ();
  }
  
  public int getFunction ()
  {
    return MfType.POLYLINE;
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[POLYLINE] count=");
    b.append (getPointCount());
    int l = getPointCount();
    int[] points_x = getPointsX();
    int[] points_y = getPointsY();
    
    for (int i = 0; i < l; i++)
    {
      if (i != 0) b.append (",");
      
      b.append (" (");
      b.append (points_x[i]);
      b.append (",");
      b.append (points_y[i]);
      b.append (") ");
    }
    return b.toString();
  }
  
  public void setRecord (MfRecord record)
  {
    int count = record.getParam (0);
    int[] points_x = new int[count];
    int[] points_y = new int[count];
    
    for (int i = 0; i < count; i++)
    {
      points_x[i] = record.getParam (1 + 2*i);
      points_y[i] = record.getParam (2 + 2*i);
    }
    setPointCount (count);
    setPoints (points_x, points_y);
  }
  
  public void setPointCount (int count)
  {
    this.count = count;
  }
  
  public void setPoints (int[] points_x, int[] points_y)
  {
    this.points_x = points_x;
    this.points_y = points_y;
    scaleXChanged ();
    scaleYChanged ();
    
  }
  
  public int[] getPointsX ()
  {
    return points_x;
  }

  public int[] getPointsY ()
  {
    return points_y;
  }
  
  public int getPointCount ()
  {
    return count;
  }

  public int[] getScaledPointsX ()
  {
    return scaled_points_x;
  }

  public int[] getScaledPointsY ()
  {
    return scaled_points_y;
  }
  
  protected void scaleXChanged ()
  {
    scaled_points_x = applyScaleX (points_x, scaled_points_x);
  }

  protected void scaleYChanged ()
  {
    scaled_points_y = applyScaleY (points_y, scaled_points_y);
  }
}
