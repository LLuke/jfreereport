package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.ROPConstants;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * BitBlockTransfer - Copies PixelData of a rectangle to another position
 *
 * !Check, as DIB is not implemented.
 */
public class MfCmdBitBlt extends MfCmd implements ROPConstants
{
  private int scaled_destX;
  private int scaled_destY;
  private int scaled_destWidth;
  private int scaled_destHeight;
  private int scaled_sourceX;
  private int scaled_sourceY;

  private int destX;
  private int destY;
  private int destWidth;
  private int destHeight;
  private int sourceX;
  private int sourceY;
  private int operation;

  public MfCmdBitBlt ()
  {
  }

  public void replay (WmfFile file)
  {
    BufferedImage image = file.getImage ();
    Rectangle source = getScaledSource ();
    Rectangle dest = getScaledDestination ();

    // Scalierung holen (über AffineTransform)
//    Graphics2D graphics = file.getGraphics2D ();


    int[] data = image.getRGB (source.x, source.y, source.width, source.height, null, 0, source.width);
    image.setRGB (dest.x, dest.y, dest.width, dest.height, data, 0, dest.width);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[BIT_BLT] command=");
    b.append (getOperation ());
    b.append (" source=");
    b.append (getOrigin ());
    b.append (" destination=");
    b.append (getDestination ());
    return b.toString ();

  }

  protected void scaleXChanged ()
  {
    scaled_sourceX = getScaledX (sourceX);
    scaled_destX = getScaledX (destX);
    scaled_destWidth = getScaledX (destWidth);
  }

  protected void scaleYChanged ()
  {
    scaled_sourceY = getScaledY (sourceY);
    scaled_destWidth = getScaledY (destY);
    scaled_destHeight = getScaledY (destHeight);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdBitBlt ();
  }

  public int getFunction ()
  {
    return MfType.BIT_BLT;
  }

  public void setOrigin (int x, int y)
  {
    sourceX = x;
    sourceY = y;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Point getOrigin ()
  {
    return new Point (sourceX, sourceY);
  }

  public Rectangle getSource ()
  {
    return new Rectangle (sourceX, sourceY, destWidth, destHeight);
  }

  public Point getScaledOrigin ()
  {
    return new Point (scaled_sourceX, scaled_sourceY);
  }

  public Rectangle getScaledSource ()
  {
    return new Rectangle (scaled_sourceX, scaled_sourceY, scaled_destWidth, scaled_destHeight);
  }

  public void setDestination (int x, int y, int w, int h)
  {
    destX = x;
    destY = y;
    destWidth = w;
    destHeight = h;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Rectangle getDestination ()
  {
    return new Rectangle (destX, destY, destWidth, destHeight);
  }

  public Rectangle getScaledDestination ()
  {
    return new Rectangle (scaled_destX, scaled_destY, scaled_destWidth, scaled_destHeight);
  }

  public void setOperation (int op)
  {
    operation = op;
  }

  public int getOperation ()
  {
    return operation;
  }

  public void setRecord (MfRecord record)
  {
    int rop = record.getLongParam (0);
    int sy = record.getParam (2);
    int sx = record.getParam (3);

    int parCnt = 0;
    if (record.getLength () == record.RECORD_HEADER + 8 * 2)
    {
      // Simple form
      parCnt = 5;
    }
    else
    {
      // Complex form
      parCnt = 4;
    }

    int dh = record.getParam (parCnt + 0);
    int dw = record.getParam (parCnt + 1);
    int dy = record.getParam (parCnt + 2);
    int dx = record.getParam (parCnt + 3);

    // The sourceDib follows on Position 8 til the end if this is not the simple
    // form.

    setOperation (rop);
    setOrigin (sx, sy);
    setDestination (dx, dy, dw, dh);
  }
}
