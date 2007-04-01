/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.common;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.jfree.xmlns.parser.ParseException;

/**
 * Basic helper functions to ease up the process of parsing.
 *
 * @author Thomas Morgner
 */
public class ParserUtil
{

  private ParserUtil()
  {
  }

  /**
   * Parses the string <code>text</code> into an int. If text is null or does
   * not contain a parsable value, the message given in <code>message</code>
   * is used to throw a SAXException.
   *
   * @param text    the text to parse.
   * @param message the error message if parsing fails.
   * @return the int value.
   * @throws SAXException if there is a problem with the parsing.
   */
  public static int parseInt(final String text,
                             final String message,
                             final Locator locator)
      throws SAXException
  {
    if (text == null)
    {
      throw new SAXException(message);
    }

    try
    {
      return Integer.parseInt(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new ParseException("NumberFormatError: " + message, locator);
    }
  }

  /**
   * Parses the string <code>text</code> into an int. If text is null or does
   * not contain a parsable value, the message given in <code>message</code>
   * is used to throw a SAXException.
   *
   * @param text    the text to parse.
   * @param message the error message if parsing fails.
   * @return the int value.
   * @throws SAXException if there is a problem with the parsing.
   */
  public static int parseInt(final String text, final String message)
      throws SAXException
  {
    if (text == null)
    {
      throw new SAXException(message);
    }

    try
    {
      return Integer.parseInt(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException("NumberFormatError: " + message);
    }
  }

  /**
   * Parses an integer.
   *
   * @param text       the text to parse.
   * @param defaultVal the default value.
   * @return the integer.
   */
  public static int parseInt(final String text, final int defaultVal)
  {
    if (text == null)
    {
      return defaultVal;
    }

    try
    {
      return Integer.parseInt(text);
    }
    catch (NumberFormatException nfe)
    {
      return defaultVal;
    }
  }

  /**
   * Parses the string <code>text</code> into an float. If text is null or
   * does not contain a parsable value, the message given in
   * <code>message</code> is used to throw a SAXException.
   *
   * @param text    the text to parse.
   * @param message the error message if parsing fails.
   * @return the float value.
   * @throws SAXParseException if there is a problem with the parsing.
   */
  public static float parseFloat(final String text,
                                 final String message,
                                 final Locator locator)
      throws ParseException
  {
    if (text == null)
    {
      throw new ParseException(message, locator);
    }
    try
    {
      return Float.parseFloat(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new ParseException("NumberFormatError: " + message, locator);
    }
  }

  /**
   * Parses the string <code>text</code> into an float. If text is null or
   * does not contain a parsable value, the message given in
   * <code>message</code> is used to throw a SAXException.
   *
   * @param text    the text to parse.
   * @param message the error message if parsing fails.
   * @return the float value.
   * @throws SAXException if there is a problem with the parsing.
   */
  public static float parseFloat(final String text, final String message)
      throws SAXException
  {
    if (text == null)
    {
      throw new SAXException(message);
    }
    try
    {
      return Float.parseFloat(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException("NumberFormatError: " + message);
    }
  }

  /**
   * Parses the string <code>text</code> into an float. If text is null or
   * does not contain a parsable value, the message given in
   * <code>message</code> is used to throw a SAXException.
   *
   * @param text       the text to parse.
   * @param defaultVal the defaultValue returned if parsing fails.
   * @return the float value.
   */
  public static float parseFloat(final String text, final float defaultVal)
  {
    if (text == null)
    {
      return defaultVal;
    }
    try
    {
      return Float.parseFloat(text);
    }
    catch (NumberFormatException nfe)
    {
      return defaultVal;
    }
  }

  /**
   * Parses a boolean. If the string <code>text</code> contains the value of
   * "true", the true value is returned, else false is returned.
   *
   * @param text       the text to parse.
   * @param defaultVal the default value.
   * @return a boolean.
   */
  public static boolean parseBoolean(final String text,
                                     final boolean defaultVal)
  {
    if (text == null)
    {
      return defaultVal;
    }
    return text.equalsIgnoreCase("true");
  }


  /**
   * Translates an boolean string ("true" or "false") into the corresponding Boolean
   * object.
   *
   * @param value the string that represents the boolean.
   * @return Boolean.TRUE or Boolean.FALSE
   *
   * @throws SAXException if an parse error occured.
   */
  public static Boolean parseBoolean (final String value, Locator locator)
          throws SAXException
  {
    if (value == null)
    {
      return null;
    }
    if (value.equals("true"))
    {
      return Boolean.TRUE;
    }
    else if (value.equals("false"))
    {
      return Boolean.FALSE;
    }
    throw new ParseException("Failed to parse value: Expected 'true' or 'false'", locator);
  }

  /**
   * Parses a string. If the <code>text</code> is null, defaultval is
   * returned.
   *
   * @param text       the text to parse.
   * @param defaultVal the default value.
   * @return a string.
   */
  public static String parseString(final String text, final String defaultVal)
  {
    if (text == null)
    {
      return defaultVal;
    }
    return text;
  }

}
