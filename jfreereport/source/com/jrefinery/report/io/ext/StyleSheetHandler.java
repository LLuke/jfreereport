/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id: StyleSheetHandler.java,v 1.2 2003/01/13 19:00:44 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.io.ext.BasicStyleKeyHandler;
import com.jrefinery.report.io.ext.CompoundStyleKeyHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Hashtable;

/**
 * Used for the styles definition ...
 */
public class StyleSheetHandler implements ReportDefinitionHandler
{
  public static final String COMPOUND_KEY_TAG = "compound-key";
  public static final String BASIC_KEY_TAG = "basic-key";
  public static final String EXTENDS_TAG = "extends";

  private Parser parser;
  private String finishTag;
  private ElementStyleSheet sheet;
  private BasicStyleKeyHandler basicFactory;
  private Hashtable styleCollection;

  public StyleSheetHandler(Parser parser, String finishTag, ElementStyleSheet styleSheet)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.sheet = styleSheet;
    styleCollection = (Hashtable) getParser().getConfigurationValue(StylesHandler.STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
    }
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(BASIC_KEY_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
        throw new SAXException ("Attribute 'name' is missing.");

      String className = attrs.getValue("class");
      Class c = null;
      try
      {
        c = Class.forName(className);
      }
      catch (Exception e)
      {
        // ignore me ...
      }

      basicFactory = new BasicStyleKeyHandler(getParser(), tagName, name, c);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_KEY_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
        throw new SAXException ("Attribute 'name' is missing.");

      String className = attrs.getValue("class");
      Class c = null;
      try
      {
        c = Class.forName(className);
      }
      catch (Exception e)
      {
        // ignore me ...
      }

      basicFactory = new CompoundStyleKeyHandler(getParser(), tagName, name, c);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(EXTENDS_TAG))
    {
      String extend = attrs.getValue("name");
      if (extend != null)
      {
        ElementStyleSheet exSheet = (ElementStyleSheet) styleCollection.get(extend);
        if (exSheet == null)
          throw new SAXException("Invalid parent styleSheet, StyleSheet not defined");

        sheet.addParent(exSheet);
      }
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
