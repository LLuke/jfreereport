/**
 * Creation-Date: 26.10.2005, 14:47:20
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontStyle extends CSSConstant
{
  public static final FontStyle NORMAL = new FontStyle("normal");
  public static final FontStyle OBLIQUE = new FontStyle("oblique");
  public static final FontStyle ITALIC = new FontStyle("italic");

  private FontStyle(String name)
  {
    super(name);
  }
}
