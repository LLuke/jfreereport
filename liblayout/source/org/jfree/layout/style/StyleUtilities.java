/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.style;

public class StyleUtilities
{
  private StyleUtilities ()
  {
  }

  public static boolean getBooleanStyle (final StyleSheet sheet,
                                     final StyleKey key)
  {
    return getBooleanStyle(sheet, key, false);
  }

  public static boolean getBooleanStyle (final StyleSheet sheet,
                                       final StyleKey key,
                                       final boolean defaultValue)
  {
    final Boolean bool = (Boolean) sheet.getStyleProperty(key);
    if (bool == null)
    {
      return defaultValue;
    }
    return bool.booleanValue();
  }


  public static int getIntStyle (final StyleSheet sheet,
                                 final StyleKey key)
  {
    return getIntStyle(sheet, key, 0);
  }

  public static int getIntStyle (final StyleSheet sheet,
                                     final StyleKey key,
                                     final int defaultValue)
  {
    final Number f = (Number) sheet.getStyleProperty(key);
    if (f == null)
    {
      return defaultValue;
    }
    return f.intValue();
  }


  public static float getFloatStyle (final StyleSheet sheet,
                                     final StyleKey key)
  {
    return getFloatStyle(sheet, key, 0);
  }

  public static float getFloatStyle (final StyleSheet sheet,
                                     final StyleKey key,
                                     final float defaultValue)
  {
    final Number f = (Number) sheet.getStyleProperty(key);
    if (f == null)
    {
      return defaultValue;
    }
    return f.floatValue();
  }
}
