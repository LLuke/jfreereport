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
 * MfCmdSelectObject.java
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

import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogFont;
import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfLogPen;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.WmfObject;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * Activates the specified Object. The object must be previously defined
 * for the device context by using the correct create*() method.
 */
public class MfCmdSelectObject extends MfCmd
{
  private static final int RECORD_SIZE = 1;
  private static final int POS_OBJECT_ID = 0;

  private int objectId;

  public MfCmdSelectObject ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    WmfObject object = file.getObject (objectId);
    if (object == null)
    {
      throw new NullPointerException ("Object " + objectId + " is not defined");
    }
    switch (object.getType ())
    {
      case WmfObject.OBJ_BRUSH:
        file.getCurrentState ().setLogBrush ((MfLogBrush) object);
        break;
      case WmfObject.OBJ_FONT:
        file.getCurrentState ().setLogFont ((MfLogFont) object);
        break;
      case WmfObject.OBJ_PALETTE:
        file.getCurrentState ().setLogPalette ((MfLogPalette) object);
        break;
      case WmfObject.OBJ_PEN:
        file.getCurrentState ().setLogPen ((MfLogPen) object);
        break;
      case WmfObject.OBJ_REGION:
        file.getCurrentState ().setLogRegion ((MfLogRegion) object);
        break;
    }
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdSelectObject ();
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
    int id = record.getParam (POS_OBJECT_ID);
    setObjectId (id);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    MfRecord record = new MfRecord(RECORD_SIZE);
    record.setParam(POS_OBJECT_ID, getObjectId());
    return record;
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SELECT_OBJECT;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public void setObjectId (int id)
  {
    this.objectId = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SELECT_OBJECT] object=");
    b.append (getObjectId ());
    return b.toString ();
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
