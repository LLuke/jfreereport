package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetPolyFillMode extends MfCmd
{
  public static final int ALTERNATE                    = 1;
  public static final int WINDING                      = 2;
  
  private int fillmode;

  public MfCmdSetPolyFillMode ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setPolyFillMode (fillmode);
  
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetPolyFillMode ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setFillMode (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_POLY_FILL_MODE;
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
    b.append (getFillMode());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
}
