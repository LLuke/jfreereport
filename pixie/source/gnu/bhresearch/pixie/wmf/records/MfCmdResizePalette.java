package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

public class MfCmdResizePalette extends MfCmd
{
  public MfCmdResizePalette ()
  {
  }

  public void replay (WmfFile file)
  {
    // Not implemented!
  }

  public MfCmd getInstance ()
  {
    return new MfCmdResizePalette ();
  }

  public void setRecord (MfRecord record)
  {
    System.out.println ("ResizePalette is not yet implemented.");
  }

  public int getFunction ()
  {
    return MfType.RESIZE_PALETTE;
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
