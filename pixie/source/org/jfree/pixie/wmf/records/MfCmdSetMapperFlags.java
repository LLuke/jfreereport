package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

/**
 * This records is ignored, as it is used to map fonts into the given
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

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setMapperFlag (mapperflags);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetMapperFlags ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int id = record.getLongParam (0);
    setMapperFlags (id);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_MAPPER_FLAGS;
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
