package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ResourceFieldElementFactory;
import org.jfree.report.elementfactory.ResourceLabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.PropertyStringReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class ResourceLabelReadHandler extends StringFieldReadHandler
{
  private PropertyStringReadHandler stringReadHandler;

  public ResourceLabelReadHandler ()
  {
    this(new ResourceFieldElementFactory());
  }

  protected ResourceLabelReadHandler (
          final TextFieldElementFactory textFieldElementFactory)
  {
    super(textFieldElementFactory);
    stringReadHandler = new PropertyStringReadHandler(null);
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
    final ResourceFieldElementFactory elementFactory = (ResourceFieldElementFactory) getElementFactory();
    elementFactory.setResourceBase(atts.getValue("resource-base"));
    getRootHandler().delegate(stringReadHandler, getTagName(), atts);
  }


  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    final ResourceLabelElementFactory elementFactory = (ResourceLabelElementFactory) getElementFactory();
    elementFactory.setResourceKey(stringReadHandler.getResult());
    super.doneParsing();
  }
}
