/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------
 * StylesHandler.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id:$
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Hashtable;

/**
 * A style sheet handler.
 * 
 * @author Thomas Morgner.
 */
public class StyleSheetHandler implements ReportDefinitionHandler
{
  /** The 'compound-key' tag name. */
  public static final String COMPOUND_KEY_TAG = "compound-key";
  
  /** The 'basic-key' tag name. */
  public static final String BASIC_KEY_TAG = "basic-key";
  
  /** The 'extends' tag name. */
  public static final String EXTENDS_TAG = "extends";

  /** The parser. */
  private Parser parser;
  
  /** The finish tag. */
  private String finishTag;
  
  /** The element style sheet. */
  private ElementStyleSheet sheet;

  /** The basic style key handler. */
  private BasicStyleKeyHandler basicFactory;
  
  /** The style collection. */
  private Hashtable styleCollection;

  /** 
   * Creates a new handler.
   * 
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   * @param styleSheet  the style sheet.
   */
  public StyleSheetHandler(Parser parser, String finishTag, ElementStyleSheet styleSheet)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.sheet = styleSheet;
    styleCollection = (Hashtable) getParser().getConfigurationValue(
        StylesHandler.STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
    }
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   * 
   * @throws SAXException ??.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(BASIC_KEY_TAG))
    {
      String name = attrs.getValue("name");
      if (name == null)
      {
        throw new SAXException ("Attribute 'name' is missing.");
      }
      String className = attrs.getValue("class");
      Class c = null;
      try
      {
        c = getClass().getClassLoader().loadClass(className);
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
      {
        throw new SAXException ("Attribute 'name' is missing.");
      }
      String className = attrs.getValue("class");
      Class c = null;
      try
      {
        c = getClass().getClassLoader().loadClass(className);
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
        {
          throw new SAXException("Invalid parent styleSheet, StyleSheet not defined: " + extend);
        }
        sheet.addParent(exSheet);
      }
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + BASIC_KEY_TAG + ", "
                              + COMPOUND_KEY_TAG + ". ");
    }
  }

  /**
   * Callback to indicate that some character data has been read.
   * 
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */  
  public void characters(char ch[], int start, int length)
  {
    // no characters expected here ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * 
   * @throws SAXException ??.
   */
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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
                              + BASIC_KEY_TAG + ", "
                              + COMPOUND_KEY_TAG + ", "
                              + finishTag);
    }
  }

  /**
   * Returns the parser.
   * 
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }

  /**
   * Sets the parser.
   * 
   * @param parser  the parser.
   */
  public void setParser(Parser parser)
  {
    this.parser = parser;
  }
}
