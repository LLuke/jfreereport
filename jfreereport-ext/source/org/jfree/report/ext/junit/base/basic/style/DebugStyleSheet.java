package org.jfree.report.ext.junit.base.basic.style;

import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCarrier;

public class DebugStyleSheet extends ElementStyleSheet
{
  private static class DirectStyleSheetCarrier implements StyleSheetCarrier
  {
    private ElementStyleSheet styleSheet;

    public DirectStyleSheetCarrier (final ElementStyleSheet styleSheet)
    {
      this.styleSheet = styleSheet;
    }

    public Object clone ()
            throws CloneNotSupportedException
    {
      return super.clone();
    }

    public ElementStyleSheet getStyleSheet ()
    {
      return styleSheet;
    }

    public void invalidate ()
    {
      styleSheet = null;
    }

    public boolean isSame (final ElementStyleSheet style)
    {
      return style.equals(styleSheet);
    }
  }

  public DebugStyleSheet (final String name)
  {
    super(name);
  }

  protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
  {
    return new DirectStyleSheetCarrier(styleSheet);
  }

}
