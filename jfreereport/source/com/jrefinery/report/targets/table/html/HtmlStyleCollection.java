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
 * -------------------
 * HtmlStyleCollection.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlStyleCollection.java,v 1.6 2003/02/20 00:39:37 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.io.ext.factory.objects.ColorObjectDescription;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class HtmlStyleCollection
{
  private ColorObjectDescription colorObjectDescription;
  private Hashtable table;
  private int nameCounter;


  public HtmlStyleCollection()
  {
    this.colorObjectDescription = new ColorObjectDescription();
    this.table = new Hashtable();
  }


  private String createName ()
  {
    String name = "style-" + nameCounter;
    nameCounter++;
    return name;

  }
  /**
   *
   * @param style
   */
  public String addStyle (HtmlCellStyle style)
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
  }

  private String translateFontName (FontDefinition font)
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

  public String createStyleSheetDefinition (HtmlCellStyle style)
  {
    FontDefinition font = style.getFont();
    String colorValue = getColorString(style.getFontColor());

    StringBuffer b = new StringBuffer();
    b.append ("font-family:");
    b.append (translateFontName(font));
    b.append ("; font-size:");
    b.append (font.getFontSize());
    b.append ("pt");
    if (font.isBold())
    {
      b.append ("; font-weight:bold");
    }
    if (font.isItalic())
    {
      b.append ("; font-style:italic");
    }
    if (font.isUnderline() && font.isStrikeThrough())
    {
      b.append ("; text-decoration:underline,line-through");
    }
    else
    if (font.isUnderline())
    {
      b.append ("; text-decoration:underline");
    }
    else if (font.isStrikeThrough())
    {
      b.append ("; text-decoration:line-through");
    }
    if (colorValue != null)
    {
      b.append ("; color:");
      b.append (colorValue);
    }

    b.append ("; vertical-align:");
    b.append (translateVerticalAlignment(style.getVerticalAlignment()));
    b.append ("; text-align:");
    b.append (translateHorizontalAlignment(style.getHorizontalAlignment()));
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
      //Log.debug ("Failed to compute the color value");
    }
    return null;
  }

  public String getBackgroundStyle (TableCellBackground bg)
  {
    ArrayList style = new ArrayList();
    Color c = bg.getColor();
    if (c != null)
    {
      StringBuffer b = new StringBuffer();
      b.append ("background-color:");
      b.append (getColorString(c));
      style.add (b.toString());
    }

    if (bg.getColorTop() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append ("border-top: ");
      b.append (bg.getBorderSizeTop());
      b.append ("pt solid ");
      b.append (getColorString(bg.getColorTop()));
      style.add (b.toString());
    }

    if (bg.getColorBottom() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append ("border-bottom: ");
      b.append (bg.getBorderSizeBottom());
      b.append ("pt solid ");
      b.append (getColorString(bg.getColorBottom()));
      style.add (b.toString());
    }

    if (bg.getColorLeft() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append ("border-left: ");
      b.append (bg.getBorderSizeLeft());
      b.append ("pt solid ");
      b.append (getColorString(bg.getColorLeft()));
      style.add (b.toString());
    }

    if (bg.getColorRight() != null)
    {
      StringBuffer b = new StringBuffer();
      b.append ("border-right: ");
      b.append (bg.getBorderSizeRight());
      b.append ("pt solid ");
      b.append (getColorString(bg.getColorRight()));
      style.add (b.toString());
    }

    StringBuffer b = new StringBuffer();
    Iterator styles = style.iterator();
    while (styles.hasNext())
    {
      b.append (styles.next());
      if (styles.hasNext())
      {
        b.append("; ");
      }
    }

    return b.toString();
  }
}
