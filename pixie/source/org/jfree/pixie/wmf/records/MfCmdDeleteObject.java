package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

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

  public void replay (org.jfree.pixie.wmf.WmfFile file)
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

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(1);
    record.setParam(0, getObjectId());
    return record;
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.DELETE_OBJECT;
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
