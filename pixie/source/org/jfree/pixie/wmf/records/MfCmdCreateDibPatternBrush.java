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
 * MfCmdCreateDibPatternBrush.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmd.java,v 1.1 2003/03/09 20:38:23 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;
import org.jfree.pixie.wmf.bitmap.DIBReader;

import java.awt.image.BufferedImage;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this records is ignored.
public class MfCmdCreateDibPatternBrush extends MfCmd
{
  private BufferedImage image;

  public MfCmdCreateDibPatternBrush ()
  {
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord () throws RecordCreationException
  {
    /**
     * Requires a DIB-Writer, this is not yet supported
     */
    throw new RecordCreationException ("CreateDIBPatternBrush is not yet supported");
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
    try
    {
      DIBReader reader = new DIBReader ();
      setImage(reader.setRecord (record));
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
  }

  public BufferedImage getImage ()
  {
    return image;
  }

  public void setImage (BufferedImage image)
  {
    if (image == null) throw new NullPointerException();
    this.image = image;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_DIB_PATTERN_BRUSH] ");
    b.append (" no internals known ");
    return b.toString ();
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.CREATE_DIB_PATTERN_BRUSH;
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    MfLogBrush lbrush = new MfLogBrush ();
    lbrush.setStyle (MfLogBrush.BS_DIBPATTERN);
    lbrush.setBitmap (image);

    file.getCurrentState ().setLogBrush (lbrush);
    file.storeObject (lbrush);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreateDibPatternBrush ();
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted. Not used here.
   */
  protected void scaleXChanged ()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted. Not used here.
   */
  protected void scaleYChanged ()
  {
  }

}
