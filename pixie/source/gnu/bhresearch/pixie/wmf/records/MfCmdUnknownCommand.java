package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * This function is not in the validity list of Microsofts 
 * WindowsMetafile Records.
 */
public class MfCmdUnknownCommand extends MfCmd
{
  private int function;

  public MfCmdUnknownCommand ()
  {
  }

  public void replay (WmfFile file)
  {
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdUnknownCommand ();
  }
  
  public void setRecord (MfRecord record)
  {
    System.out.println (this);
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[UNKNOWN COMMAND] " + Integer.toHexString (getFunction ()));
    return b.toString();
  }
  
  public void setFunction (int function)
  {
    this.function = function;
  }
  
  public int getFunction ()
  {
    return function;
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
