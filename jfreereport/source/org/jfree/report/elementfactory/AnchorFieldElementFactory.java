package org.jfree.report.elementfactory;

import org.jfree.report.AnchorElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.AnchorFieldTemplate;

public class AnchorFieldElementFactory extends ElementFactory
{
  private String fieldname;

  public AnchorFieldElementFactory ()
  {
  }

  public String getFieldname ()
  {
    return fieldname;
  }

  public void setFieldname (final String field)
  {
    this.fieldname = field;
  }

  /**
   * Creates a new instance of the element.
   *
   * @return the newly generated instance of the element.
   */
  public Element createElement ()
  {
    final AnchorElement element = new AnchorElement();
    final AnchorFieldTemplate anchorFieldTemplate = new AnchorFieldTemplate();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(anchorFieldTemplate);
    return element;
  }
}
