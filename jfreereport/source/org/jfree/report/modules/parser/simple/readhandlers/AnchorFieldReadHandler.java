package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.AnchorFieldElementFactory;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class AnchorFieldReadHandler extends AbstractElementReadHandler
{
  private AnchorFieldElementFactory elementFactory;

  public AnchorFieldReadHandler ()
  {
    elementFactory = new AnchorFieldElementFactory();
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
    final String fieldName = atts.getValue("fieldname");
    if (fieldName == null)
    {
      throw new ElementDefinitionException("Required attribute 'fieldname' is missing.");
    }
    elementFactory.setFieldname(fieldName);
    elementFactory.setName(atts.getValue(NAME_ATT));
    elementFactory.setAbsolutePosition(getElementPosition(atts));
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
