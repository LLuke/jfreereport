package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.AnchorFieldElementFactory;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.ElementDefinitionException;
import org.xml.sax.Attributes;
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
  protected void startParsing (final Attributes atts)
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
