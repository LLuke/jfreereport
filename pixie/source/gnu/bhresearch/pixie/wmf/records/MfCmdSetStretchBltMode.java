package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetStretchBltMode extends MfCmd
{
  private int stretchmode;

  public MfCmdSetStretchBltMode ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setStretchBltMode (stretchmode);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetStretchBltMode ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setStretchMode (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_STRETCH_BLT_MODE;
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
    b.append (getStretchMode());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
