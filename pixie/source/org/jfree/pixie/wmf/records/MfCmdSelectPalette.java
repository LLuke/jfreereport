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
 * MfCmdSelectPalette.java
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

import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.WmfObject;

/**
 * Activates the specified Palette Object. The object must be previously defined for the
 * device context by using createPalette().
 */
public class MfCmdSelectPalette extends MfCmd
{
  private static final int RECORD_SIZE = 1;
  private static final int POS_OBJECT_ID = 0;

  private int objectId;

  public MfCmdSelectPalette ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    final WmfObject object = file.getObject(objectId);
    if (object == null)
    {
      throw new NullPointerException();
    }

    switch (object.getType())
    {
      case WmfObject.OBJ_PALETTE:
        file.getCurrentState().setLogPalette((MfLogPalette) object);
        break;
      default:
        throw new IllegalStateException("Object is no palette");
    }

  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdSelectPalette();
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
    final int id = record.getParam(POS_OBJECT_ID);
    setObjectId(id);
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
    record.setParam(POS_OBJECT_ID, getObjectId());
    return record;
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SELECT_PALETTE;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public void setObjectId (final int id)
  {
    this.objectId = id;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[SELECT_PALETTE] object=");
    b.append(getObjectId());
    return b.toString();
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
