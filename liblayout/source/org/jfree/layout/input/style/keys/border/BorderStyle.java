package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:37:35
 *
 * @author Thomas Morgner
 */
public class BorderStyle extends CSSConstant
{
  public static final BorderStyle NONE = new BorderStyle("none");
  public static final BorderStyle HIDDEN = new BorderStyle("hidden");
  public static final BorderStyle DOTTED = new BorderStyle("dotted");
  public static final BorderStyle DASHED = new BorderStyle("dashed");
  public static final BorderStyle SOLID = new BorderStyle("solid");
  public static final BorderStyle DOUBLE = new BorderStyle("double");
  public static final BorderStyle DOT_DASH = new BorderStyle("dot-dash");
  public static final BorderStyle DOT_DOT_DASH = new BorderStyle("dot-dot-dash");
  public static final BorderStyle WAVE = new BorderStyle("wave");
  public static final BorderStyle GROOVE = new BorderStyle("groove");
  public static final BorderStyle RIDGE = new BorderStyle("ridge");
  public static final BorderStyle INSET = new BorderStyle("inset");
  public static final BorderStyle OUTSET = new BorderStyle("outset");

  private BorderStyle(String name)
  {
    super(name);
  }
}
