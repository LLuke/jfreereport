package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.BrushConstants;

// The structure constists of a single bitmap, which defines the
// Brush. Not yet implemented.
public class MfCmdCreatePatternBrush extends MfCmd
{
  public void setRecord (MfRecord record)
  {
    System.out.println ("Create Pattern Brush is not implemented.");
    System.out.println (record.toString ());
  }
  
  public int getFunction ()
  {
    return MfType.CREATE_PATTERN_BRUSH;
  }

  public MfCmdCreatePatternBrush ()
  {
  }
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_PATTERN_BRUSH] ");
    b.append (" no internals known (not seen in the wild)");
    return b.toString();
  }
  
  public void replay (WmfFile file)
  {
    MfLogBrush lbrush = new MfLogBrush ();
    lbrush.setStyle (BrushConstants.BS_NULL);
    lbrush.setColor (new GDIColor (0xFFFFFFFF));

    file.getCurrentState ().setLogBrush (lbrush);
    file.storeObject (lbrush);
  
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdCreatePatternBrush ();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
