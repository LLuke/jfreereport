/**
 * Date: Jan 25, 2003
 * Time: 6:17:21 AM
 *
 * $Id: HtmlStyleCollection.java,v 1.1 2003/01/25 20:38:34 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.io.ext.factory.objects.ColorObjectDescription;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

public class HtmlStyleCollection
{
  private ColorObjectDescription colorObjectDescription;
  private Hashtable table;
  private boolean compressed;
  private int nameCounter;

  private boolean useXHTML = false;

  public HtmlStyleCollection()
  {
    this.colorObjectDescription = new ColorObjectDescription();
    this.table = new Hashtable();
  }

  public boolean isCompressed()
  {
    return compressed;
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
      Log.debug ("There is no such style ");
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
      Log.debug ("Failed to refactor the color value");
    }
    return null;
  }

  public void compactNames ()
  {

  }
}
