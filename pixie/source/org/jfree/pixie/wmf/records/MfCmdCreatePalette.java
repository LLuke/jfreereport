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
 * MfCmdCreatePalette.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: MfCmdCreatePalette.java,v 1.5 2004/01/19 18:36:25 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf.records;

import java.awt.Color;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Palette function not supported
 */
public class MfCmdCreatePalette extends MfCmd
{
  private static final int POS_HPALETTE = 0;
  private static final int POS_CENTRIES = 1;
  private static final int POS_START_ENTRIES = 2;

  private int hPalette;
  private Color[] colors;

  public MfCmdCreatePalette ()
  {
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number
   * corresponding to one of the Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction ()
  {
    return MfType.CREATE_PALETTE;
  }

  /**
   * Returns the number of colors defined for the AnimatePalette command.
   *
   * @return the number of colors or 0 if no colors are defined.
   */
  public int getEntriesCount ()
  {
    if (colors == null)
    {
      return 0;
    }

    return colors.length;
  }

  /**
   * Creates a new record based on the data stored in the MfCommand. <i>This function may
   * or may not work, there is not much HQ documentation about metafiles available in the
   * net. </i>
   *
   * @return the created record.
   */
  public MfRecord getRecord ()
          throws RecordCreationException
  {
    final int cEntries = getEntriesCount();
    if (cEntries == 0)
    {
      throw new RecordCreationException("Empty CreatePaletteRecord is not valid");
    }

    final MfRecord record = new MfRecord(2 + cEntries * 2);
    record.setParam(POS_HPALETTE, getHPalette());
    record.setParam(POS_CENTRIES, cEntries);

    final Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      final Color c = colors[i];
      // a long parameter is 2 words long
      record.setLongParam(i * 2 + POS_START_ENTRIES, GDIColor.translateColor(c));
    }
    return record;
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters
   * according to the data parsed.
   * <p/>
   * This method is not implemented, as a Palette implementation is still missing.
   *
   * @param record the record.
   */
  public void setRecord (final MfRecord record)
  {
    // the handle to the palette object ignored
    final int hPalette = record.getParam(POS_HPALETTE);
    setHPalette(hPalette);
    // the number of defined entries ...
    final int cEntries = record.getParam(POS_CENTRIES);
    final Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      final int cr = record.getLongParam(i * 2 + POS_START_ENTRIES);
      final GDIColor color = new GDIColor(cr);
      colors[i] = color;
    }
    setEntries(colors);
  }

  public void replay (final WmfFile file)
  {
    // no real implementation, as palettes are not yet fully supported ...
    final MfLogPalette pal = new MfLogPalette();
    file.getCurrentState().setLogPalette(pal);
    file.storeObject(pal);
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("[CREATE_PALETTE] ");
    b.append(" no internals known ");
    return b.toString();
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdCreatePalette();
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

  public int getHPalette ()
  {
    return hPalette;
  }

  public void setHPalette (final int hPalette)
  {
    this.hPalette = hPalette;
  }

  public Color[] getEntries ()
  {
    return colors;
  }

  public void setEntries (final Color[] colors)
  {
    this.colors = colors;
  }

}
