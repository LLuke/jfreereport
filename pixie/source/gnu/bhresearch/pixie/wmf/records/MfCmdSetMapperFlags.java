package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

/**
 * This command is ignored, as it is used to map fonts into the given
 * aspect ratio. This affects only BitMap-fonts, as TrueTypeFonts are
 * always able to scale to any aspect ratio.
 *
 * In java all fonts are considered true-type.
 */
public class MfCmdSetMapperFlags extends MfCmd
{
  private int mapperflags;

  public MfCmdSetMapperFlags ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setMapperFlag (mapperflags);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetMapperFlags ();
  }

  public void setRecord (MfRecord record)
  {
    int id = record.getLongParam (0);
    setMapperFlags (id);
  }

  public int getFunction ()
  {
    return MfType.SET_MAPPER_FLAGS;
  }

  public int getMapperFlags ()
  {
    return mapperflags;
  }

  public void setMapperFlags (int id)
  {
    this.mapperflags = id;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[MAPPERFLAGS] mapperflags=");
    b.append (getMapperFlags ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
