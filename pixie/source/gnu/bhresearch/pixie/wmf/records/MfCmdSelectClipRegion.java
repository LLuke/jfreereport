package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.WmfObject;
import gnu.bhresearch.pixie.wmf.MfLogRegion;

public class MfCmdSelectClipRegion extends MfCmd
{
  private int objectId;

  public MfCmdSelectClipRegion ()
  {
  }

  public void replay (WmfFile file)
  {
    WmfObject object = file.getObject (objectId);
    if (object == null)
       throw new NullPointerException ();
       
    switch (object.getType())
    {
      case WmfObject.OBJ_REGION:  file.getCurrentState().setLogRegion ((MfLogRegion) object); break;
      default: throw new IllegalStateException ("Object is no region");
    }
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSelectClipRegion ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }
  
  public int getFunction ()
  {
    return MfType.SELECT_CLIP_REGION;
  }

  public int getObjectId ()
  {
    return objectId;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SELECT_CLIPREGION] object=");
    b.append (getObjectId());
    return b.toString();
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
