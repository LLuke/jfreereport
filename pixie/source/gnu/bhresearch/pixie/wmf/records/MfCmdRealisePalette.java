package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * Synchronizes the Metafile-Palette with the device-dependent palette
 * This is not used here, as java does use 24-Bit TrueColors to display colors.
 */
public class MfCmdRealisePalette extends MfCmd
{
  public MfCmdRealisePalette ()
  {
  }

  public void replay (WmfFile file)
  {
    // Not implemented!
  }

  public MfCmd getInstance ()
  {
    return new MfCmdRealisePalette ();
  }

  public void setRecord (MfRecord record)
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
    return MfType.REALISE_PALETTE;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
