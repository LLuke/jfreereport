package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * This function is not in the validity list of Microsofts
 * WindowsMetafile Records.
 */
public class MfCmdOldStrechBlt extends MfCmd
{
  public MfCmdOldStrechBlt ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdOldStrechBlt ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("Old StretchBlt is not yet implemented.");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OLD_STRECH_BLT] is not implemented");
    return b.toString ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.OLD_STRETCH_BLT;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
