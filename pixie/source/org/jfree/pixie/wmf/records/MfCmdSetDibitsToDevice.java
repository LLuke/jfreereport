package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this records is ignored.

public class MfCmdSetDibitsToDevice extends MfCmd
{
  public MfCmdSetDibitsToDevice ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetDibitsToDevice ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("SetDibitsToDevice is not implemented.");

  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_DIBITS_TO_DEVICE] <<windows specific, will not be implemented>>");
    return b.toString ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_DIBITS_TO_DEVICE;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
