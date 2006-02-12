/**
 * Creation-Date: 26.10.2005, 14:42:15
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontStretch extends CSSConstant implements Comparable
{
  public static final FontStretch NORMAL =
          new FontStretch("normal", 0);
  public static final FontStretch ULTRA_CONDENSED =
          new FontStretch("ultra-condensed", -4);
  public static final FontStretch EXTRA_CONDENSED =
          new FontStretch("extra-condensed", -3);
  public static final FontStretch CONDENSED =
          new FontStretch("condensed", -2);
  public static final FontStretch SEMI_CONDENSED =
          new FontStretch("semi-condensed", -1);
  public static final FontStretch SEMI_EXPANDED =
          new FontStretch("semi-expanded", 1);
  public static final FontStretch EXPANDED =
          new FontStretch("expanded", 2);
  public static final FontStretch EXTRA_EXPANDED =
          new FontStretch("extra-expanded", 3);
  public static final FontStretch ULTRA_EXPANDED =
          new FontStretch("ultra-expanded", 4);

  public static final CSSConstant WIDER = new CSSConstant("wider");
  public static final CSSConstant NARROWER = new CSSConstant("narrower");

  private int order;

  private FontStretch(String name, final int order)
  {
    super(name);
    this.order = order;
  }

  public int getOrder()
  {
    return order;
  }

  public int compareTo(final Object o)
  {
    FontStretch fs = (FontStretch) o;
    if (fs.order == order)
    {
      return 0;
    }
    else if (order > fs.order)
    {
      return 1;
    }
    return -1;
  }

  public static FontStretch getByOrder(int order)
  {
    switch (order)
    {
      case -4:
        return ULTRA_CONDENSED;
      case -3:
        return EXTRA_CONDENSED;
      case -2:
        return CONDENSED;
      case -1:
        return SEMI_CONDENSED;
      case 1:
        return SEMI_EXPANDED;
      case 2:
        return EXPANDED;
      case 3:
        return EXTRA_EXPANDED;
      case 4:
        return ULTRA_EXPANDED;
      case 0:
        return NORMAL;
    }

    if (order < -4)
    {
      return ULTRA_CONDENSED;
    }
    return ULTRA_EXPANDED;
  }
}
