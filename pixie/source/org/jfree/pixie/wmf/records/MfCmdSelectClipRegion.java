package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.WmfObject;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSelectClipRegion extends MfCmd
{
  private int objectId;

  public MfCmdSelectClipRegion ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.WmfObject object = file.getObject (objectId);
    if (object == null)
      throw new NullPointerException ();

    switch (object.getType ())
    {
      case org.jfree.pixie.wmf.WmfObject.OBJ_REGION:
        file.getCurrentState ().setLogRegion ((org.jfree.pixie.wmf.MfLogRegion) object);
        break;
      default:
        throw new IllegalStateException ("Object is no region");
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSelectClipRegion ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SELECT_CLIP_REGION;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SELECT_CLIPREGION] object=");
    b.append (getObjectId ());
    return b.toString ();
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
