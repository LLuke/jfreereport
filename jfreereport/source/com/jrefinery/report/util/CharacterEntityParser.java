/*
 * Copyright (c) 1998, 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package com.jrefinery.report.util;

import java.util.Enumeration;
import java.util.Properties;

/**
 * The character entity parser replaces all known occurences of an entity
 * in the format &amp;entityname;.
 *
 * Abhängikeiten: Keine
 */
public class CharacterEntityParser
{
  private Properties entities;
  private Properties reverese;

  private CharacterEntityParser(Properties characterEntities)
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

  public static CharacterEntityParser createHTMLEntityParser ()
  {
    return new CharacterEntityParser(new HTMLCharacterEntities());
  }

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

  private Properties getEntities()
  {
    return entities;
  }

  private Properties getReverese()
  {
    return reverese;
  }

  public String lookupCharacter(String key)
  {
    String val = getEntities().getProperty(key);
    if (val == null)
    {
      return key;
    }
    else
      return val;
  }

  public String lookupEntity(String character)
  {
    String val = getReverese().getProperty(character);
    if (val == null)
    {
      return character;
    }
    else
      return "&" + val + ";";
  }

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

  public static void main (String [] args)
  {
    CharacterEntityParser html = createXMLEntityParser();

    Log.debug (html.decodeEntities(html.encodeEntities("The \" is encoded right")));
  }
}

