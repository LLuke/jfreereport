package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSetTextCharExtra extends MfCmd
{
  private int textCharExtra;

  public MfCmdSetTextCharExtra ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setTextCharExtra (textCharExtra);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetTextCharExtra ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setTextCharExtra (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_TEXT_CHAR_EXTRA;
  }

  public int getTextCharExtra ()
  {
    return textCharExtra;
  }

  public void setTextCharExtra (int id)
  {
    this.textCharExtra = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_TEXT_CHAR_EXTRA] textCharExtra=");
    b.append (getTextCharExtra ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
