package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * This function is not in the validity list of Microsofts
 * WindowsMetafile Records.
 */
public class MfCmdUnknownCommand extends MfCmd
{
  private int function;

  public MfCmdUnknownCommand ()
  {
  }

  public void replay (WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdUnknownCommand ();
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
    throw new RecordCreationException("The {Unknown Command} is not writeable");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[UNKNOWN COMMAND] " + Integer.toHexString (getFunction ()));
    return b.toString ();
  }

  public void setFunction (int function)
  {
    this.function = function;
  }

  public int getFunction ()
  {
    return function;
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleXChanged()
  {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the
   * internal coordinate values have to be adjusted.
   */
  protected void scaleYChanged()
  {
  }
}
