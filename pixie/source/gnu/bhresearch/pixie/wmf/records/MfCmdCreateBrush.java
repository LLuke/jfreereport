package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfLogBrush;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
  {
    MfLogBrush lbrush = new MfLogBrush ();
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
    return MfType.CREATE_BRUSH_INDIRECT;
  }

  public void setRecord (MfRecord record)
  {
    int style = record.getParam (0);
    int color = record.getLongParam (1);
    int hatch = record.getParam (3);
    setStyle (style);
    setColor (new GDIColor (color));
    setHatch (hatch);
  }

  /** Writer function */
  public MfRecord getRecord ()
  {
    MfRecord record = new MfRecord(4);
    record.setParam(0, getStyle());
    record.setLongParam(1, GDIColor.translateColor(getColor()));
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
