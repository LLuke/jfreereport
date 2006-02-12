package org.jfree.layouting.input.style;

public class CSSCounterRule extends CSSDeclarationRule
{
  private String name;

  public CSSCounterRule (final StyleSheet parentStyle,
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
