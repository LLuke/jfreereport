package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * PolyPolygon, is a list of polygons, for filled polygons
 * SetPolyFillMode affects how the polygon is filled.
 * the number of polygons is recorded, followed by the number of
 * points in each polygon, and then a long sequence of all the points
 * of all the polygons.
 */
public class MfCmdPolyPolygon extends MfCmd
{
  private Object[] points_x; // contains int[]
  private Object[] points_y; // contains int[]
  private Object[] scaled_points_x; // contains int[]
  private Object[] scaled_points_y; // contains int[]
  private int[] count;
  private int polycount;

  public MfCmdPolyPolygon ()
  {
  }

  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();

    MfDcState state = file.getCurrentState ();

    for (int i = 0; i < polycount; i++)
    {
      Polygon polygon = new Polygon (getScaledPointsX (i), getScaledPointsY (i), getPointCount (i));
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
  }

  public MfCmd getInstance ()
  {
    return new MfCmdPolyPolygon ();
  }

  public int getFunction ()
  {
    return MfType.POLY_POLYGON;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[POLYPOLYGON] polycount=");
    b.append (getPolygonCount ());
    b.append ("\n");
    for (int p = 0; p < getPolygonCount (); p++)
    {
      b.append ("  Polygon ");
      b.append (p);

      int l = getPointCount (p);
      int[] points_x = getPointsX (p);
      int[] points_y = getPointsY (p);

      for (int i = 0; i < l; i++)
      {
        if (i != 0) b.append (",");

        b.append (" (");
        b.append (points_x[i]);
        b.append (",");
        b.append (points_y[i]);
        b.append (") ");
      }
      b.append ("\n");
    }
    return b.toString ();
  }

  /**
   * Is not correct!
   *
   */
  public void setRecord (MfRecord record)
  {
    int polycount = record.getParam (0);
    int[] count = new int[polycount];
    Object[] poly_points_x = new Object[polycount];
    Object[] poly_points_y = new Object[polycount];

    int pointPos = 0;
    // read the length of each polygon
    for (int i = 0; i < polycount; i++)
    {
      int currentcount = record.getParam (1 + i);
      count[i] = currentcount;

      // Position of the points depends on the number of points
      // of the previous polygons
      int[] points_x = new int[currentcount];
      int[] points_y = new int[currentcount];
      int readPos = pointPos * 2 + polycount;
      for (int j = 0; j < currentcount; j++)
      {
        points_x[j] = record.getParam ((readPos + 1) + j * 2);
        points_y[j] = record.getParam ((readPos + 2) + j * 2);
      }
      poly_points_x[i] = points_x;
      poly_points_y[i] = points_y;
      pointPos += currentcount;
    }
    setPolygonCount (polycount);
    setPointCount (count);
    setPoints (poly_points_x, poly_points_y);
  }

  public void setPointCount (int[] count)
  {
    this.count = count;
  }

  public void setPoints (Object[] points_x, Object[] points_y)
  {
    this.points_x = points_x;
    this.points_y = points_y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public int[] getPointsX (int polygon)
  {
    return (int[]) points_x[polygon];
  }

  public int[] getPointsY (int polygon)
  {
    return (int[]) points_y[polygon];
  }

  public int[] getScaledPointsX (int polygon)
  {
    return (int[]) scaled_points_x[polygon];
  }

  public int[] getScaledPointsY (int polygon)
  {
    return (int[]) scaled_points_y[polygon];
  }

  public int getPointCount (int polygon)
  {
    return count[polygon];
  }

  public void setPolygonCount (int count)
  {
    this.polycount = count;
  }

  public int getPolygonCount ()
  {
    return polycount;
  }

  protected void scaleXChanged ()
  {
    if (scaled_points_x == null)
      scaled_points_x = new Object[points_x.length];
    if (scaled_points_x.length < points_x.length)
      scaled_points_x = new Object[points_x.length];

    for (int i = 0; i < polycount; i++)
    {
      scaled_points_x[i] = applyScaleX ((int[]) points_x[i], (int[]) scaled_points_x[i]);
    }
  }

  protected void scaleYChanged ()
  {
    if (scaled_points_y == null)
      scaled_points_y = new Object[points_y.length];
    if (scaled_points_y.length < points_y.length)
      scaled_points_y = new Object[points_y.length];

    for (int i = 0; i < polycount; i++)
    {
      scaled_points_y[i] = applyScaleY ((int[]) points_y[i], (int[]) scaled_points_y[i]);
    }
  }

}
