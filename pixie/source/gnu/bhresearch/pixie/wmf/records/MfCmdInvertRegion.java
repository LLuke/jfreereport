package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.image.BufferedImage;

//
// Inverts the colors in the specified region
//

public class MfCmdInvertRegion extends MfCmd
{
  private int region;

  public MfCmdInvertRegion ()
  {
  }

  public void replay (WmfFile file)
  {
    BufferedImage image = file.getImage ();

    // Scalierung holen (über AffineTransform)
    //    Graphics2D graphics = file.getGraphics2D ();


//    int[] data = image.getRGB (source.x, source.y, source.width, source.height, null, 0, source.width);
//    image.setRGB (dest.x, dest.y, dest.width, dest.height, data, 0, dest.width);
    // not yet implemented!

  }

  public MfCmd getInstance ()
  {
    return new MfCmdInvertRegion ();
  }

  public void setRecord (MfRecord record)
  {
    int region = record.getParam (0);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(1);
    record.setParam(0, getRegion());
    return record;
  }

  public void setRegion (int region)
  {
    this.region = region;
  }

  public int getRegion ()
  {
    return region;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[INVERT_REGION] region=");
    b.append (getRegion ());
    return b.toString ();
  }

  public int getFunction ()
  {
    return MfType.INVERT_REGION;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
