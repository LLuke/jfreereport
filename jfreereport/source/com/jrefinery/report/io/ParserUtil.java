/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ParserUtil.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 2002-05-21: Contains utility functions to make parsing easier.
 */
package com.jrefinery.report.io;

import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;

public class ParserUtil
{
  public static int parseInt (String text, String message) throws SAXException
  {
    if (text == null)
      throw new SAXException (message);

    try
    {
      return Integer.parseInt (text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException ("NumberFormatError: " + message);
    }
  }

  public static float parseFloat (String text, String message) throws SAXException
  {
    if (text == null)
      throw new SAXException (message);
    try
    {
      return Float.parseFloat (text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException ("NumberFormatError: " + message);
    }
  }

  public static boolean parseBoolean (String text, boolean defaultVal)
  {
    if (text == null)
      return defaultVal;
    return text.equalsIgnoreCase ("true");
  }

  public static String parseString (String text, String defaultVal)
  {
    if (text == null)
      return defaultVal;
    return text;
  }

  public static Stroke parseStroke (String weight)
  {
    try
    {
      if (weight != null)
      {
        Float w = new Float (weight);
        return new BasicStroke (w.floatValue ());
      }
    }
    catch (NumberFormatException nfe)
    {
      Log.debug ("Invalid weight for line element", nfe);
    }
    return new BasicStroke (1);
  }

  public static Paint parseColor (String color)
  {
    if (color == null)
      return Color.black;

    try
    {
      // get color by hex or octal value
      return Color.decode (color);
    }
    catch (NumberFormatException nfe)
    {
      // if we can't decode lets try to get it by name
      try
      {
        // try to get a color by name using reflection
        // black is used for an instance and not for the color itselfs
        Field f = Color.class.getField (color);

        return (Color) f.get (null);
      }
      catch (Exception ce)
      {
        System.out.println ("No such Color : " + color);
        // if we can't get any color return black
        return Color.black;
      }
    }
  }


  /**
   * parses a position of an element. If a relative postion is given, the returnvalue
   * is a negative number between 0 and -100.
   */
  public static float parseRelativeFloat (String value, String exceptionMessage) throws SAXException
  {
    if (value == null) throw new SAXException (exceptionMessage);
    String tvalue = value.trim ();
    if (tvalue.endsWith ("%"))
    {
      String number = tvalue.substring (0, tvalue.indexOf ("%"));
      float f = parseFloat (number, exceptionMessage) * -1.0f;
      Log.debug ("  Parsed Relative value: " + f);
      return f;
    }
    else
      return parseFloat (tvalue, exceptionMessage);
  }

  public static Rectangle2D getElementPosition (Attributes atts) throws SAXException
  {
    float x = ParserUtil.parseRelativeFloat (atts.getValue ("x"), "Element x not specified");
    float y = ParserUtil.parseRelativeFloat (atts.getValue ("y"), "Element y not specified");
    float w = ParserUtil.parseRelativeFloat (atts.getValue ("width"), "Element width not specified");
    float h = ParserUtil.parseRelativeFloat (atts.getValue ("height"), "Element height not specified");
    if (w == 0) Log.warn ("Element width is 0. Use xxx% to specify a relative width.");
    Rectangle2D.Float retval = new Rectangle2D.Float (x, y, w, h);
    return retval;
  }
}
