package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * This function is not in the validity list of Microsofts
 * WindowsMetafile Records. The Function ID is 0x0922, but there is
 * not other description available ...
 */
public class MfCmdBitBlt extends MfCmd
{
  public MfCmdBitBlt ()
  {
  }

  public void replay (final WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdBitBlt ();
  }

  public void setRecord (final MfRecord record)
  {
    System.out.println ("Old BitBlit is not yet implemented.");
  }

  public String toString ()
  {
    final StringBuffer b = new StringBuffer ();
    b.append ("[OLD_BIT_BLT] is not implemented");
    return b.toString ();
  }

  public int getFunction ()
  {
    return MfType.OLD_BIT_BLT;
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
    throw new RecordCreationException("CmdBitBlt is not supported");
  }
}
