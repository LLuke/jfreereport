package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

public class MfCmdSetTextJustification extends MfCmd
{
  private int extraSpaceLength;
  private int breakCount;

  public MfCmdSetTextJustification ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setTextJustification (extraSpaceLength, breakCount);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetTextJustification ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_TEXT_JUSTIFICATION] breakCount=");
    b.append (getBreakCount ());
    b.append (" extraSpaceLength=");
    b.append (getExtraSpaceLength ());
    return b.toString ();
  }

  public void setRecord (MfRecord record)
  {
    int spaceLength = record.getParam (0);
    int breakCount = record.getParam (1);
    setExtraSpaceLength (spaceLength);
    setBreakCount (breakCount);
  }

  public int getFunction ()
  {
    return MfType.SET_TEXT_JUSTIFICATION;
  }

  public int getBreakCount ()
  {
    return breakCount;
  }

  public void setBreakCount (int count)
  {
    this.breakCount = count;
  }

  public int getExtraSpaceLength ()
  {
    return extraSpaceLength;
  }

  public void setExtraSpaceLength (int count)
  {
    this.extraSpaceLength = count;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
