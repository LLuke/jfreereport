/**
 * Date: Jan 10, 2003
 * Time: 6:53:26 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.targets.style.StyleKey;

public class BasicStyleKeyHandler implements ReportDefinitionHandler
{
  private String finishTag;
  private Parser parser;
  private StringBuffer buffer;
  private StyleKeyFactory keyfactory;
  private StyleKey key;

  public BasicStyleKeyHandler(Parser parser, String finishTag, String name)
    throws SAXException
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.buffer = new StringBuffer();
    keyfactory = (StyleKeyFactory)
        getParser().getConfigurationValue(ParserConfigHandler.STYLEKEY_FACTORY_TAG);
    key = keyfactory.getStyleKey(name);
    if (key == null) throw new SAXException("The defined StyleKey is invalid: " + name);

  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    throw new SAXException("Element '" + finishTag + "' has no child-elements.");
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    buffer.append(ch, start,  length);
  }

  public StyleKeyFactory getKeyfactory()
  {
    return keyfactory;
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag) == false)
    {
      throw new SAXException("Expected tag '" + finishTag + "'");
    }
    getParser().popFactory().endElement(tagName);
  }

  public StyleKey getStyleKey ()
  {
    return key;
  }

  public Object getValue () throws SAXException
  {
    return keyfactory.createBasicObject(key, buffer.toString());
  }

  public Parser getParser()
  {
    return parser;
  }

  protected String getFinishTag()
  {
    return finishTag;
  }
}
