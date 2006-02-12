package org.jfree.layouting.input.style.values;

/**
 * A numeric constant indicating an value that must be resolved during the
 * layouting process.
 *
 * @author Thomas Morgner
 */
public final class CSSAutoValue extends CSSConstant
{
  private static CSSAutoValue instance;

  public static synchronized CSSAutoValue getInstance()
  {
    if (instance == null)
    {
      instance = new CSSAutoValue();
    }
    return instance;
  }

  private CSSAutoValue()
  {
    super ("auto");
  }
}
