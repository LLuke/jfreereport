package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfLogPalette;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * Palette function not supported
 */
public class MfCmdCreatePalette extends MfCmd
{
  public void setRecord (MfRecord record)
  {
    System.out.println ("Create Palette is not implemented.");
  }

  public int getFunction ()
  {
    return MfType.CREATE_PALETTE;
  }

  public MfCmdCreatePalette ()
  {
  }

  public void replay (WmfFile file)
  {
    MfLogPalette pal = new MfLogPalette ();
    file.getCurrentState ().setLogPalette (pal);
    file.storeObject (pal);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_PALETTE] ");
    b.append (" no internals known ");
    return b.toString ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdCreatePalette ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
