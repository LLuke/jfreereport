package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this command is ignored.

public class MfCmdSetDibitsToDevice extends MfCmd
{
  public MfCmdSetDibitsToDevice ()
  {
  }

  public void replay (WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetDibitsToDevice ();
  }

  public void setRecord (MfRecord record)
  {
    System.out.println ("SetDibitsToDevice is not implemented.");

  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_DIBITS_TO_DEVICE] <<windows specific, will not be implemented>>");
    return b.toString ();
  }

  public int getFunction ()
  {
    return MfType.SET_DIBITS_TO_DEVICE;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
