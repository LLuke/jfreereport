package org.jfree.report.elementfactory;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.ui.Drawable;

public class StaticDrawableElementFactory extends ElementFactory
{
  private Drawable content;

  public StaticDrawableElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public Drawable getContent ()
  {
    return content;
  }

  /**
   * Defines the field name from where to read the content of the element. The field name
   * is the name of a datarow column.
   *
   * @param content the field name.
   */
  public void setContent (final Drawable content)
  {
    this.content = content;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @return the generated elements
   *
   * @throws IllegalStateException if the field name is not set.
   * @see ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getContent() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final StaticDataSource dataSource = new StaticDataSource(content);
    element.setDataSource(dataSource);

    return element;
  }

}
