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
 * --------------------------
 * CharacterEntityParser.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CharacterEntityParser.java,v 1.10 2003/05/11 13:39:20 taqua Exp $
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
  protected CharacterEntityParser(Properties characterEntities)
  {
    entities = characterEntities;
    reverse = new Properties();
    Enumeration enum = entities.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      String value = entities.getProperty(key);
      reverse.setProperty(value, key);
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
  private String lookupCharacter(String key)
  {
    String val = getEntities().getProperty(key);
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
  private String lookupEntity(String character)
  {
    String val = getReverse().getProperty(character);
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
  public String encodeEntities (String value)
  {
    StringBuffer writer = new StringBuffer();
    for (int i = 0; i < value.length(); i++)
    {
      String character = String.valueOf (value.charAt(i));
      String lookup = lookupEntity(character);
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
   * We assume, that both writers access the same stream and that both
   * of them are unbuffered.
   *
   * @param value the string that should be encoded
   * @param writer the writer which should receive the encoded contents.
   */
  public void encodeEntities (String value, HtmlWriter writer)
  {
    for (int i = 0; i < value.length(); i++)
    {
      String character = String.valueOf (value.charAt(i));
      String lookup = lookupEntity(character);
      if (lookup == null)
      {
        writer.printEncoded(character);
      }
      else
      {
        writer.print(lookup);
      }
    }
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

    while (((subStart = value.indexOf("&", parserIndex)) != -1)
           && (subEnd = value.indexOf(";", parserIndex)) != -1)
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
/*
  public static void main (String [] args) throws Exception
  {
    String test = "Test is a הצüהצü test";
    OutputStreamWriter w = new OutputStreamWriter(System.err, "UTF-16");
    w.write(test);
    w.flush();

    CharacterEntityParser ep = CharacterEntityParser.createHTMLEntityParser();
    System.out.println ("First Test: " + ep.encodeEntities(test));
    System.out.println ("Second Test: " );
    ep.encodeEntities(test, new HtmlWriter (System.err, "UTF-16"));

  }
*/
}

