package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Color;

/**
 * The AnimatePalette function replaces entries in the specified logical palette.
 *
 * BOOL AnimatePalette(
 *  HPALETTE hpal,           // handle to logical palette
 *  UINT iStartIndex,        // first entry in logical palette
 *  UINT cEntries,           // number of entries
 *  CONST PALETTEENTRY *ppe  // first replacement
 * );
 *
 * I don't know if this this is correctly implemented or what it does at all.
 * Expect the worst.
 */
public class MfCmdAnimatePalette extends MfCmd
{
  private int hPalette;
  private int cEntries;
  private Color[] colors;

  private static final int POS_HPALETTE = 0; // ?
  private static final int POS_CENTRIES = 2; // ?

  public MfCmdAnimatePalette ()
  {
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

  /** Writer function */
  public MfRecord getRecord ()
  {
    int cEntries = getEntriesCount();
    MfRecord record = new MfRecord(2 + cEntries * 2);
    record.setParam(0, getHPalette());
    record.setParam(1, cEntries);

    Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      Color c = colors[i];
      record.setLongParam(i + 2, GDIColor.translateColor(c));
    }
    return record;
  }

  public void setRecord (MfRecord record)
  {
    int hPalette = record.getParam (0);
    int cEntries = record.getParam (1);
    Color[] colors = new Color[cEntries];

    for (int i = 0; i < cEntries; i++)
    {
      int cr = record.getLongParam(i + 2);
      GDIColor color = new GDIColor (cr);
      colors[i] = color;
    }
    setEntriesCount (cEntries);
    setEntries (colors);
  }

  public int getFunction ()
  {
    return MfType.ANIMATE_PALETTE;
  }

  public void replay (WmfFile file)
  {
    // do nothing
    System.out.println ("Animate Palette is not implemented");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[ANIMATE_PALETTE] hPalette=");
    b.append (getHPalette ());
    b.append (" entriesCount=");
    b.append (getEntriesCount ());
    return b.toString ();
  }

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

  /** No scaling needed */
  protected void scaleXChanged ()
  {
  }

  /** No scaling needed */
  protected void scaleYChanged ()
  {
  }
}
