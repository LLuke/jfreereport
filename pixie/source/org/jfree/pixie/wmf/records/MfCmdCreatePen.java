package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogPen;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;

public class MfCmdCreatePen extends MfCmd
{
  private int style;
  private Color color;
  private int width;
  private int scaled_width;

  public MfCmdCreatePen ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogPen lpen = new org.jfree.pixie.wmf.MfLogPen ();
    lpen.setStyle (getStyle ());
    lpen.setColor (getColor ());
    lpen.setWidth (getScaledWidth ());

    file.getCurrentState ().setLogPen (lpen);
    file.storeObject (lpen);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdCreatePen ();
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.CREATE_PEN_INDIRECT;
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(3);
    record.setParam(0, getStyle());
    record.setParam(1, getWidth());
    record.setLongParam(2, org.jfree.pixie.wmf.GDIColor.translateColor(getColor()));
    return record;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int style = record.getParam (0);
    int width = record.getParam (1);
    int color = record.getLongParam (2);

    setStyle (style);
    setWidth (width);
    setColor (new org.jfree.pixie.wmf.GDIColor (color));
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_PEN] style=");
    b.append (getStyle ());
    b.append (" width=");
    b.append (getWidth ());
    b.append (" color=");
    b.append (getColor ());
    return b.toString ();
  }


  public int getStyle ()
  {
    return style;
  }

  public void setStyle (int style)
  {
    this.style = style;
  }

  public int getScaledWidth ()
  {
    return scaled_width;
  }

  public int getWidth ()
  {
    return width;
  }

  public void setWidth (int width)
  {
    this.width = width;
    scaleXChanged ();
  }

  protected void scaleXChanged ()
  {
    scaled_width = getScaledX (width);
  }

  public Color getColor ()
  {
    return color;
  }

  public void setColor (Color c)
  {
    this.color = c;
  }

  protected void scaleYChanged ()
  {
  }

}
