package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSaveDc extends MfCmd
{
  public MfCmdSaveDc ()
  {
  }

  public void replay (WmfFile file)
  {
    file.saveDCState ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSaveDc ();
  }

  public void setRecord (MfRecord record)
  {
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord() throws RecordCreationException
  {
    return new MfRecord(0);
  }

  public int getFunction ()
  {
    return MfType.SAVE_DC;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SAVE_DC]");
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
