package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.MfType;
import gnu.bhresearch.pixie.wmf.GDIColor;
import java.awt.Color;
import gnu.bhresearch.pixie.wmf.WmfFile;
import gnu.bhresearch.pixie.wmf.MfDcState;

public class MfCmdSetTextColor extends MfCmd
{
  private Color color;

  public MfCmdSetTextColor ()
  {
  }

  public void replay (WmfFile file)
  {
    MfDcState state = file.getCurrentState ();
    state.setTextColor (color);
  }
  
  public MfCmd getInstance ()
  {
    return new MfCmdSetTextColor ();
  }
  
  public void setRecord (MfRecord record)
  {
    int colref = record.getLongParam (0);
    setColor (new GDIColor (colref));
  }
  
  public int getFunction ()
  {
    return MfType.SET_TEXT_COLOR;
  }

  public Color getColor ()
  {
    return color;
  }

  public void setColor (Color color)
  {
    this.color = color;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("[SET_TEXT_COLOR] textColor=");
    b.append (getColor());
    return b.toString();
  }

  protected void scaleXChanged ()
  {
  }
  
  protected void scaleYChanged ()
  {
  }

}
