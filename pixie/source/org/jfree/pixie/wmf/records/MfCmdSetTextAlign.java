package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * The SetTextAlign function sets the text-alignment flags for the
 * specified device context.
 *
 * Specifies the text alignment by using a mask of the values in the
 * following list. Only one flag can be chosen from those that affect
 * horizontal and vertical alignment. In addition, only one of the two
 * flags that alter the current position can be chosen.
 *
 * The default values are TA_LEFT, TA_TOP, and TA_NOUPDATECP
 */
public class MfCmdSetTextAlign extends MfCmd
{
  private int textAlignMode;

  public MfCmdSetTextAlign ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setTextAlign (textAlignMode);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetTextAlign ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setTextAlignMode (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_TEXT_ALIGN;
  }

  public int getTextAlignMode ()
  {
    return textAlignMode;
  }

  public void setTextAlignMode (int id)
  {
    this.textAlignMode = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_TEXT_ALIGN] textAlign=");
    b.append (getTextAlignMode ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
