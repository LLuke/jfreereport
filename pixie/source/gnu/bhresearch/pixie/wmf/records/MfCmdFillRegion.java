package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.MfLogRegion;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.Rectangle;

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
  
    BufferedImage img = file.getImage ();
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
  
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[FILL_REGION] brush=");
    b.append (getBrush());
    b.append (" region=");
    b.append (getRegion());
    return b.toString();
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
