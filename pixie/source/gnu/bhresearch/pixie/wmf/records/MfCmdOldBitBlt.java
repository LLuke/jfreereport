package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * This function is not in the validity list of Microsofts 
 * WindowsMetafile Records.
 */
public class MfCmdOldBitBlt extends MfCmd
{
  public MfCmdOldBitBlt ()
  {
  }

  public void replay (WmfFile file)
  {
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdOldBitBlt ();
  }
  
  public void setRecord (MfRecord record)
  {
    System.out.println ("Old BitBlit is not yet implemented.");
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OLD_BIT_BLT] is not implemented");
    return b.toString();
  }
  
  public int getFunction ()
  {
    return MfType.OLD_BIT_BLT;
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
