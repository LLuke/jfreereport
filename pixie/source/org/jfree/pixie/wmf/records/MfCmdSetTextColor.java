package org.jfree.pixie.wmf.records;

import org.jfree.pixie.wmf.GDIColor;
import org.jfree.pixie.wmf.MfDcState;
import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.MfType;
import org.jfree.pixie.wmf.WmfFile;
import org.jfree.pixie.wmf.records.MfCmd;

import java.awt.Color;

public class MfCmdSetTextColor extends MfCmd
{
  private Color color;

  public MfCmdSetTextColor ()
  {
  }

  public void replay (org.jfree.pixie.wmf.WmfFile file)
  {
    org.jfree.pixie.wmf.MfDcState state = file.getCurrentState ();
    state.setTextColor (color);
  }

  public MfCmd getInstance ()
  {
    return new MfCmdSetTextColor ();
  }

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    int colref = record.getLongParam (0);
    setColor (new org.jfree.pixie.wmf.GDIColor (colref));
  }

  public int getFunction ()
  {
    return org.jfree.pixie.wmf.MfType.SET_TEXT_COLOR;
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
    b.append (getColor ());
    return b.toString ();
  }

  protected void scaleXChanged ()
  {
  }

  protected void scaleYChanged ()
  {
  }

}
