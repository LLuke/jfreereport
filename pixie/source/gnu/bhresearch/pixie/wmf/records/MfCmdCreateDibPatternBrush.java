package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.bitmap.DIBReader;

import java.awt.image.BufferedImage;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this command is ignored.

public class MfCmdCreateDibPatternBrush extends MfCmd
{
  private BufferedImage image;

  public void setRecord (MfRecord record)
  {
    try
    {
      DIBReader reader = new DIBReader ();
      image = reader.setRecord (record);
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_DIB_PATTERN_BRUSH] ");
    b.append (" no internals known ");
    return b.toString ();
  }

  public int getFunction ()
  {
    return MfType.CREATE_DIB_PATTERN_BRUSH;
  }

  public MfCmdCreateDibPatternBrush ()
  {
  }

  public void replay (WmfFile file)
  {
    MfLogBrush lbrush = new MfLogBrush ();
    lbrush.setStyle (MfLogBrush.BS_DIBPATTERN);
    lbrush.setBitmap (image);

    file.getCurrentState ().setLogBrush (lbrush);
    file.storeObject (lbrush);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdCreateDibPatternBrush ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
