package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.TextElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class StringFieldReadHandler extends AbstractTextElementReadHandler
{
  private TextFieldElementFactory textFieldElementFactory;

  public StringFieldReadHandler ()
  {
    textFieldElementFactory = new TextFieldElementFactory();
  }

  protected StringFieldReadHandler (
          final TextFieldElementFactory textFieldElementFactory)
  {
    this.textFieldElementFactory = textFieldElementFactory;
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
      throw new ElementDefinitionException("Required attribute 'fieldname' is missing.");
    }
    textFieldElementFactory.setFieldname(fieldName);
    textFieldElementFactory.setNullString(atts.getValue("nullstring"));
  }

  protected TextElementFactory getTextElementFactory ()
  {
    return textFieldElementFactory;
  }
}
