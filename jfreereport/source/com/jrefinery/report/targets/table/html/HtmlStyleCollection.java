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
 * ------------------------
 * HtmlStyleCollection.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlStyleCollection.java,v 1.14 2003/05/02 12:40:40 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.table.TableCellBackground;
import org.jfree.xml.factory.objects.ColorObjectDescription;

/**
 * The HtmlStyleCollection is used to create HtmlCellStyles and to convert these
 * cell styles into Cascading StyleSheet code.
 * <p>
 * The collection reuses previously generated styles to create optimized code.
 *
 * @author Thomas Morgner
 */
public class HtmlStyleCollection
{
  /**
   * the ObjectDescription for color objects is used to translate colors into names or
   * RGB-values.
   */
  private ColorObjectDescription colorObjectDescription;

  /** contains all generated style sheets. */
  private HashMap table;

  /** the name counter helps to create unique names for the styles. */
  private int nameCounter;

  /**
   * Creates a new HtmlStyleCollection.
   */
  public HtmlStyleCollection()
  {
    this.colorObjectDescription = new ColorObjectDescription();
    this.table = new HashMap();
  }

  /**
   * Create a unique name for a new style sheet.
   *
   * @return the generated name.
   */
  private String createName()
  {
    String name = "style-" + nameCounter;
    nameCounter++;
    return name;

  }

  /**
   * Adds the given style to the cache, if not already contained in the cache.
   *
   * @param style the generated style, that should be added to the style cache.
   * @return the registered name for the stylesheet.
   */
  public String addStyle(HtmlCellStyle style)
  {
    String name = lookupName(style);
    if (name == null)
    {
      // Style did not change during the report processing, add it
      name = createName();
      table.put(style, name);
    }
    return name;
  }

  /**
   * Checks, whether the given style is contained in the cache.
   *
   * @param style the style, that should be checked.
   * @return true, if the style is registered, false otherwise.
   */
  public boolean isRegistered(HtmlCellStyle style)
  {
    String name = lookupName(style);

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
  public Iterator getDefinedStyles()
  {
    return table.keySet().iterator();
  }

  /**
   * Try to find the registered name of the given style. Returns null,
   * if the style is not registered.
   *
   * @param style the style, which should be looked up.
   * @return the registered name for this style, or null, if the style is not registed.
   * @see HtmlStyleCollection#isRegistered
   */
  public String lookupName(HtmlCellStyle style)
  {
    return (String) table.get(style);
  }

  /**
   * Removes all registered styles.
   */
  public void clear()
  {
    table.clear();
  }

  /**
   * Translates the font name of the FontDefinition into the HTML-Font name.
   * If the fontdefinition describes a logical font, then the html font name
   * for that logical font is returned.
   *
   * @param font the font definition.
   * @return the translated html font name.
   */
  private String translateFontName(FontDefinition font)
  {
    if (font.isCourier())
    {
      return "monospaced";
    }
    if (font.isSerif())
    {
      return "serif";
    }
    if (font.isSansSerif())
    {
      return "sans-serif";
    }
    return "'" + font.getFontName() + "'";
  }

  /**
   * Transforms the given HtmlCellStyle into a Cascading StyleSheet definition.
   *
   * @param style the HtmlCellStyle, that should be translated.
   * @return the generated stylesheet definition.
   */
  public String createStyleSheetDefinition(HtmlCellStyle style)
  {
    FontDefinition font = style.getFont();
    String colorValue = getColorString(style.getFontColor());

    StringBuffer b = new StringBuffer();
    b.append("font-family:");
    b.append(translateFontName(font));
    b.append("; font-size:");
    b.append(font.getFontSize());
    b.append("pt");
    if (font.isBold())
    {
      b.append("; font-weight:bold");
    }
    if (font.isItalic())
    {
      b.append("; font-style:italic");
    }
    if (font.isUnderline() && font.isStrikeThrough())
    {
      b.append("; text-decoration:underline,line-through");
    }
    else if (font.isUnderline())
    {
      b.append("; text-decoration:underline");
    }
    else if (font.isStrikeThrough())
    {
      b.append("; text-decoration:line-through");
    }
    if (colorValue != null)
    {
      b.append("; color:");
      b.append(colorValue);
    }

    b.append("; vertical-align:");
    b.append(translateVerticalAlignment(style.getVerticalAlignment()));
    b.append("; text-align:");
    b.append(translateHorizontalAlignment(style.getHorizontalAlignment()));
    b.append(";");
    return b.toString();
  };

  /**
   * Translates the JFreeReport horizontal element alignment into a
   * HTML alignment constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  private String translateHorizontalAlignment(ElementAlignment ea)
  {
    if (ea == ElementAlignment.RIGHT)
    {
      return "right";
    }
    if (ea == ElementAlignment.CENTER)
    {
      return "center";
    }
    return "left";
  }

  /**
   * Translates the JFreeReport vertical element alignment into a
   * HTML alignment constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  private String translateVerticalAlignment(ElementAlignment ea)
  {
    if (ea == ElementAlignment.BOTTOM)
    {
      return "bottom";
    }
    if (ea == ElementAlignment.MIDDLE)
    {
      return "middle";
    }
    return "top";
  }

  /**
   * Creates the color string for the given AWT color. If the color is one of
   * the predefined HTML colors, then the logical name is returned. For all other
   * colors, the RGB-Tripple is returned.
   *
   * @param color the AWTColor that should be translated.
   * @return the translated html color definition
   */
  private String getColorString(Color color)
  {
    try
    {
      colorObjectDescription.setParameterFromObject(color);
      return (String) colorObjectDescription.getParameter("value");
    }
    catch (Exception ofe)
    {
      //Log.debug ("Failed to compute the color value");
    }
    return null;
  }

  /**
   * Transforms the given TableCellBackground into a Cascading StyleSheet definition.
   *
   * @param bg the background definition, that should be translated.
   * @return the generated stylesheet definition.
   */
  public String getBackgroundStyle(TableCellBackground bg)
  {
    ArrayList style = new ArrayList();
    Color c = bg.getColor();
    if (c != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("background-color:");
      b.append(getColorString(c));
      style.add(b.toString());
    }

    if (bg.getColorTop() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("border-top: ");
      b.append(bg.getBorderSizeTop());
      b.append("pt solid ");
      b.append(getColorString(bg.getColorTop()));
      style.add(b.toString());
    }

    if (bg.getColorBottom() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("border-bottom: ");
      b.append(bg.getBorderSizeBottom());
      b.append("pt solid ");
      b.append(getColorString(bg.getColorBottom()));
      style.add(b.toString());
    }

    if (bg.getColorLeft() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("border-left: ");
      b.append(bg.getBorderSizeLeft());
      b.append("pt solid ");
      b.append(getColorString(bg.getColorLeft()));
      style.add(b.toString());
    }

    if (bg.getColorRight() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append("border-right: ");
      b.append(bg.getBorderSizeRight());
      b.append("pt solid ");
      b.append(getColorString(bg.getColorRight()));
      style.add(b.toString());
    }

    StringBuffer b = new StringBuffer();
    Iterator styles = style.iterator();
    while (styles.hasNext())
    {
      b.append(styles.next());
      if (styles.hasNext())
      {
        b.append("; ");
      }
    }

    return b.toString();
  }
}
