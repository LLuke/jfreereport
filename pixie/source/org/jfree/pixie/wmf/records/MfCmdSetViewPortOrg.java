package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Point;

/**
 * Defines the upper left corner of the viewport. The size of the
 * viewport is defined using setViewportExt.
 */
public class MfCmdSetViewPortOrg extends MfCmd
{
  private int x;
  private int y;
  private int scaled_x;
  private int scaled_y;

  public MfCmdSetViewPortOrg ()
  {
  }

  /**
   * Implemented!!
   */
  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    Point p = getScaledTarget ();
    state.setViewportOrg (p.x, p.y);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetViewPortOrg ();
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_VIEWPORT_ORG] target=");
    b.append (getTarget ());
    return b.toString ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int y = record.getParam (0);
    int x = record.getParam (1);
    setTarget (x, y);
  }

  public Point getTarget ()
  {
    return new Point (x, y);
  }

  public void setTarget (int x, int y)
  {
    this.x = x;
    this.y = y;
    scaleXChanged ();
    scaleYChanged ();

  }

  public Point getScaledTarget ()
  {
    return new Point (scaled_x, scaled_y);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_VIEWPORT_ORG;
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
