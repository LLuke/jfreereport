/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ------------------------
 * CharacterEntityParser.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CharacterEntityParser.java,v 1.4 2003/02/05 21:48:27 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version
 * 27-Jan-2003 : Removed external file-depencies
 * 04-Feb-2003 : Code optimisation
 */
package com.jrefinery.report.util;

import java.util.Enumeration;
import java.util.Properties;

/**
 * The character entity parser replaces all known occurences of an entity
 * in the format &amp;entityname;.
 */
public class CharacterEntityParser
{
  /** the entities, keyed by entity name */
  private Properties entities;
  /** the reverse lookup entities, keyed by character */
  private Properties reverese;

  /**
   * Creates a new CharacterEntityParser and initializes the parser with
   * the given set of entities.
   *
   * @param characterEntities the entities used for the parser
   */
  protected CharacterEntityParser(Properties characterEntities)
  {
    entities = characterEntities;
    reverese = new Properties();
    Enumeration enum = entities.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      String value = entities.getProperty(key);
      reverese.setProperty(value, key);
    }
  }

  /**
   * create a new Character entity parser and initializes the parser with
   * the entities defined in the HTML4 standard.
   *
   * @return the CharacterEntityParser initialized with HTML4 entities.
   */
  public static CharacterEntityParser createHTMLEntityParser ()
  {
    return new CharacterEntityParser(new HTMLCharacterEntities());
  }

  /**
   * create a new Character entity parser and initializes the parser with
   * the entities defined in the XML standard.
   *
   * @return the CharacterEntityParser initialized with XML entities.
   */
  public static CharacterEntityParser createXMLEntityParser ()
  {
    Properties entities = new Properties();
    entities.setProperty("amp", "&");
    entities.setProperty("quot", "\"");
    entities.setProperty("lt", "<");
    entities.setProperty("gt", ">");
    entities.setProperty("apos", "\u0039");
    return new CharacterEntityParser(entities);
  }

  /**
   * returns the entities used in the parser.
   *
   * @return the properties for this parser.
   */
  private Properties getEntities()
  {
    return entities;
  }

  /**
   * returns the reverse-lookup table for the entities.
   *
   * @return the reverse-lookup properties for this parsers.
   */
  private Properties getReverese()
  {
    return reverese;
  }

  /**
   * Looks up the character for the entity name specified in <code>key</code>.
   *
   * @param key the entity name
   * @return the character as string with a length of 1
   */
  private String lookupCharacter(String key)
  {
    String val = getEntities().getProperty(key);
    if (val == null)
    {
      return key;
    }
    else
      return val;
  }

  /**
   * Performs a reverse lookup, to retrieve the entity name for a given character.
   *
   * @param character the character that should be translated into the entity
   * @return the entity name for the character or the untranslated character.
   */
  private String lookupEntity(String character)
  {
    String val = getReverese().getProperty(character);
    if (val == null)
    {
      return character;
    }
    else
      return "&" + val + ";";
  }

  /**
   * Encode the given String, so that all known entites are encoded. All characters
   * represented by these entites are now removed from the string.
   *
   * @param value the original string
   * @return the encoded string.
   */
  public String encodeEntities (String value)
  {
    StringBuffer b = new StringBuffer(value.length());
    for (int i = 0; i < value.length(); i++)
    {
      String character = String.valueOf (value.charAt(i));
      b.append(lookupEntity(character));
    }
    return b.toString();
  }

  /**
   * Decode the string, all known entities are replaced by their resolved characters.
   *
   * @param value the string that should be decoded.
   * @return the decoded string.
   */
  public String decodeEntities(String value)
  {
    int parserIndex = 0;
    int subStart;
    int subEnd;
    int subValue;
    String replaceString = null;
    StringBuffer bufValue = new StringBuffer(value);

    while (((subStart = value.indexOf("&", parserIndex)) != -1) && (subEnd = value.indexOf(";", parserIndex)) != -1)
    {
      parserIndex = subStart;
      StringBuffer buf = new StringBuffer();
      buf.append(bufValue.substring(subStart + 1, subEnd));
      if (buf.charAt(0) == '#')
      {
        buf.deleteCharAt(0);
        subValue = Integer.parseInt(buf.toString());
        if ((subValue >= 1) && (subValue <= 65536))
        {
          char[] chr = new char[1];
          chr[0] = (char) subValue;
          replaceString = new String(chr);
        }
      }
      else
      {
        replaceString = lookupCharacter(buf.toString());
      }
      if (replaceString != null)
      {
        replaceString = decodeEntities(replaceString);
        bufValue.replace(subStart, subEnd + 1, replaceString);
        parserIndex = parserIndex + replaceString.length();
      }
      value = bufValue.toString();
    }
    return bufValue.toString();
  }
}

