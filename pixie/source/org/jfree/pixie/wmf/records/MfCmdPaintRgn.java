package org.jfree.pixie.wmf.records;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfLogRegion;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;

/**
 * Fills the region with the currently selected brush
 */
public class MfCmdPaintRgn extends MfCmd
{
  private int region;

  public MfCmdPaintRgn ()
  {
  }

  public void replay (WmfFile file)
  {
    MfLogRegion regio = file.getRegionObject (region);

    MfDcState state = file.getCurrentState ();
    state.setLogRegion (regio);
    MfLogBrush brush = state.getLogBrush ();

    Graphics2D graph = file.getGraphics2D ();
    Rectangle rec = scaleRect (regio.getBounds ());

    Rectangle2D rect = new Rectangle2D.Double ();
    rect.setFrame (rec.x, rec.y, rec.width, rec.height);

    if (brush.isVisible ())
    {
      state.preparePaint ();
      graph.fill (rect);
      state.postPaint ();
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdPaintRgn ();
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

  public int getFunction ()
  {
    return MfType.PAINTREGION;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[PAINT_REGION] region=");
    b.append (getRegion ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
