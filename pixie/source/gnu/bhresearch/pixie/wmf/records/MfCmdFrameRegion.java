package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfDcState;
import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.MfLogRegion;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class MfCmdFrameRegion extends MfCmd
{
  private int width;
  private int height;
  private int scaled_width;
  private int scaled_height;
  private int brushObjectNr;
  private int regionObjectNr;

  public MfCmdFrameRegion ()
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

    if (brush.isVisible ())
    {
      Dimension dim = getScaledDimension ();
      // upper side
      Rectangle2D rect = new Rectangle2D.Double ();
      rect.setFrame (rec.x, rec.y, rec.width, dim.height);
      state.preparePaint ();
      graph.fill (rect);

      // lower side
      rect.setFrame (rec.x, rec.y - dim.height, rec.width, dim.height);
      graph.fill (rect);

      // east
      rect.setFrame (rec.x, rec.y, dim.width, rec.height);
      graph.fill (rect);

      // west
      rect.setFrame (rec.width - dim.width, rec.y, dim.width, rec.height);
      graph.fill (rect);
      state.postPaint ();
    }
  }

  public MfCmd getInstance ()
  {
    return new MfCmdFrameRegion ();
  }

  public int getFunction ()
  {
    return MfType.FRAME_REGION;
  }

  public void setRecord (MfRecord record)
  {
    int height = record.getParam (0);
    int width = record.getParam (1);
    int regio = record.getParam (2);
    int brush = record.getParam (3);
    setBrush (brush);
    setRegion (regio);
    setDimension (width, height);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[FRAME_REGION] region=");
    b.append (getRegion ());
    b.append (" brush=");
    b.append (getBrush ());
    b.append (" dimension=");
    b.append (getDimension ());
    return b.toString ();
  }

  public void setDimension (int width, int height)
  {
    this.width = width;
    this.height = height;
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

  public Dimension getDimension ()
  {
    return new Dimension (width, height);
  }

  public Dimension getScaledDimension ()
  {
    return new Dimension (scaled_width, scaled_height);
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

}
