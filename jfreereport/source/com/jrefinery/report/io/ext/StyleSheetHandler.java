/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.io.ext.BasicStyleKeyHandler;
import com.jrefinery.report.io.ext.CompoundStyleKeyHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Used for the styles definition ...
 */
public class StyleSheetHandler implements ReportDefinitionHandler
{
  public static final String COMPOUND_KEY_TAG = "compound-key";
  public static final String BASIC_KEY_TAG = "basic-key";

  private Parser parser;
  private String finishTag;
  private ElementStyleSheet sheet;
  private BasicStyleKeyHandler basicFactory;

  public StyleSheetHandler(Parser parser, String finishTag, ElementStyleSheet styleSheet)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.sheet = styleSheet;
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(BASIC_KEY_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
        throw new SAXException ("Attribute 'name' is missing.");

      basicFactory = new BasicStyleKeyHandler(getParser(), tagName, name);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_KEY_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
        throw new SAXException ("Attribute 'name' is missing.");

      basicFactory = new CompoundStyleKeyHandler(getParser(), tagName, name);
      getParser().pushFactory(basicFactory);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              BASIC_KEY_TAG + ", " +
                              COMPOUND_KEY_TAG + ". ");
    }
  }

  public void characters(char ch[], int start, int length)
  {
    // no characters expected here ...
  }

  public void endElement(String tagName)
    throws SAXException
  {
    if (tagName.equals(BASIC_KEY_TAG))
    {
      sheet.setStyleProperty(basicFactory.getStyleKey(), basicFactory.getValue());
      basicFactory = null;
    }
    else if (tagName.equals(COMPOUND_KEY_TAG))
    {
      sheet.setStyleProperty(basicFactory.getStyleKey(), basicFactory.getValue());
      basicFactory = null;
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              BASIC_KEY_TAG + ", " +
                              COMPOUND_KEY_TAG + ", " +
                              finishTag);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  public void setParser(Parser parser)
  {
    this.parser = parser;
  }
}
