package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.ShapeFieldElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ShapeFieldReadHandler extends AbstractShapeElementReadHandler
{
  private ShapeFieldElementFactory elementFactory;

  public ShapeFieldReadHandler ()
  {
    this.elementFactory = new ShapeFieldElementFactory();
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);

    final String fieldName = atts.getValue("fieldname");
    if (fieldName == null)
    {
      throw new ElementDefinitionException ("Required attribute 'fieldname' is missing.");
    }
    elementFactory.setFieldname(fieldName);
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
