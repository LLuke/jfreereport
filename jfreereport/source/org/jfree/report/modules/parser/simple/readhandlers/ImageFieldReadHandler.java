package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.elementfactory.ImageFieldElementFactory;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ImageFieldReadHandler
        extends AbstractImageElementReadHandler
{
  private ImageFieldElementFactory elementFactory;

  public ImageFieldReadHandler ()
  {
    this.elementFactory = new ImageFieldElementFactory();
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
    handleFieldName (atts);
  }

  private void handleFieldName (final Attributes atts) throws ElementDefinitionException
  {
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
