package org.jfree.pixie.wmf.records;

import java.awt.Point;
import java.awt.Rectangle;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.ROPConstants;
import org.jfree.pixie.wmf.WmfFile;

/**
 * BitBlockTransfer - Copies PixelData of a rectangle to another position
 *
 * <pre>
BOOL BitBlt(
  HDC hdcDest, // handle to destination DC
  int nXDest,  // x-coord of destination upper-left corner
  int nYDest,  // y-coord of destination upper-left corner
  int nWidth,  // width of destination rectangle
  int nHeight, // height of destination rectangle
  HDC hdcSrc,  // handle to source DC
  int nXSrc,   // x-coordinate of source upper-left corner
  int nYSrc,   // y-coordinate of source upper-left corner
  DWORD dwRop  // raster operation code
);
 </pre>
 *
 * This method is not implemented. You may use this implementation to generate
 * the correct record. This command may contain a DIB that should be copied into
 * the target image. DIBs are not yet fully implemented.
 * <p>
 * todo implement me once the DIBs are complete
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

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay (WmfFile file)
  {
    // is not implemented, as we don't have access to the raster data.
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[BIT_BLT] records=");
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
    scaled_destY = getScaledY (destY);
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

  /** Writer function */
  public MfRecord getRecord ()
  {
    // Assume the simple form, as the complex form would need a DIB implementation.
    MfRecord record = new MfRecord(8);
    record.setLongParam(0, getOperation());
    Rectangle source = getSource();
    record.setParam(2, (int) source.getY());
    record.setParam(3, (int) source.getX());

    // Ignore the handle to the device context
    Rectangle dest = getDestination();
    record.setParam(4, 0); // the handle to the device context ... a stored DIB?.
    record.setParam(5, (int) dest.getHeight());
    record.setParam(6, (int) dest.getWidth());
    record.setParam(7, (int) dest.getY());
    record.setParam(8, (int) dest.getX());
    return record;
  }

  public void setRecord (MfRecord record)
  {
    int rop = record.getLongParam (0);
    int sy = record.getParam (2);
    int sx = record.getParam (3);

    int parCnt = 0;
    if (record.getLength () == (record.RECORD_HEADER_SIZE + 8 * 2))
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
