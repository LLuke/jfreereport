/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * HtmlStyleCollection.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlStyleCollection.java,v 1.11 2005/02/23 19:32:04 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.util.PaintUtilities;

/**
 * The HtmlStyleCollection is used to create HtmlCellStyles and to convert these cell
 * styles into Cascading StyleSheet code.
 * <p/>
 * The collection reuses previously generated styles to create optimized code.
 * <p/>
 * Created StyleSheets are stored in the collection and can be used as keys to lookup the
 * name of that style in the global style sheet.
 *
 * @author Thomas Morgner
 */
public class HtmlStyleCollection
{
  /**
   * contains all generated style sheets.
   */
  private final HashMap table;
  private final HashMap reverseTable;

  /**
   * the name counter helps to create unique names for the tablerow-styles.
   */
  private int rowCounter;
  /**
   * the name counter helps to create unique names for the tabledata-styles.
   */
  private int cellCounter;

  /**
   * the name counter helps to create unique names for the styles.
   */
  private int nameCounter;

  private static final String ROW_STYLE_PREFIX = "tr.";
  private static final String CELL_STYLE_PREFIX = "td.";
  private static final String GENERIC_STYLE_PREFIX = ".";


  /**
   * Creates a new HtmlStyleCollection.
   */
  public HtmlStyleCollection ()
  {
    this.table = new HashMap();
    this.reverseTable = new HashMap();
  }

  /**
   * Create a unique name for a new style sheet.
   *
   * @return the generated name.
   */
  private String createName ()
  {
    // the leading dot is important - it makes the style a generic class definition
    final String name = GENERIC_STYLE_PREFIX + "style-" + nameCounter;
    nameCounter++;
    return name;

  }

  /**
   * Adds the given style to the cache, if not already contained in the cache.
   *
   * @param style the generated style, that should be added to the style cache.
   * @return the registered name for the stylesheet.
   */
  public String addContentStyle (final HtmlContentStyle style)
  {
    String name = lookupName(style);
    if (name == null)
    {
      // Style did not change during the report processing, add it
      name = createName();
      table.put(style, name);
      reverseTable.put(name, style);
    }
//    else
//    {
//      Log.debug ("Already contained, will not add");
//    }
    return name;
  }

  /**
   * Checks, whether the given style is contained in the cache.
   *
   * @param style the style, that should be checked.
   * @return true, if the style is registered, false otherwise.
   */
  public boolean isRegistered (final HtmlStyle style)
  {
    final String name = lookupName(style);

    // if the table does not contain this style, it is not registered.
    if (name == null)
    {
      return false;
    }
    return true;
  }

  /**
   * Gets a enumeration of all defined styles.
   *
   * @return the styles as enumeration.
   */
  public Iterator getDefinedStyles ()
  {
    return table.keySet().iterator();
  }

  public TreeMap getSortedStyleMap ()
  {
    final TreeMap map = new TreeMap();
    final Iterator it = table.entrySet().iterator();
    while (it.hasNext())
    {
      final Map.Entry entry = (Map.Entry) it.next();
      map.put(entry.getValue(), entry.getKey());
    }
    return map;
  }

  /**
   * Try to find the registered name of the given style. Returns null, if the style is not
   * registered.
   *
   * @param name the name of the style, that should be looked up.
   * @return the style, or null, if the name is not registed.
   *
   * @see HtmlStyleCollection#isRegistered
   */
  public HtmlStyle lookupStyle (final String name)
  {
    return (HtmlStyle) reverseTable.get(name);
  }

  /**
   * Try to find the registered name of the given style. Returns null, if the style is not
   * registered.
   *
   * @param style the style, which should be looked up.
   * @return the registered name for this style, or null, if the style is not registed.
   *
   * @see HtmlStyleCollection#isRegistered
   */
  public String lookupName (final HtmlStyle style)
  {
    return (String) table.get(style);
  }

  public String getPublicName (final HtmlStyle style)
  {
    final String styleName = (String) table.get(style);
    if (styleName == null)
    {
      return null;
    }
    if (style instanceof HtmlTableCellStyle)
    {
      return styleName.substring(CELL_STYLE_PREFIX.length());
    }
    if (style instanceof HtmlTableRowStyle)
    {
      return styleName.substring(ROW_STYLE_PREFIX.length());
    }
    else
    {
      return styleName.substring(GENERIC_STYLE_PREFIX.length());
    }
  }

  /**
   * Removes all registered styles.
   */
  public void clear ()
  {
    table.clear();
    reverseTable.clear();
  }


  /**
   * Creates the color string for the given AWT color. If the color is one of the
   * predefined HTML colors, then the logical name is returned. For all other colors, the
   * RGB-Tripple is returned.
   *
   * @param color the AWTColor that should be translated.
   * @return the translated html color definition
   */
  public static String getColorString (final Color color)
  {
    try
    {
      return PaintUtilities.colorToString(color);
    }
    catch (Exception ofe)
    {
      //Log.debug ("Failed to compute the color value");
    }
    return null;
  }


  public String addRowStyle (final HtmlTableRowStyle style)
  {
    String name = (String) table.get(style);
    if (name == null)
    {
      name = ROW_STYLE_PREFIX + "row-style-" + rowCounter;
      table.put(style, name);
      reverseTable.put(name, style);
      rowCounter++;
    }
    return name;
  }

  public String addCellStyle (final HtmlTableCellStyle style)
  {
    String name = (String) table.get(style);
    if (name == null)
    {
      name = CELL_STYLE_PREFIX + "cell-style-" + cellCounter;
      table.put(style, name);
      reverseTable.put(name, style);
      cellCounter++;
    }
    return name;
  }
}
