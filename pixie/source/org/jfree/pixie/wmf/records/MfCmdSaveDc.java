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

  /**
   * Implemented!
   */
  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    file.saveDCState ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSaveDc ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    // Save DC has no parameters
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SAVE_DC;
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
