package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.BrushConstants;
import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

// The structure constists of a single bitmap, which defines the
// Brush. Not yet implemented.

public class MfCmdCreatePatternBrush extends MfCmd
{
  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("Create Pattern Brush is not implemented.");
    System.out.println (record.toString ());
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.CREATE_PATTERN_BRUSH;
  }

  public MfCmdCreatePatternBrush ()
  {
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_PATTERN_BRUSH] ");
    b.append (" no internals known (not seen in the wild)");
    return b.toString ();
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogBrush lbrush = new org.jfree.pixie.wmf.MfLogBrush ();
    lbrush.setStyle (org.jfree.pixie.wmf.BrushConstants.BS_NULL);
    lbrush.setColor (new org.jfree.pixie.wmf.GDIColor (0xFFFFFFFF));

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

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    return null;
  }
}
