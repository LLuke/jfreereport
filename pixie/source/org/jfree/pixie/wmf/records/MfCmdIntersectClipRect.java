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
 * MfCmdIntersectClipRect.java
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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * top, left, right and bottom define the points of the clipping region, the resultant
 * clipping region is the intersection of this region and the original region.
 */
public class MfCmdIntersectClipRect extends MfCmd
{
  private static final int RECORD_SIZE = 4;
  private static final int POS_BOTTOM = 3;
  private static final int POS_RIGHT = 2;
  private static final int POS_TOP = 1;
  private static final int POS_LEFT = 0;

  private int x;
  private int y;
  private int width;
  private int height;

  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  public MfCmdIntersectClipRect ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final MfDcState state = file.getCurrentState();
    final Rectangle rect = state.getClipRegion();
    final Rectangle2D rec2 = rect.createIntersection(getScaledIntersectClipRect());
    state.setClipRegion(new Rectangle((int) rec2.getX(),
            (int) rec2.getY(),
            (int) rec2.getWidth(),
            (int) rec2.getHeight()));
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdIntersectClipRect();
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.INTERSECT_CLIP_RECT;
  }

  public Rectangle getIntersectClipRect ()
  {
    return new Rectangle(x, y, width, height);
  }

  public Rectangle getScaledIntersectClipRect ()
  {
    return new Rectangle(scaled_x, scaled_y, scaled_width, scaled_height);
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[INTERSECT_CLIP_RECT] bounds=");
    b.append(getIntersectClipRect());
    return b.toString();
  }

  public void setIntersectClipRect (final int x, final int y, final int width,
                                    final int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaleXChanged();
    scaleYChanged();
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
    final int bottom = record.getParam(POS_BOTTOM);
    final int right = record.getParam(POS_RIGHT);
    final int top = record.getParam(POS_TOP);
    final int left = record.getParam(POS_LEFT);
    setIntersectClipRect(left, top, right - left, bottom - top);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
  {
    final Rectangle rc = getIntersectClipRect();
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_BOTTOM, (int) (rc.getY() + rc.getHeight()));
    record.setParam(POS_RIGHT, (int) (rc.getX() + rc.getWidth()));
    record.setParam(POS_TOP, (int) (rc.getY()));
    record.setParam(POS_LEFT, (int) (rc.getX()));
    return record;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
    scaled_x = getScaledX(x);
    scaled_width = getScaledX(width);
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
    scaled_y = getScaledY(y);
    scaled_height = getScaledY(height);
  }
}
