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
 * MfCmdAnimatePalette.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BrushConstants.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
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
 * The AnimatePalette function replaces entries in the specified logical palette.
 *
 * <pre>
 * BOOL AnimatePalette(
 *  HPALETTE hpal,           // handle to logical palette
 *  UINT iStartIndex,        // first entry in logical palette
 *  UINT cEntries,           // number of entries
 *  CONST PALETTEENTRY *ppe  // first replacement
 * );
 * </pre>
 *
 * This function is not implemented. However, you can use this implementation
 * to create a valid record.
 */
public class MfCmdAnimatePalette extends MfCmd
{
  private int hPalette;
  private Color[] colors;

  private static final int POS_HPALETTE = 0;
  private static final int POS_CENTRIES = 1;
  private static final int POS_START_ENTRIES = 2;

  /**
   * DefaultConstructor.
   */
  public MfCmdAnimatePalette ()
  {
  }

  /**
   * Returns the number of colors defined for the AnimatePalette command.
   *
   * @return the number of colors or 0 if no colors are defined.
   */
  public int getEntriesCount ()
  {
    if (colors == null)
      return 0;

    return colors.length;
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord () throws RecordCreationException
  {
    int cEntries = getEntriesCount();
    if (cEntries == 0)
      throw new RecordCreationException("Empty AnimatePaletteRecord is not valid");

    MfRecord record = new MfRecord(2 + cEntries * 2);
    record.setParam(POS_HPALETTE, getHPalette());
    record.setParam(POS_CENTRIES, cEntries);

    Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      Color c = colors[i];
      // a long parameter is 2 words long
      record.setLongParam(i * 2 + POS_START_ENTRIES, GDIColor.translateColor(c));
    }
    return record;
  }

  /**
   * Reads the command data from the given record and adjusts the internal
   * parameters according to the data parsed.
   * <p>
   * This method is not implemented, as a Palette implementation is still missing.
   *
   * @param record the record.
   */
  public void setRecord (MfRecord record)
  {
    // the handle to the palette object
    int hPalette = record.getParam (POS_HPALETTE);
    // the number of defined entries ...
    int cEntries = record.getParam (POS_CENTRIES);
    Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      int cr = record.getLongParam(i * 2 + POS_START_ENTRIES);
      GDIColor color = new GDIColor (cr);
      colors[i] = color;
    }
    setEntries (colors);
  }

  /**
   * Reads the function identifiert .Every record type is identified by a
   * function number corresponding to one of the Windows GDI functions used.
   *
   * @return MfType.ANIMATE_PALETTE.
   */
  public int getFunction ()
  {
    return MfType.ANIMATE_PALETTE;
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    // do nothing
    System.out.println ("Animate Palette is not implemented");
  }

  /**
   * Returns a string representation of this command.
   *
   * @return the command as string.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[ANIMATE_PALETTE] hPalette=");
    b.append (getHPalette ());
    b.append (" entriesCount=");
    b.append (getEntriesCount ());
    return b.toString ();
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance ()
  {
    return new MfCmdAnimatePalette ();
  }

  public int getHPalette ()
  {
    return hPalette;
  }

  public void setHPalette (int hPalette)
  {
    this.hPalette = hPalette;
  }

  public Color[] getEntries ()
  {
    return colors;
  }

  public void setEntries (Color[] colors)
  {
    this.colors = colors;
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
}
