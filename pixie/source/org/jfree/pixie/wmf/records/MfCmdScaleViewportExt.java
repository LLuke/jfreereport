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
 * MfCmdScaleViewportExt.java
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

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Scales the current view port.
 */
public class MfCmdScaleViewportExt extends MfCmd
{
  private static final int RECORD_SIZE = 4;
  private static final int POS_Y_DENOM = 0;
  private static final int POS_X_DENOM = 2;
  private static final int POS_Y_NUM = 1;
  private static final int POS_X_NUM = 3;

  private int yNum;
  private int yDenom;
  private int xNum;
  private int xDenom;

  public MfCmdScaleViewportExt ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    file.getGraphics2D().scale
            ((double) getXNum() / (double) getXDenom(),
                    (double) getYNum() / (double) getYDenom());
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
          throws RecordCreationException
  {
    final MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_X_DENOM, getXDenom());
    record.setParam(POS_X_NUM, getXNum());
    record.setParam(POS_Y_DENOM, getYDenom());
    record.setParam(POS_Y_NUM, getYNum());
    return record;
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdScaleViewportExt();
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SCALE_VIEWPORT_EXT;
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
    final int yD = record.getParam(POS_Y_DENOM);
    final int yN = record.getParam(POS_Y_NUM);
    final int xD = record.getParam(POS_X_DENOM);
    final int xN = record.getParam(POS_X_NUM);
    setXScale(xN, xD);
    setYScale(yN, yD);
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[SCALE_VIEWPORT] scaleX=");
    b.append(getXNum());
    b.append("/");
    b.append(getXDenom());
    b.append(" scaley=");
    b.append(getYNum());
    b.append("/");
    b.append(getYDenom());
    return b.toString();
  }

  public void setXScale (final int xNum, final int xDenom)
  {
    this.xNum = xNum;
    this.xDenom = xDenom;
  }

  public void setYScale (final int yNum, final int yDenom)
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

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged ()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged ()
  {
  }
}
