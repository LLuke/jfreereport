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
 * MfCmdCreateDibPatternBrush.java
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

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.bitmap.DIBReader;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this records is ignored.
// todo ...

public class MfCmdCreateDibPatternBrush extends MfCmd
{
  private static final int POS_BITMAP_TYPE = 0;
  private static final int POS_PALETTE_TYPE = 1;
  private static final int POS_BITMAP_INFO_STRUCT = 2;
  // bitmap data follows the bitmap header ...
  
  private BufferedImage image;

  public MfCmdCreateDibPatternBrush ()
  {
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
          throws RecordCreationException
  {
    /**
     * Requires a DIB-Writer, this is not yet supported
     */
    throw new RecordCreationException("CreateDIBPatternBrush is not yet supported");
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
    try
    {
      final DIBReader reader = new DIBReader();
      setImage(reader.setRecord(record));
    }
    catch (IOException ioe)
    {
      // failed to load the bitmap ..
    }
  }

  public BufferedImage getImage ()
  {
    return image;
  }

  public void setImage (final BufferedImage image)
  {
    if (image == null)
    {
      throw new NullPointerException();
    }
    this.image = image;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[CREATE_DIB_PATTERN_BRUSH] ");
    b.append(" no internals known ");
    return b.toString();
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
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
  public void replay (final WmfFile file)
  {
    final MfLogBrush lbrush = new MfLogBrush();
    lbrush.setStyle(MfLogBrush.BS_DIBPATTERN);
    lbrush.setBitmap(image);

    file.getCurrentState().setLogBrush(lbrush);
    file.storeObject(lbrush);
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreateDibPatternBrush();
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
