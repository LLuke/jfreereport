/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * MfCmdResizePalette.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdResizePalette.java,v 1.3 2003/07/03 16:13:36 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * The ResizePalette function increases or decreases the size of a logical
 * palette based on the specified value.
 */
public class MfCmdResizePalette extends MfCmd
{
  private static final int RECORD_SIZE = 1;
  private static final int POS_NEW_PALETTE_SIZE = 0;
  private int paletteSize;

  public MfCmdResizePalette ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    // Not implemented!
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdResizePalette ();
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
    setPaletteSize(record.getParam(POS_NEW_PALETTE_SIZE));
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    MfRecord record = new MfRecord(RECORD_SIZE);
    record.setType(getFunction());
    record.setParam(POS_NEW_PALETTE_SIZE, getPaletteSize());
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
    return MfType.RESIZE_PALETTE;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer ();
    b.append ("[RESIZE_PALETTE] is not implemented");
    return b.toString ();
  }

  /**
   * Not implemented as no scaling needed for this operation.
   */
  protected void scaleXChanged ()
  {
  }

  /**
   * Not implemented as no scaling needed for this operation.
   */
  protected void scaleYChanged ()
  {
  }

  public int getPaletteSize()
  {
    return paletteSize;
  }

  public void setPaletteSize(int paletteSize)
  {
    this.paletteSize = paletteSize;
  }
}
