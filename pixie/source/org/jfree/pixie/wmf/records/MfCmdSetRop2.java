package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSetRop2 extends MfCmd
{
  private int drawmode;

  public MfCmdSetRop2 ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setROP (drawmode);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetRop2 ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setDrawMode (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_ROP2;
  }

  public int getDrawMode ()
  {
    return drawmode;
  }

  public void setDrawMode (int id)
  {
    this.drawmode = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_ROP2] drawmode=");
    b.append (getDrawMode ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
