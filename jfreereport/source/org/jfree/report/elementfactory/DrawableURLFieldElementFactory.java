package org.jfree.report.elementfactory;

import java.net.URL;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableURLFieldTemplate;

public class DrawableURLFieldElementFactory extends ElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;

  private URL baseURL;

  public DrawableURLFieldElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname ()
  {
    return fieldname;
  }

  /**
   * Defines the field name from where to read the content of the element. The field name
   * is the name of a datarow column.
   *
   * @param fieldname the field name.
   */
  public void setFieldname (final String fieldname)
  {
    this.fieldname = fieldname;
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
   * @return the generated elements
   *
   * @throws IllegalStateException if the field name is not set.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final DrawableURLFieldTemplate fieldTemplate = new DrawableURLFieldTemplate();
    fieldTemplate.setField(getFieldname());
    fieldTemplate.setBaseURL(getBaseURL());
    element.setDataSource(fieldTemplate);

    return element;
  }

}
