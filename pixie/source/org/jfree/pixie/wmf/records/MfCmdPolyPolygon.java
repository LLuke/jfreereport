/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------
 * MfCmdPolyPolygon.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdArc.java,v 1.2 2003/03/14 20:06:04 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

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
  private int polycount;

  public MfCmdPolyPolygon ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();

    MfDcState state = file.getCurrentState ();

    for (int i = 0; i < polycount; i++)
    {
      int[] pointsX = getScaledPointsX (i);
      int[] pointsY = getScaledPointsY (i);
      Polygon polygon = new Polygon (pointsX, pointsY, pointsX.length);
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

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdPolyPolygon ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
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

      int[] points_x = getPointsX (p);
      int[] points_y = getPointsY (p);
      int l = points_x.length;

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
   * Reads the command data from the given record and adjusts the internal
   * parameters according to the data parsed.
   * <p>
   * After the raw record was read from the datasource, the record is parsed
   * by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (MfRecord record)
  {
    int numberOfPolygons = record.getParam (0);
    int[] count = new int[numberOfPolygons];
    Object[] poly_points_x = new Object[numberOfPolygons];
    Object[] poly_points_y = new Object[numberOfPolygons];

    int numberOfPointsRead = 0;
    // read the length of each polygon
    for (int i = 0; i < numberOfPolygons; i++)
    {
      int numberOfPointsInPolygon = record.getParam (1 + i);
      count[i] = numberOfPointsInPolygon;

      // Position of the points depends on the number of points
      // of the previous polygons
      int[] points_x = new int[numberOfPointsInPolygon];
      int[] points_y = new int[numberOfPointsInPolygon];
      // read position is after numPolygonPointsRead + noOfPolygons + 1 (for the first parameter)
      int readPos = numberOfPointsRead * 2 + numberOfPolygons + 1;
      for (int j = 0; j < numberOfPointsInPolygon; j++)
      {
        points_x[j] = record.getParam ((readPos + 1) + j * 2);
        points_y[j] = record.getParam ((readPos + 2) + j * 2);
      }
      poly_points_x[i] = points_x;
      poly_points_y[i] = points_y;
      numberOfPointsRead += numberOfPointsInPolygon;
    }
    setPolygonCount (numberOfPolygons);
    setPoints (poly_points_x, poly_points_y);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    int numberOfPolygons = getPolygonCount();
    int pointsTotal = 0;
    for (int i = 0; i < numberOfPolygons; i++)
    {
      pointsTotal += getPointsX(i).length;
    }
    MfRecord record = new MfRecord(1 + numberOfPolygons + pointsTotal * 2);
    record.setParam(0, numberOfPolygons);

    int numberOfPointsRead = 0;
    for (int i = 0; i < numberOfPolygons; i++)
    {
      int[] x_points = getPointsX(i);
      int[] y_points = getPointsY(i);
      int numberOfPointsInPolygon = x_points.length;
      record.setParam(1 + i, numberOfPointsInPolygon);

      int readPos = numberOfPointsRead * 2 + numberOfPolygons + 1;
      for (int j = 0; j < numberOfPointsInPolygon; j++)
      {
        record.setParam ((readPos + 1) + j * 2, x_points[i]);
        record.setParam ((readPos + 2) + j * 2, y_points[i]);
      }
      numberOfPointsRead += numberOfPointsInPolygon;
    }
    return record;
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

  public void setPolygonCount (int count)
  {
    this.polycount = count;
  }

  public int getPolygonCount ()
  {
    return polycount;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
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

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
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
