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
 * FontFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 */
package com.jrefinery.report.io;

import org.xml.sax.Attributes;

import java.awt.Font;

public class FontFactory implements ReportDefinitionTags
{
  public FontFactory ()
  {
    init ();
  }

  private String defaultFontName;
  private int defaultFontStyle;
  private int defaultFontSize;

  public void init ()
  {
    defaultFontName = "Serif";
    defaultFontStyle = Font.PLAIN;
    defaultFontSize = 12;
  }

  protected int readInt (Attributes attr, String name, int def)
  {
    String val = attr.getValue (name);
    if (val == null)
      return def;

    try
    {
      int s = Integer.parseInt (val);
      return s;
    }
    catch (NumberFormatException e)
    {
    }
    return def;
  }

  protected String readString (Attributes attr, String name, String def)
  {
    String val = attr.getValue (name);
    if (val == null)
      return def;
    return val;
  }

  protected int readFontStyle (Attributes attr, int def)
  {
    String fontStyle = attr.getValue (FONT_STYLE_ATT);
    int fs = def;
    if (fontStyle != null)
    {
      if (fontStyle.equals ("plain"))
        fs = Font.PLAIN;
      else if (fontStyle.equals ("bold"))
        fs = Font.BOLD;
      else if (fontStyle.equals ("italic"))
        fs = Font.ITALIC;
      else if (fontStyle.equals ("bold-italic"))
        fs = Font.BOLD + Font.ITALIC;
    }
    return fs;
  }

  public Font createFont (Attributes attr)
          throws ReportDefinitionException
  {
    // get the font name...
    String elementFontName = readString (attr, FONT_NAME_ATT, defaultFontName);

    // get the font style...
    int elementFontStyle = readFontStyle (attr, defaultFontStyle);

    // get the font size...
    int elementFontSize = readInt (attr, FONT_SIZE_ATT, defaultFontSize);

    if (elementFontName == null || elementFontStyle == -1 || elementFontSize == -1)
      throw new ReportDefinitionException ("Unable to create the font");

    // return the font...
    return new Font (elementFontName, elementFontStyle, elementFontSize);
  }

  public Font createDefaultFont (Attributes attr)
  {
    init ();
    // get the font name...
    defaultFontName = readString (attr, FONT_NAME_ATT, defaultFontName);

    // get the font style...
    defaultFontStyle = readFontStyle (attr, defaultFontStyle);

    // get the font size...
    defaultFontSize = readInt (attr, FONT_SIZE_ATT, defaultFontSize);


    if (defaultFontName == null || defaultFontStyle == -1 || defaultFontSize == -1)
      return null;

    // return the font...
    return new Font (defaultFontName, defaultFontStyle, defaultFontSize);
  }
}
