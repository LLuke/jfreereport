package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSetBkMode extends MfCmd
{

  private int bkmode;

  public MfCmdSetBkMode ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setBkMode (bkmode);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetBkMode ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setBkMode (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_BK_MODE;
  }

  public int getBkMode ()
  {
    return bkmode;
  }

  public void setBkMode (int id)
  {
    this.bkmode = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_BK_MODE] bkmode=");
    b.append (getBkMode ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
