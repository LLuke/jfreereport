package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.MfLogFont;
import gnu.bhresearch.pixie.wmf.MfLogPalette;
import gnu.bhresearch.pixie.wmf.MfLogPen;
import gnu.bhresearch.pixie.wmf.MfLogRegion;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.WmfObject;

public class MfCmdSelectObject extends MfCmd
{
  private int objectId;

  public MfCmdSelectObject ()
  {
  }

  public void replay (WmfFile file)
  {
    WmfObject object = file.getObject (objectId);
    if (object == null)
    {
      throw new NullPointerException ("Object " + objectId + " is not defined");
    }
    switch (object.getType ())
    {
      case WmfObject.OBJ_BRUSH:
        file.getCurrentState ().setLogBrush ((MfLogBrush) object);
        break;
      case WmfObject.OBJ_FONT:
        file.getCurrentState ().setLogFont ((MfLogFont) object);
        break;
      case WmfObject.OBJ_PALETTE:
        file.getCurrentState ().setLogPalette ((MfLogPalette) object);
        break;
      case WmfObject.OBJ_PEN:
        file.getCurrentState ().setLogPen ((MfLogPen) object);
        break;
      case WmfObject.OBJ_REGION:
        file.getCurrentState ().setLogRegion ((MfLogRegion) object);
        break;
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSelectObject ();
  }

  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return MfType.SELECT_OBJECT;
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
