package org.jfree.layouting.input.style;

public class CSSStringRule extends CSSDeclarationRule
{
  private String name;

  public CSSStringRule (final StyleSheet parentStyle,
                         final StyleRule parentRule,
                         final String name)
  {
    super(parentStyle, parentRule);
    this.name = name;
  }

  public String getName ()
  {
    return name;
  }
}
