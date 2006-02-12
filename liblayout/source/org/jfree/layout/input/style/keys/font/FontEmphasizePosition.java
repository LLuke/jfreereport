/**
 * Creation-Date: 26.10.2005, 14:21:47
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontEmphasizePosition extends CSSConstant
{
  public static final FontEmphasizePosition BEFORE =
          new FontEmphasizePosition("before");
  public static final FontEmphasizePosition AFTER =
          new FontEmphasizePosition("after");

  private FontEmphasizePosition(String name)
  {
    super(name);
  }
}
