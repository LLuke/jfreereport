package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Graphics2D;
import java.awt.Point;


/**
 * The cursor is set to the destination point.
 */
public class MfCmdMoveTo extends MfCmd
{
  private int destX;
  private int destY;
  private int scaled_destX;
  private int scaled_destY;

  public MfCmdMoveTo ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    Graphics2D graph = file.getGraphics2D ();
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();

    Point p = getScaledDestination ();
    state.setCurPos (p.x, p.y);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdMoveTo ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.MOVE_TO;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[MOVE_TO] destination=");
    b.append (getDestination ());
    return b.toString ();
  }

  public void setDestination (int x, int y)
  {
    destX = x;
    destY = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getDestination ()
  {
    return new Point (destX, destY);
  }

  public Point getScaledDestination ()
  {
    return new Point (scaled_destX, scaled_destY);
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

  protected void scaleXChanged ()
  {
    scaled_destX = getScaledX (destX);
  }

  protected void scaleYChanged ()
  {
    scaled_destY = getScaledY (destY);
  }
}
