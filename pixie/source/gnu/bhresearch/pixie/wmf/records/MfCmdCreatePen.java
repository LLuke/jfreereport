package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.GDIColor;
import gnu.bhresearch.pixie.wmf.MfLogPen;
import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.WmfFile;

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

  public void replay (WmfFile file)
  {
    MfLogPen lpen = new MfLogPen ();
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
    return MfType.CREATE_PEN_INDIRECT;
  }

  public void setRecord (MfRecord record)
  {
    int style = record.getParam (0);
    int width = record.getParam (1);
    int color = record.getLongParam (2);

    setStyle (style);
    setWidth (width);
    setColor (new GDIColor (color));
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
