package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Graphics2D;
import java.awt.Polygon;

public class MfCmdPolygon extends MfCmd
{
  private int[] points_x;
  private int[] points_y;
  private int[] scaled_points_x;
  private int[] scaled_points_y;
  private int count;

  public MfCmdPolygon ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();

    Polygon polygon = new Polygon (getScaledPointsX (), getScaledPointsY (), getPointCount ());
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();

    if (state.getLogBrush ().isVisible ())
    {
      state.preparePaint ();
      graph.fill (polygon);
      state.postPaint ();
    }
    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (polygon);
      state.postDraw ();
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdPolygon ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.POLYGON;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[POLYGON] count=");
    b.append (getPointCount ());
    int l = getPointCount ();
    int[] points_x = getPointsX ();
    int[] points_y = getPointsY ();

    for (int i = 0; i < l; i++)
    {
      if (i != 0) b.append (",");

      b.append (" (");
      b.append (points_x[i]);
      b.append (",");
      b.append (points_y[i]);
      b.append (") ");
    }
    return b.toString ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int count = record.getParam (0);
    int[] points_x = new int[count];
    int[] points_y = new int[count];

    for (int i = 0; i < count; i++)
    {
      points_x[i] = record.getParam (1 + 2 * i);
      points_y[i] = record.getParam (2 + 2 * i);
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

  public int[] getScaledPointsX ()
  {
    return scaled_points_x;
  }

  public int[] getScaledPointsY ()
  {
    return scaled_points_y;
  }

  public int getPointCount ()
  {
    return count;
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
