package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * The RestoreDC function restores a device context (DC) to the specified
 * state. The DC is restored by popping state information off a stack
 * created by earlier calls to the SaveDC function.
 *
 <code>
BOOL RestoreDC(
 HDC hdc,       // handle to DC
 int nSavedDC   // restore state
);
</code>
 *
 * Parameters
 * nSavedDC [in] Specifies the saved state to be restored. If this parameter
 * is positive, nSavedDC represents a specific instance of the state to be
 * restored. If this parameter is negative, nSavedDC represents an instance
 * relative to the current state. For example, –1 restores the most recently
 * saved state.
 */
public class MfCmdRestoreDc extends MfCmd
{
  private int dcId;

  public MfCmdRestoreDc ()
  {
  }

  /**
   * Implemented!
   */
  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    if (dcId == 0)
      return;

    if (dcId > 0)
    {
      file.restoreDCState (dcId);
    }
    else
    {
      file.restoreDCState (file.getStateCount () - dcId);
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdRestoreDc ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setNSavedDC (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.RESTORE_DC;
  }

  public int getNSavedDC ()
  {
    return dcId;
  }

  public void setNSavedDC (int id)
  {
    this.dcId = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[RESTORE_DC] nSavedDC=");
    b.append (getNSavedDC ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
