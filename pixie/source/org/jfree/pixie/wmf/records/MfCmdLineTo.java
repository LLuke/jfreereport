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
 * MfCmdLineTo.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdLineTo.java,v 1.2 2003/03/15 17:16:57 taqua Exp $
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
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * The LineTo function draws a line from the current position up to,
 * but not including, the specified point.
 * The cursor is set to the destination after drawing.
 */
public class MfCmdLineTo extends MfCmd
{
  private static final int RECORD_SIZE = 2;
  private static final int POS_X = 1;
  private static final int POS_Y = 0;

  private int destX;
  private int destY;
  private int scaled_destX;
  private int scaled_destY;

  public MfCmdLineTo ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final Graphics2D graph = file.getGraphics2D ();
    final MfDcState state = file.getCurrentState ();

    final int cx = state.getCurPosX ();
    final int cy = state.getCurPosY ();

    final Point destP = getScaledDestination ();

    if (state.getLogPen ().isVisible ())
    {
      state.prepareDraw ();
      graph.draw (new Line2D.Double (cx, cy, destP.x, destP.y));
      state.postDraw ();

    }
    state.setCurPos (destP.x, destP.y);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdLineTo ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.LINE_TO;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer ();
    b.append ("[LINE_TO] destination=");
    b.append (getDestination ());
    return b.toString ();
  }

  public void setDestination (final Point p)
  {
    setDestination(p.x, p.y);
  }

  public void setDestination (final int x, final int y)
  {
    destX = x;
    destY = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getDestination ()
  {
    return new Point (destX, destY);
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_destX, scaled_destY);
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
  public void setRecord (final MfRecord record)
  {
    final int y = record.getParam (POS_Y);
    final int x = record.getParam (POS_X);
    setDestination (x, y);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final Point dest = getDestination();
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_Y, dest.y);
    record.setParam(POS_X, dest.x);
    return record;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_destX = getScaledX (destX);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_destY = getScaledY (destY);
  }
}
