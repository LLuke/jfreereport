package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

public class MfCmdSaveDc extends MfCmd
{
  public MfCmdSaveDc ()
  {
  }

  /**
   * Implemented!
   */
  public void replay (WmfFile file)
  {
    file.saveDCState();
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSaveDc ();
  }
  
  public void setRecord (MfRecord record)
  {
    // Save DC has no parameters
  }
  
  public int getFunction ()
  {
    return MfType.SAVE_DC;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SAVE_DC]");
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
