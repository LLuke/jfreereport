package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetRop2 extends MfCmd
{
  private int drawmode;

  public MfCmdSetRop2 ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setROP (drawmode);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetRop2 ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setDrawMode (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_ROP2;
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
    b.append (getDrawMode());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
