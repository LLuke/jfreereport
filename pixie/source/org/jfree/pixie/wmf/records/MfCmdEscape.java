package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * The layout of META_ESC is unknown, but it doesnt matter,
 * as it has no effect on on screen metafile display (i think).
 *
 * This sends MCI-Informations to the device-driver. Java and all
 * non-windows systems have no use of Windows-Driver-Details at all.
 */
public class MfCmdEscape extends MfCmd
{
  public MfCmdEscape ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[ESCAPE] is not used and will not be implemented");
    return b.toString ();
  }

  public MfCmd getInstance ()
  {
    return new MfCmdEscape ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    System.out.println ("Escape is not implemented.");
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.ESCAPE;
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
