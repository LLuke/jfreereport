package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Moves the current Clipping Region (@see CreateRegion) to the specified
 * position
 */
public class MfCmdOffsetClipRgn extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdOffsetClipRgn ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    Rectangle clipRect = state.getClipRegion ();
    Point p = getScaledDestination ();
    clipRect.x = p.x;
    clipRect.y = p.y;
    state.setClipRegion (clipRect);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdOffsetClipRgn ();
  }

  public Point getDestination ()
  {
    return new Point (x, y);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[OFFSET_CLIP_RECT] destination=");
    b.append (getDestination ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.OFFSET_CLIP_RGN;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setDestination (x, y);
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    Point dest = getDestination();
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(2);
    record.setParam(0, dest.y);
    record.setParam(1, dest.x);
    return record;
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_x, scaled_y);
  }

  protected void scaleXChanged ()
  {
    scaled_x = getScaledX (x);
  }

  protected void scaleYChanged ()
  {
    scaled_y = getScaledY (y);
  }
}
