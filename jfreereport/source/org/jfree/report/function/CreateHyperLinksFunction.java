package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.Element;

public class CreateHyperLinksFunction extends AbstractElementFormatFunction
{
  private String field;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public CreateHyperLinksFunction ()
  {
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return null;
  }

  public String getField ()
  {
    return field;
  }

  public void setField (final String field)
  {
    this.field = field;
  }

  protected void processRootBand (final Band b)
  {
    final Object targetRaw = getDataRow().get(getField());
    if (targetRaw == null)
    {
      return;
    }
    final String target = String.valueOf(targetRaw);
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].setHRefTarget(target);
    }
  }
}
