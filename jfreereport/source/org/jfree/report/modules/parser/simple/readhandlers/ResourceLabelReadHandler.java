package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.elementfactory.ResourceFieldElementFactory;
import org.jfree.report.elementfactory.ResourceLabelElementFactory;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.coretypes.StringReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ResourceLabelReadHandler extends StringFieldReadHandler
{
  private StringReadHandler stringReadHandler;

  public ResourceLabelReadHandler ()
  {
    this(new ResourceFieldElementFactory ());
  }

  protected ResourceLabelReadHandler (final TextFieldElementFactory textFieldElementFactory)
  {
    super(textFieldElementFactory);
    stringReadHandler = new StringReadHandler();
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
    final String text = stringReadHandler.getResult();
    final ResourceLabelElementFactory elementFactory = (ResourceLabelElementFactory) getElementFactory();
    elementFactory.setResourceKey(stringReadHandler.getResult());
  }
}
