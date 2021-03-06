/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * MfCmdFloodFill.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.pixie.wmf.records;

import java.awt.Color;
import java.awt.Point;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * The FloodFill function fills an area of the display surface with the current brush. The
 * area is assumed to be bounded as specified by the crFill parameter.
 */
public class MfCmdFloodFill extends MfCmd
{
  private static final int RECORD_SIZE = 4;
  private static final int POS_COLOR = 0;
  private static final int POS_Y = 2;
  private static final int POS_X = 3;

  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;
  private Color color;

  public MfCmdFloodFill ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    // there is no way of implementing a flood fill operation for Graphics2D.
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdFloodFill();
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters
   * according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the
   * concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord (final MfRecord record)
  {
    final int c = record.getLongParam(POS_COLOR);
    final Color color = new GDIColor(c);
    final int y = record.getParam(POS_Y);
    final int x = record.getParam(POS_X);
    setTarget(x, y);
    setColor(color);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setLongParam(POS_COLOR, GDIColor.translateColor(getColor()));
    final Point target = getTarget();
    record.setParam(POS_Y, (int) target.getY());
    record.setParam(POS_X, (int) target.getX());
    return record;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[FLOOD_FILL] color=");
    b.append(getColor());
    b.append(" target=");
    b.append(getTarget());
    return b.toString();
  }

  public Point getTarget ()
  {
    return new Point(x, y);
  }

  public Point getScaledTarget ()
  {
    return new Point(scaled_x, scaled_y);
  }

  public void setTarget (final Point point)
  {
    setTarget(point.x, point.y);
  }

  public void setTarget (final int x, final int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged();
    scaleYChanged();
  }

  public void setColor (final Color c)
  {
    this.color = c;
  }

  public Color getColor ()
  {
    return color;
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.FLOOD_FILL;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
  }
}
