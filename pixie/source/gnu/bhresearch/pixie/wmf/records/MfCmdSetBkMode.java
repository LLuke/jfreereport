package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetBkMode extends MfCmd
{

  private int bkmode;

  public MfCmdSetBkMode ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setBkMode (bkmode);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetBkMode ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setBkMode (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_BK_MODE;
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
    b.append (getBkMode());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
}
