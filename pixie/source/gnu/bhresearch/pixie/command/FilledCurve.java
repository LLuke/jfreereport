package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;

public class FilledCurve extends PixieImageCommand
{
  private int[] points_x;
  private int[] points_y;
  private int cleanCnt;
  
  private int[] scaled_x;
  private int[] scaled_y;
  private int maxX;
  private int maxY;  
  
  public FilledCurve (PixieDataInput in)
    throws IOException
  {
    // Number of handles.
    int hCnt = in.readUnsignedVInt();

    // Read types array. A run length encoded array of boolean.
    boolean ht[] = new boolean[ hCnt ];
    int outCnt = 0;
    boolean isBezier = false;
    
    for (int h = 0; h < hCnt; )
    {
      int runLen = in.readUnsignedVInt();
      outCnt += isBezier ? runLen*in.STEPS : runLen;
      while (runLen-- > 0)
      {
        ht[h++] = isBezier;
      }
      isBezier = !isBezier;
    }

    // Read points and expand beziers.
    points_x = in.readPointsX( outCnt, ht);
    points_y = in.readPointsY( outCnt, ht);

    // Remove duplicates.
    cleanCnt = 1;
    for (int i = 1; i < outCnt; i++)
    {
      if ((points_x[cleanCnt-1] != points_x[i]) || 
          (points_y[cleanCnt-1] != points_y[i]))
      {
        points_x[cleanCnt] = points_x[i];
        points_y[cleanCnt] = points_y[i];
        cleanCnt++;
      }
    }
    for (int i = 0;i < cleanCnt; i++)
    {
      if (maxX < points_x[i])
        maxX = points_x[i];
      if (maxY < points_y[i])
        maxY = points_y[i];
    }

    scaleXChanged ();
    scaleYChanged ();
  }

  public FilledCurve (int[] x, int[] y)
  {
    if (x.length != y.length) throw new IllegalArgumentException ("X != Y");
    points_x = x;
    points_y = y;
    
    cleanCnt = x.length;
    for (int i = 0;i < cleanCnt; i++)
    {
      if (maxX < points_x[i])
        maxX = points_x[i];
      if (maxY < points_y[i])
        maxY = points_y[i];
    }
    scaleXChanged ();
    scaleYChanged ();
  }
  
  public void paint (Graphics graphics)
  {
    graphics.fillPolygon( scaled_x, scaled_y, cleanCnt );
  }
  
  protected void scaleXChanged ()
  {
    scaled_x = applyScaleX (points_x, scaled_x);
  }
  
  protected void scaleYChanged ()
  {
    scaled_x = applyScaleY (points_y, scaled_y);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("FilledCurve={");
    for (int i = 0; i < points_x.length; i++)
    {
      b.append ("(");
      b.append (points_x[i]);
      b.append (",");
      b.append (points_y[i]);
      b.append (")");
    }
    b.append ("}");
    return b.toString ();
  }
  public int getWidth ()
  {
    return maxX;
  }
  
  public int getHeight ()
  {
    return maxY;
  }
}

