package org.jfree.report.style;

import java.io.Serializable;

public interface StyleSheetCarrier extends Cloneable, Serializable
{
  public ElementStyleSheet getStyleSheet ();

  public boolean isSame (ElementStyleSheet style);

  public void invalidate ();

  public Object clone ()
          throws CloneNotSupportedException;
}
