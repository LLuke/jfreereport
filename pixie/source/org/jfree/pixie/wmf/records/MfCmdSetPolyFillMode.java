package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSetPolyFillMode extends MfCmd
{
  public static final int ALTERNATE = 1;
  public static final int WINDING = 2;

  private int fillmode;

  public MfCmdSetPolyFillMode ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setPolyFillMode (fillmode);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetPolyFillMode ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setFillMode (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_POLY_FILL_MODE;
  }

  public int getFillMode ()
  {
    return fillmode;
  }

  public void setFillMode (int id)
  {
    this.fillmode = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_POLY_FILL_MODE] fillmode=");
    b.append (getFillMode ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
