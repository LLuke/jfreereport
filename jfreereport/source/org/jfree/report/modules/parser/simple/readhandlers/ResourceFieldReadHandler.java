package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.elementfactory.ResourceFieldElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ResourceFieldReadHandler extends StringFieldReadHandler
{
  public ResourceFieldReadHandler ()
  {
    super(new ResourceFieldElementFactory ());
  }

  protected ResourceFieldReadHandler (final TextFieldElementFactory textFieldElementFactory)
  {
    super(textFieldElementFactory);
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
    final ResourceFieldElementFactory elementFactory = (ResourceFieldElementFactory) getElementFactory();
    elementFactory.setResourceBase(atts.getValue("resource-base"));
  }
}
