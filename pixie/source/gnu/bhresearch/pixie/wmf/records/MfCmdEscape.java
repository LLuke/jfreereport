package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
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

  public void setRecord (MfRecord record)
  {
    System.out.println ("Escape is not implemented.");
  }

  public int getFunction ()
  {
    return MfType.ESCAPE;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    return null;
  }
}
