package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

/**
 * The SetMapMode function sets the mapping mode of the specified 
 * device context. The mapping mode defines the unit of measure used 
 * to transform page-space units into device-space units, and also 
 * defines the orientation of the device's x and y axes. 
 *
 * @see MappingConstants
 */
public class MfCmdSetMapMode extends MfCmd
{
  private int mapmode;

  public MfCmdSetMapMode ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setMapMode (mapmode);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetMapMode ();
  }
  
  public void setRecord (MfRecord record)
  {
    int id = record.getParam (0);
    setMapMode (id);
  }
  
  public int getFunction ()
  {
    return MfType.SET_MAP_MODE;
  }

  public int getMapMode ()
  {
    return mapmode;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[MAPMODE] mapmode=");
    b.append (getMapMode());
    return b.toString();
  }

  public void setMapMode (int id)
  {
    this.mapmode = id;
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
