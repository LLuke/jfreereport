package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Rectangle;

public class MfCmdStretchBlt extends MfCmd
{
  private int rop;
  private int srcX;
  private int srcY;
  private int srcW;
  private int srcH;
  private int destX;
  private int destY;
  private int destW;
  private int destH;

  private int scaled_srcX;
  private int scaled_srcY;
  private int scaled_srcW;
  private int scaled_srcH;
  private int scaled_destX;
  private int scaled_destY;
  private int scaled_destW;
  private int scaled_destH;

  public MfCmdStretchBlt ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
  }

  public MfCmd getInstance ()
  {
    return new MfCmdStretchBlt ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.STRETCH_BLT;
  }

  public int getROP ()
  {
    return rop;
  }

  public void setROP (int rop)
  {
    this.rop = rop;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int rop = record.getLongParam (0);
    int ySrc = record.getParam (2);
    int xSrc = record.getParam (3);
    int srcH = record.getParam (4);
    int srcW = record.getParam (5);
    int destH = record.getParam (6);
    int destW = record.getParam (7);
    int yDest = record.getParam (8);
    int xDest = record.getParam (9);
    // DIB ab pos 10
    setROP (rop);
    setSrcRect (xSrc, ySrc, srcH, srcW);
    setDestRect (xDest, yDest, destH, destW);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[STRETCH_BLT] rop=");
    b.append (getROP ());
    b.append (" srcRect=");
    b.append (getSrcRect ());
    b.append (" destRect=");
    b.append (getDestRect ());
    return b.toString ();
  }


  public void setSrcRect (int x, int y, int w, int h)
  {
    this.srcX = x;
    this.srcY = y;
    this.srcW = w;
    this.srcH = h;
    scaleXChanged ();
    scaleYChanged ();
  }

  public void setDestRect (int x, int y, int w, int h)
  {
    this.destX = x;
    this.destY = y;
    this.destW = w;
    this.destH = h;
    scaleXChanged ();
    scaleYChanged ();
  }

  public Rectangle getSrcRect ()
  {
    return new Rectangle (srcX, srcY, srcW, srcH);
  }

  public Rectangle getDestRect ()
  {
    return new Rectangle (destX, destY, destW, destH);
  }

  public Rectangle getScaledSrcRect ()
  {
    return new Rectangle (scaled_srcX, scaled_srcY, scaled_srcW, scaled_srcH);
  }

  public Rectangle getScaledDestRect ()
  {
    return new Rectangle (scaled_destX, scaled_destY, scaled_destW, scaled_destH);
  }

  protected void scaleXChanged ()
  {
    scaled_srcX = getScaledX (srcX);
    scaled_srcW = getScaledX (srcW);
    scaled_destX = getScaledX (destX);
    scaled_destW = getScaledX (destW);
  }

  protected void scaleYChanged ()
  {
    scaled_srcY = getScaledY (srcY);
    scaled_srcH = getScaledY (srcH);
    scaled_destY = getScaledY (destY);
    scaled_destH = getScaledY (destH);
  }
}
