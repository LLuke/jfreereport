package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Rectangle;

/**
 * top, left, right and bottom define the points of the region to be deleted
 * from the clipping region, the resultant clipping region is the original
 * region minus this region.
 */
public class MfCmdExcludeClipRect extends MfCmd
{
  private int x;
  private int y;
  private int width;
  private int height;
  private int scaled_x;
  private int scaled_y;
  private int scaled_width;
  private int scaled_height;

  public MfCmdExcludeClipRect ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    // Not implemented!
  }

  public MfCmd getInstance ()
  {
    return new MfCmdExcludeClipRect ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int bottom = record.getParam (0);
    int right = record.getParam (1);
    int top = record.getParam (2);
    int left = record.getParam (3);
    setBounds (left, top, right - left, bottom - top);

  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    Rectangle rc = getBounds();
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(4);
    record.setParam(0, (int)(rc.getY() + rc.getHeight()));
    record.setParam(1, (int)(rc.getX() + rc.getWidth()));
    record.setParam(2, (int)(rc.getY()));
    record.setParam(3, (int)(rc.getX()));
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[EXCLUDE_CLIP_RECT] bounds=");
    b.append (getBounds ());
    return b.toString ();
  }

  public Rectangle getBounds ()
  {
    return new Rectangle (x, y, width, height);
  }

  public Rectangle getScaledBounds ()
  {
    return new Rectangle (scaled_x, scaled_y, scaled_width, scaled_height);
  }

  public void setBounds (int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    scaleXChanged ();
    scaleYChanged ();
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
    scaled_width = getScaledX (width);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
    scaled_height = getScaledY (height);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.EXCLUDE_CLIP_RECT;
  }
}
