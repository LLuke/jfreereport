package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;
import org.jfree.pixie.wmf.bitmap.DIBReader;

import java.awt.image.BufferedImage;

// This structure should include a bitmap. This implementation does
// not know of any bitmaps right now, so this records is ignored.

public class MfCmdCreateDibPatternBrush extends MfCmd
{
  private BufferedImage image;

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    /**
     * Requires a DIB-Writer, is not yet supported
     */
    return null;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    try
    {
      DIBReader reader = new DIBReader ();
      setImage(reader.setRecord (record));
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
  }

  public BufferedImage getImage ()
  {
    return image;
  }

  public void setImage (BufferedImage image)
  {
    if (image == null) throw new NullPointerException();
    this.image = image;
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
    return org.jfree.pixie.wmf.MfType.CREATE_DIB_PATTERN_BRUSH;
  }

  public MfCmdCreateDibPatternBrush ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogBrush lbrush = new org.jfree.pixie.wmf.MfLogBrush ();
    lbrush.setStyle (org.jfree.pixie.wmf.MfLogBrush.BS_DIBPATTERN);
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
