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
 * MfCmdSetPaletteEntries.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdSetPaletteEntries.java,v 1.2 2003/03/22 16:02:57 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;

/**
 * As with every palette-function: I'm not sure if this is correctly implemented.
 *
 * The SetPaletteEntries function sets RGB (red, green, blue) color values and
 * flags in a range of entries in a logical palette.
 */
public class MfCmdSetPaletteEntries extends MfCmd
{
  private int BASE_RECORD_SIZE = 3;
  private int POS_H_PALETTE = 0;
  private int POS_CSTART = 1;
  private int POS_CENTRIES = 2;

  private int hPalette;
  private Color[] colors;
  private int startPos;

  public MfCmdSetPaletteEntries ()
  {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (final WmfFile file)
  {
    // not yet
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdSetPaletteEntries ();
  }

  public Color[] getEntries ()
  {
    return colors;
  }

  public void setEntries (final Color[] colors)
  {
    this.colors = colors;
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
    final int hPalette = record.getParam (POS_H_PALETTE);
    final int cStart = record.getParam (POS_CSTART);
    final int cEntries = record.getParam (POS_CENTRIES);
    final Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      final int colorRef = record.getLongParam (2*i + BASE_RECORD_SIZE);
      final GDIColor color = new GDIColor (colorRef);
      colors[i] = color;
    }
    setStartPos(cStart);
    setEntries (colors);
    setHPalette (hPalette);
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    final Color[] cEntries = getEntries();
    if (cEntries == null)
      throw new NullPointerException("No CEntries set");

    final MfRecord record = new MfRecord(2 * cEntries.length + BASE_RECORD_SIZE);
    record.setParam(POS_H_PALETTE, getHPalette());
    record.setParam(POS_CSTART, getStartPos());
    record.setParam(POS_CENTRIES, cEntries.length);

    for (int i = 0; i < cEntries.length; i++)
    {
      record.setLongParam (2*i + BASE_RECORD_SIZE, GDIColor.translateColor(cEntries[i]));
    }
    return record;
  }

  public int getStartPos()
  {
    return startPos;
  }

  public void setStartPos(final int startPos)
  {
    this.startPos = startPos;
  }

  /**
   * Reads the function identifier. Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.SET_PALETTE_ENTRIES;
  }

  public int getHPalette ()
  {
    return hPalette;
  }

  public void setHPalette (final int hPalette)
  {
    this.hPalette = hPalette;
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer ();
    b.append ("[SET_PALETTE_ENTRIES] entriesCount=");
    if (getEntries() == null)
    {
      b.append(0);
    }
    else
    {
      b.append (getEntries ().length);
    }
    b.append (" hpalette=");
    b.append (hPalette);
    return b.toString ();
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged()
  {
  }
}
