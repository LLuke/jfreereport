package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfLogBrush;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;

public class MfCmdCreateBrush extends MfCmd
{
  private int style;
  private Color color;
  private int hatch;

  public MfCmdCreateBrush ()
  {
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[CREATE_BRUSH] style=");
    b.append (getStyle ());
    b.append (" color=");
    b.append (getColor ());
    b.append (" hatch=");
    b.append (getHatch ());
    return b.toString ();
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfLogBrush lbrush = new org.jfree.pixie.wmf.MfLogBrush ();
    lbrush.setStyle (getStyle ());
    lbrush.setColor (getColor ());
    lbrush.setHatchedStyle (getHatch ());

    file.getCurrentState ().setLogBrush (lbrush);
    file.storeObject (lbrush);

  }

  public MfCmd getInstance ()
  {
    return new MfCmdCreateBrush ();
  }

  public void setStyle (int style)
  {
    this.style = style;
  }

  public int getStyle ()
  {
    return style;
  }

  public void setHatch (int hatch)
  {
    this.hatch = hatch;
  }

  public int getHatch ()
  {
    return hatch;
  }

  public void setColor (Color c)
  {
    this.color = c;
  }

  public Color getColor ()
  {
    return color;
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.CREATE_BRUSH_INDIRECT;
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int style = record.getParam (0);
    int color = record.getLongParam (1);
    int hatch = record.getParam (3);
    setStyle (style);
    setColor (new org.jfree.pixie.wmf.GDIColor (color));
    setHatch (hatch);
  }

  /** Writer function */
  public org.jfree.pixie.wmf.MfRecord getRecord ()
  {
    org.jfree.pixie.wmf.MfRecord record = new org.jfree.pixie.wmf.MfRecord(4);
    record.setParam(0, getStyle());
    record.setLongParam(1, org.jfree.pixie.wmf.GDIColor.translateColor(getColor()));
    record.setParam(3, getHatch());
    return record;
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
