package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * Currently i have no clue, how this is implemented. Lets have a
 * look at the WINE-Sources.
 */
public class MfCmdCreateRegion extends MfCmd
{
  public MfCmdCreateRegion ()
  {
  }

  public void setRecord (MfRecord record)
  {
    System.out.println ("Create Region is not implemented.");
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_REGION] ");
    b.append (" no internals known (see WINE for details)");
    return b.toString ();
  }


  public int getFunction ()
  {
    return MfType.CREATE_REGION;
  }

  public void replay (WmfFile file)
  {
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    return null;
  }

  public MfCmd getInstance ()
  {
    return new MfCmdCreateRegion ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
