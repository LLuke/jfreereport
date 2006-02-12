package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Describes how replaced content should be scaled if neither the width or
 * height of an element is set to 'auto'.
 *
 * @author Thomas Morgner
 */
public class Fit extends CSSConstant
{
  public static final Fit FILL = new Fit("fill");
  public static final Fit NONE = new Fit("none");
  public static final Fit MEET = new Fit("meet");
  public static final Fit SLICE = new Fit("slice");

  private Fit(String name)
  {
    super(name);
  }
}
