package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdResizePalette extends MfCmd
{
  public MfCmdResizePalette ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    // Not implemented!
  }

  public MfCmd getInstance ()
  {
    return new MfCmdResizePalette ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("ResizePalette is not yet implemented.");
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.RESIZE_PALETTE;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[RESIZE_PALETTE] is not implemented");
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
