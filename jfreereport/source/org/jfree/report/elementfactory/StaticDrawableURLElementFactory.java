package org.jfree.report.elementfactory;

import java.net.URL;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableURLElementTemplate;

public class StaticDrawableURLElementFactory extends ElementFactory
{
  private String content;

  private URL baseURL;

  public StaticDrawableURLElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getContent()
  {
    return content;
  }

  /**
   * Defines the field name from where to read the content of the element.
   * The field name is the name of a datarow column.
   *
   * @param content the field name.
   */
  public void setContent(final String content)
  {
    this.content = content;
  }

  public URL getBaseURL ()
  {
    return baseURL;
  }

  public void setBaseURL (final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @see ElementFactory#createElement()
   *
   * @return the generated elements
   * @throws IllegalStateException if the field name is not set.
   */
  public Element createElement()
  {
    if (getContent() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DrawableURLElementTemplate fieldTemplate = new DrawableURLElementTemplate();
    fieldTemplate.setContent(getContent());
    fieldTemplate.setBaseURL(getBaseURL());
    element.setDataSource(fieldTemplate);

    return element;
  }

}
