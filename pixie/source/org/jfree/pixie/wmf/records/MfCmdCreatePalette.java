package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * Palette function not supported
 */
public class MfCmdCreatePalette extends MfCmd
{
  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("Create Palette is not implemented.");
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.CREATE_PALETTE;
  }

  public MfCmdCreatePalette ()
  {
  }

  /**
   * Writer function
   *
   * Need to implement a dummy
   */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    return null;
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogPalette pal = new org.jfree.pixie.wmf.MfLogPalette ();
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
