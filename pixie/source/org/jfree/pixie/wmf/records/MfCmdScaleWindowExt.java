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
 * MfCmdScaleWindowExt.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdEscape.java,v 1.3 2003/03/15 17:16:57 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdScaleWindowExt extends MfCmd
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

  public MfCmdScaleWindowExt ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    // ToDo Not yet implemented
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    MfRecord record = new MfRecord(RECORD_SIZE);
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
    return new MfCmdScaleWindowExt ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SCALE_WINDOW_EXT;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SCALE_WINDOW] scaleX=");
    b.append (getXNum ());
    b.append ("/");
    b.append (getXDenom ());
    b.append (" scaley=");
    b.append (getYNum ());
    b.append ("/");
    b.append (getYDenom ());
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
    int yD = record.getParam (0);
    int yN = record.getParam (1);
    int xD = record.getParam (2);
    int xN = record.getParam (3);
    setXScale (xN, xD);
    setYScale (yN, yD);
  }

  public void setXScale (int xNum, int xDenom)
  {
    this.xNum = xNum;
    this.xDenom = xDenom;
  }

  public void setYScale (int yNum, int yDenom)
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
