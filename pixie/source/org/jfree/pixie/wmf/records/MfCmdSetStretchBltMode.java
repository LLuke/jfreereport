package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSetStretchBltMode extends MfCmd
{
  private int stretchmode;

  public MfCmdSetStretchBltMode ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setStretchBltMode (stretchmode);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetStretchBltMode ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setStretchMode (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_STRETCH_BLT_MODE;
  }

  public int getStretchMode ()
  {
    return stretchmode;
  }

  public void setStretchMode (int id)
  {
    this.stretchmode = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_STRECH_BLT_MODE] stretchmode=");
    b.append (getStretchMode ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
