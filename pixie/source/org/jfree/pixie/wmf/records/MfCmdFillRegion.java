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

public class MfCmdFillRegion extends MfCmd
{
  private int brushObjectNr;
  private int regionObjectNr;

  public MfCmdFillRegion ()
  {
  }

  public void replay (WmfFile file)
  {
    MfLogBrush brush = file.getBrushObject (brushObjectNr);
    MfLogRegion regio = file.getRegionObject (regionObjectNr);

    MfDcState state = file.getCurrentState ();
    state.setLogRegion (regio);
    state.setLogBrush (brush);

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
    return new MfCmdFillRegion ();
  }

  public void setRecord (MfRecord record)
  {
    int regio = record.getParam (0);
    int brush = record.getParam (1);
    setBrush (brush);
    setRegion (regio);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(2);
    record.setParam(0, getRegion());
    record.setParam(1, getBrush());
    return record;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[FILL_REGION] brush=");
    b.append (getBrush ());
    b.append (" region=");
    b.append (getRegion ());
    return b.toString ();
  }

  public int getBrush ()
  {
    return brushObjectNr;
  }

  public void setBrush (int brush)
  {
    this.brushObjectNr = brush;
  }

  public int getRegion ()
  {
    return regionObjectNr;
  }

  public void setRegion (int region)
  {
    regionObjectNr = region;
  }

  public int getFunction ()
  {
    return MfType.FILL_REGION;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }
}
