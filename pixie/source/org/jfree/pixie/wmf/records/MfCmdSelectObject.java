package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogFont;
import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfLogPen;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.WmfObject;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSelectObject extends MfCmd
{
  private int objectId;

  public MfCmdSelectObject ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.WmfObject object = file.getObject (objectId);
    if (object == null)
    {
      throw new NullPointerException ("Object " + objectId + " is not defined");
    }
    switch (object.getType ())
    {
      case org.jfree.pixie.wmf.WmfObject.OBJ_BRUSH:
        file.getCurrentState ().setLogBrush ((org.jfree.pixie.wmf.MfLogBrush) object);
        break;
      case org.jfree.pixie.wmf.WmfObject.OBJ_FONT:
        file.getCurrentState ().setLogFont ((org.jfree.pixie.wmf.MfLogFont) object);
        break;
      case org.jfree.pixie.wmf.WmfObject.OBJ_PALETTE:
        file.getCurrentState ().setLogPalette ((org.jfree.pixie.wmf.MfLogPalette) object);
        break;
      case org.jfree.pixie.wmf.WmfObject.OBJ_PEN:
        file.getCurrentState ().setLogPen ((org.jfree.pixie.wmf.MfLogPen) object);
        break;
      case org.jfree.pixie.wmf.WmfObject.OBJ_REGION:
        file.getCurrentState ().setLogRegion ((org.jfree.pixie.wmf.MfLogRegion) object);
        break;
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSelectObject ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SELECT_OBJECT;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public void setObjectId (int id)
  {
    this.objectId = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SELECT_OBJECT] object=");
    b.append (getObjectId ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
