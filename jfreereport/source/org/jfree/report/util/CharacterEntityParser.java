/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------------------
 * CharacterEntityParser.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CharacterEntityParser.java,v 1.2 2003/07/25 00:20:35 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version
 * 27-Jan-2003 : Removed external file-depencies
 * 04-Feb-2003 : Code optimisation
 */
package org.jfree.report.util;

import java.util.Enumeration;
import java.util.Properties;

/**
 * The character entity parser replaces all known occurrences of an entity
 * in the format &amp;entityname;.
 *
 * @author Thomas Morgner
 */
public class CharacterEntityParser
{
  /** the entities, keyed by entity name. */
  private Properties entities;

  /** the reverse lookup entities, keyed by character. */
  private Properties reverse;

  /**
   * Creates a new CharacterEntityParser and initializes the parser with
   * the given set of entities.
   *
   * @param characterEntities the entities used for the parser
   */
  public CharacterEntityParser(final Properties characterEntities)
  {
    entities = characterEntities;
    reverse = new Properties();
    final Enumeration enum = entities.keys();
    while (enum.hasMoreElements())
    {
      final String key = (String) enum.nextElement();
      final String value = entities.getProperty(key);
      reverse.setProperty(value, key);
    }
  }

  /**
   * create a new Character entity parser and initializes the parser with
   * the entities defined in the XML standard.
   *
   * @return the CharacterEntityParser initialized with XML entities.
   */
  public static CharacterEntityParser createXMLEntityParser()
  {
    final Properties entities = new Properties();
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
  private Properties getReverse()
  {
    return reverse;
  }

  /**
   * Looks up the character for the entity name specified in <code>key</code>.
   *
   * @param key the entity name
   * @return the character as string with a length of 1
   */
  private String lookupCharacter(final String key)
  {
    final String val = getEntities().getProperty(key);
    if (val == null)
    {
      return key;
    }
    else
    {
      return val;
    }
  }

  /**
   * Performs a reverse lookup, to retrieve the entity name for a given character.
   *
   * @param character the character that should be translated into the entity
   * @return the entity name for the character or the untranslated character.
   */
  private String lookupEntity(final String character)
  {
    final String val = getReverse().getProperty(character);
    if (val == null)
    {
      return null;
    }
    else
    {
      return "&" + val + ";";
    }
  }

  /**
   * Encode the given String, so that all known entites are encoded. All characters
   * represented by these entites are now removed from the string.
   *
   * @param value the original string
   * @return the encoded string.
   */
  public String encodeEntities(final String value)
  {
    final StringBuffer writer = new StringBuffer();
    for (int i = 0; i < value.length(); i++)
    {
      final String character = String.valueOf(value.charAt(i));
      final String lookup = lookupEntity(character);
      if (lookup == null)
      {
        writer.append(character);
      }
      else
      {
        writer.append(lookup);
      }
    }
    return writer.toString();
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
    final StringBuffer bufValue = new StringBuffer(value);

    while (((subStart = value.indexOf("&", parserIndex)) != -1)
        && (subEnd = value.indexOf(";", parserIndex)) != -1)
    {
      parserIndex = subStart;
      final StringBuffer buf = new StringBuffer();
      buf.append(bufValue.substring(subStart + 1, subEnd));
      if (buf.charAt(0) == '#')
      {
        buf.deleteCharAt(0);
        subValue = Integer.parseInt(buf.toString());
        if ((subValue >= 1) && (subValue <= 65536))
        {
          final char[] chr = new char[1];
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

