/**
 * Date: Jan 10, 2003
 * Time: 7:13:34 PM
 *
 * $Id: StylesHandler.java,v 1.2 2003/01/13 19:00:45 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Hashtable;

public class StylesHandler implements ReportDefinitionHandler
{
  public static final String STYLES_COLLECTION = "styles-collection";
  public static final String STYLE_TAG = "style";

  private Parser parser;
  private String finishTag;
  private Hashtable styleCollection;
  private ElementStyleSheet styleSheet;

  public StylesHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    styleCollection = (Hashtable) getParser().getConfigurationValue(STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
    }
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(STYLE_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
        throw new SAXException("Attribute 'name' is required");

      styleSheet = new ElementStyleSheet(name);

      StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getParser(), STYLE_TAG, styleSheet);
      getParser().pushFactory(styleSheetFactory);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "'");
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // no such events ...
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(STYLE_TAG))
    {
      styleCollection.put (styleSheet.getName(), styleSheet);
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "' or " +
                             finishTag + "', found : " +tagName);
    }
  }

  public Parser getParser()
  {
    return parser;
  }
}
