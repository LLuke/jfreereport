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
 * MfCmdPolyline.java
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
import java.awt.geom.Line2D;

/**
 * The Polyline function draws a series of line segments by connecting
 * the points in the specified array.
 * <p>
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

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
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
      state.postDraw();
    }
    state.setCurPos (cx, cy);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdPolyline ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.POLYLINE;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[POLYLINE] count=");
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

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    MfRecord record = new MfRecord(getPointCount()* 2 + 1);
    int count = getPointCount();
    int[] points_x = getPointsX();
    int[] points_y = getPointsY();

    record.setParam(0, count);

    for (int i = 0; i < count; i++)
    {
      record.setParam(1 + 2 * i, points_x[i]);
      record.setParam(2 + 2 * i, points_y[i]);
    }
    return record;
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

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_points_x = applyScaleX (points_x, scaled_points_x);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_points_y = applyScaleY (points_y, scaled_points_y);
  }
}
