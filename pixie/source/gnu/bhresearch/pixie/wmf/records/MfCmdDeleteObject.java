package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * Removes an object from the object list. An wmf-object is either
 * a Pen, a palette, a brush, a font or a region.
 */
public class MfCmdDeleteObject extends MfCmd
{
  private int objectId;

  public MfCmdDeleteObject ()
  {
  }

  public void replay (WmfFile file)
  {
    file.deleteObject (objectId);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdDeleteObject ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[DELETE_OBJECT] object=");
    b.append (getObjectId ());
    return b.toString ();
  }

  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return MfType.DELETE_OBJECT;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public void setObjectId (int id)
  {
    this.objectId = id;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
