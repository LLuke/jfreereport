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
  private int hPalette;
  private int cEntries;
  private Color[] colors;

  public MfCmdSetPaletteEntries ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    // not yet
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetPaletteEntries ();
  }

  public int getEntriesCount ()
  {
    return cEntries;
  }

  public void setEntriesCount (int cEntries)
  {
    if (cEntries < 0)
      throw new IllegalArgumentException ();
  }

  public Color[] getEntries ()
  {
    return colors;
  }

  public void setEntries (Color[] colors)
  {
    this.colors = colors;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int hPalette = record.getParam (0);
    int cStart = record.getParam (1);
    int cEntries = record.getParam (2);
    Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      int colorRef = record.getInt (i + 3);
      org.jfree.pixie.wmf.GDIColor color = new org.jfree.pixie.wmf.GDIColor (colorRef);
      colors[i] = color;
    }
    setEntriesCount (cEntries);
    setEntries (colors);
    setHPalette (hPalette);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_PALETTE_ENTRIES;
  }

  public int getHPalette ()
  {
    return hPalette;
  }

  public void setHPalette (int hPalette)
  {
    this.hPalette = hPalette;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_PALETTE_ENTRIES] entriesCount=");
    b.append (getEntriesCount ());
    b.append (" hpalette=");
    b.append (hPalette);
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
