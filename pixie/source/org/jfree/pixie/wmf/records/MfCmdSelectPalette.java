package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogPalette;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.WmfObject;
import org.jfree.pixie.wmf.records.MfCmd;

public class MfCmdSelectPalette extends MfCmd
{
  private int objectId;

  public MfCmdSelectPalette ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.WmfObject object = file.getObject (objectId);
    if (object == null)
      throw new NullPointerException ();

    switch (object.getType ())
    {
      case org.jfree.pixie.wmf.WmfObject.OBJ_PALETTE:
        file.getCurrentState ().setLogPalette ((org.jfree.pixie.wmf.MfLogPalette) object);
        break;
      default:
        throw new IllegalStateException ("Object is no palette");
    }

  }

  public MfCmd getInstance ()
  {
    return new MfCmdSelectPalette ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getParam (0);
    setObjectId (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SELECT_PALETTE;
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
