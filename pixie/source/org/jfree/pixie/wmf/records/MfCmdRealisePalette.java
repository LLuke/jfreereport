package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * Synchronizes the Metafile-Palette with the device-dependent palette
 * This is not used here, as java does use 24-Bit TrueColors to display colors.
 */
public class MfCmdRealisePalette extends MfCmd
{
  public MfCmdRealisePalette ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    // Not implemented!
  }

  public MfCmd getInstance ()
  {
    return new MfCmdRealisePalette ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("RealizePalette is not yet implemented.");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[REALISE_PALETTE] is not implemented");
    return b.toString ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.REALISE_PALETTE;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
