package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * Currently i have no clue, how this is implemented. Lets have a
 * look at the WINE-Sources.
 */
public class MfCmdCreateRegion extends MfCmd
{
  public MfCmdCreateRegion ()
  {
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
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
    return org.jfree.pixie.wmf.MfType.CREATE_REGION;
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
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
