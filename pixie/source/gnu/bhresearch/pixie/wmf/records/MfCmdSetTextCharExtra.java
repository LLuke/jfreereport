package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetTextCharExtra extends MfCmd
{
  private int textCharExtra;

  public MfCmdSetTextCharExtra ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setTextCharExtra (textCharExtra);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetTextCharExtra ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setTextCharExtra (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_TEXT_CHAR_EXTRA;
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
    b.append (getTextCharExtra());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
}
