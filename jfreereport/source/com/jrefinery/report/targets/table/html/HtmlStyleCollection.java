/**
 * Date: Jan 25, 2003
 * Time: 6:17:21 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.StyleSheet;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.io.ext.factory.objects.ColorObjectDescription;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

public class HtmlStyleCollection
{
  private ColorObjectDescription colorObjectDescription;
  private Hashtable table;
  private ArrayList styleBlacklist;

  public HtmlStyleCollection()
  {
    this.colorObjectDescription = new ColorObjectDescription();
    this.table = new Hashtable();
    this.styleBlacklist = new ArrayList();
  }

  /**
   *
   * @param style
   * @param name
   */
  public void addStyle (HtmlCellStyle style, String name)
  {
    if (table.containsValue(name))
    {
      // stylesheet already registed. blacklist it ...
      if (isBlacklisted(name) == false)
        styleBlacklist.add (name);
    }
    else
    {
      // Style did not change during the report processing, add it
      table.put(style, name);
    }
  }

  public boolean isBlacklisted(String name)
  {
    return styleBlacklist.contains(name);
  }

  public boolean isRegistered(HtmlCellStyle style)
  {
    String name = lookupName(style);

    // if the table does not contain this style, it is not registered.
    if (name == null)
      return false;

    // if the style is blacklisted, it is treated as if it is not registered
    return isBlacklisted(name);
  }

  public Enumeration getDefinedStyles ()
  {
    return table.keys();
  }

  public String lookupName (HtmlCellStyle style)
  {
    return (String) table.get(style);
  }

  public void clear ()
  {
    table.clear();
    styleBlacklist.clear();
  }

  public String createStyleSheetDefinition (HtmlCellStyle style)
  {
    FontDefinition font = style.getFont();
    String colorValue = getColorString(style.getFontColor());

    StringBuffer b = new StringBuffer();
    b.append ("font-family:'");
    b.append (font.getFontName());
    b.append ("';font-size:");
    b.append (font.getFontSize());
    if (font.isBold())
    {
      b.append (";font-weight:bold");
    }
    if (font.isItalic())
    {
      b.append (";font-style:italic");
    }
    if (font.isUnderline() && font.isStrikeThrough())
    {
      b.append (";text-decoration:underline,line-through");
    }
    else
    if (font.isUnderline())
    {
      b.append (";text-decoration:underline");
    }
    else if (font.isStrikeThrough())
    {
      b.append (";text-decoration:line-through");
    }
    if (colorValue != null)
    {
      b.append (";color:");
      b.append (colorValue);
    }

    b.append (";vertical-align:");
    b.append (translateVerticalAlignment(style.getVerticalAlignment()));
    b.append (";text-align:");
    b.append (translateHorizontalAlignment(style.getVerticalAlignment()));
    b.append (";");
    return b.toString();
  };

  private String translateHorizontalAlignment (ElementAlignment ea)
  {
    if (ea == ElementAlignment.RIGHT)
      return "right";
    if (ea == ElementAlignment.CENTER)
      return "center";
    return "left";
  }

  private String translateVerticalAlignment (ElementAlignment ea)
  {
    if (ea == ElementAlignment.BOTTOM)
      return "bottom";
    if (ea == ElementAlignment.MIDDLE)
      return "middle";
    return "top";
  }

  private String getColorString (Color color)
  {
    try
    {
      colorObjectDescription.setParameterFromObject(color);
      return (String) colorObjectDescription.getParameter ("value");
    }
    catch (Exception ofe)
    {
      Log.debug ("Failed to refactor the color value");
    }
    return null;
  }


}
