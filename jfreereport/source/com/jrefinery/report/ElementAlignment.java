/**
 * Date: Nov 27, 2002
 * Time: 4:39:13 PM
 *
 * $Id$
 */
package com.jrefinery.report;

public class ElementAlignment
{
  public static final ElementAlignment LEFT = new ElementAlignment("LEFT", Element.LEFT);
  public static final ElementAlignment CENTER = new ElementAlignment("CENTER", Element.CENTER);
  public static final ElementAlignment RIGHT = new ElementAlignment("RIGHT", Element.RIGHT);

  public static final ElementAlignment TOP = new ElementAlignment("TOP", Element.TOP);
  public static final ElementAlignment MIDDLE = new ElementAlignment("MIDDLE", Element.MIDDLE);
  public static final ElementAlignment BOTTOM = new ElementAlignment("BOTTOM", Element.BOTTOM);

  private final String myName; // for debug only
  private final int oldAlignment;

  private ElementAlignment(String name, int oldAlignment)
  {
    myName = name;
    this.oldAlignment = oldAlignment;
  }

  public String toString()
  {
    return myName;
  }

  public int getOldAlignment()
  {
    return oldAlignment;
  }
}
