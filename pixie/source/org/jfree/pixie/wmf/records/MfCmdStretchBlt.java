package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * This function is not in the validity list of Microsofts
 * WindowsMetafile Records.
 */
public class MfCmdStretchBlt extends MfCmd
{
  public MfCmdStretchBlt ()
  {
  }

  public void replay (WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdStretchBlt ();
  }

  public void setRecord (MfRecord record)
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
    return MfType.OLD_STRETCH_BLT;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    return null;
  }
}
