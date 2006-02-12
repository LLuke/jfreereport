/**
 * Creation-Date: 26.10.2005, 14:20:40
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontEmphasizeStyle extends CSSConstant
{
  public static final FontEmphasizeStyle NONE =
          new FontEmphasizeStyle("none");
  public static final FontEmphasizeStyle ACCENT =
          new FontEmphasizeStyle("accent");
  public static final FontEmphasizeStyle DOT =
          new FontEmphasizeStyle("dot");
  public static final FontEmphasizeStyle CIRCLE =
          new FontEmphasizeStyle("circle");
  public static final FontEmphasizeStyle DISC =
          new FontEmphasizeStyle("disc");

  private FontEmphasizeStyle(String name)
  {
    super(name);
  }
}
