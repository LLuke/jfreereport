package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Dimension;

/**
 * Sets the size of the viewport.
 */
public class MfCmdSetViewPortExt extends MfCmd
{
  private int height;
  private int width;
  private int scaled_width;
  private int scaled_height;

  public MfCmdSetViewPortExt ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    Dimension dim = getScaledDimension ();
    state.setViewportExt (dim.width, dim.height);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetViewPortExt ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int height = record.getParam (0);
    int width = record.getParam (1);
    setDimension (width, height);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_VIEWPORT_EXT] dimension=");
    b.append (getDimension ());
    return b.toString ();
  }

  public Dimension getDimension ()
  {
    return new Dimension (width, height);
  }

  public Dimension getScaledDimension ()
  {
    return new Dimension (scaled_width, scaled_height);
  }

  public void setDimension (int w, int h)
  {
    this.width = w;
    this.height = h;
    scaleXChanged ();
    scaleYChanged ();

  }

  protected void scaleXChanged ()
  {
    scaled_width = getScaledX (width);
  }

  protected void scaleYChanged ()
  {
    scaled_height = getScaledY (height);
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_VIEWPORT_EXT;
  }

}
