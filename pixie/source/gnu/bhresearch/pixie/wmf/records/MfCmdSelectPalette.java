package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfLogPalette;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.WmfObject;

public class MfCmdSelectPalette extends MfCmd
{
  private int objectId;

  public MfCmdSelectPalette ()
  {
  }

  public void replay (WmfFile file)
  {
    WmfObject object = file.getObject (objectId);
    if (object == null)
      throw new NullPointerException ();

    switch (object.getType ())
    {
      case WmfObject.OBJ_PALETTE:
        file.getCurrentState ().setLogPalette ((MfLogPalette) object);
        break;
      default:
        throw new IllegalStateException ("Object is no palette");
    }

  }

  public MfCmd getInstance ()
  {
    return new MfCmdSelectPalette ();
  }

  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return MfType.SELECT_PALETTE;
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
    b.append ("[SELECT_PALETTE] object=");
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
