package org.jfree.report.style;

public interface StyleSheetCarrier extends Cloneable
{
  public ElementStyleSheet getStyleSheet();
  public boolean isSame (ElementStyleSheet style);
  public void invalidate ();
  public Object clone () throws CloneNotSupportedException;
}
