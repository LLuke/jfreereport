package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * This function is not in the validity list of Microsofts 
 * WindowsMetafile Records.
 */
public class MfCmdOldStrechBlt extends MfCmd
{
  public MfCmdOldStrechBlt ()
  {
  }

  public void replay (WmfFile file)
  {
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdOldStrechBlt ();
  }
  
  public void setRecord (MfRecord record)
  {
    System.out.println ("Old StretchBlt is not yet implemented.");
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OLD_STRECH_BLT] is not implemented");
    return b.toString();
  }
  
  public int getFunction ()
  {
    return MfType.OLD_STRETCH_BLT;
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }
}
