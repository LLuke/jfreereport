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

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdUnknownCommand ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println (this);
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    return null;
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

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
