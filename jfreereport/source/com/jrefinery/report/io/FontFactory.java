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
 * 20-May-2002 : The default font is no longer hardcoded into the factory. It uses the Bands
 *               default font as fallback.
 */
package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import org.xml.sax.Attributes;

import java.awt.Font;

/**
 * Parses the font specifications for bands and text elements.
 */
public class FontFactory implements ReportDefinitionTags
{
  private String defaultFontName;
  private int defaultFontStyle;
  private int defaultFontSize;

  /**
   * Default Constructor
   */
  public FontFactory ()
  {
    init ();
  }

  /**
   * Initializes the factory to the default values.
   */
  public void init ()
  {
    Font defaultFont = Band.DEFAULT_FONT;
    defaultFontName = defaultFont.getName ();
    defaultFontStyle = defaultFont.getStyle ();
    defaultFontSize = defaultFont.getSize ();
  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails
   */
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

  /**
   * Reads the fontstyle for an attribute set. The font style is appended to the given
   * font style definition.
   * <p>
   * This implementation does not support underline or strikethrough
   * attributes of the DTD.
   */
  protected int readSimpleFontStyle (Attributes attr, int def)
  {
    String fontStyle = attr.getValue (FONT_STYLE_ATT);
    boolean isBold = false;
    boolean isItalic = false;
    boolean isUnderline = false;
    boolean isStriked = false;

    int fs = def;
    if (fontStyle != null)
    {
      if (fontStyle.equals ("bold"))
      {
        isBold = true;
      }
      else if (fontStyle.equals ("italic"))
      {
        isItalic = true;
      }
      else if (fontStyle.equals ("bold-italic"))
      {
        isBold = true;
        isItalic = true;
      }
    }

    isBold = ParserUtil.parseBoolean (attr.getValue (FS_BOLD), isBold);
    isItalic = ParserUtil.parseBoolean (attr.getValue (FS_ITALIC), isItalic);
//    isUnderline = ParserUtil.parseBoolean (attr.getValue (FS_UNDERLINE), isUnderline);
//    isStriked = ParserUtil.parseBoolean (attr.getValue (FS_STRIKETHR), isStriked);
    if (isBold) fs += Font.BOLD;
    if (isItalic) fs += Font.ITALIC;
    return fs;
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default fonts attributes.
   */
  public Font createFont (Attributes attr)
          throws ReportDefinitionException
  {
    // get the font name...
    String elementFontName = ParserUtil.parseString (attr.getValue (FONT_NAME_ATT), defaultFontName);

    // get the font style...
    int elementFontStyle = readSimpleFontStyle (attr, defaultFontStyle);

    // get the font size...
    int elementFontSize = readInt (attr, FONT_SIZE_ATT, defaultFontSize);

    if (elementFontName == null || elementFontStyle == -1 || elementFontSize == -1)
      throw new ReportDefinitionException ("Unable to create the font");

    // return the font...
    return new Font (elementFontName, elementFontStyle, elementFontSize);
  }

  /**
   * Parses an band font.
   * Missing attributes are replaces with the default fonts attributes. The result of this parsing
   * will set the default values for element fonts
   */
  public Font createDefaultFont (Attributes attr)
  {
    init ();
    // get the font name...
    defaultFontName = ParserUtil.parseString (attr.getValue (FONT_NAME_ATT), defaultFontName);

    // get the font style...
    defaultFontStyle = readSimpleFontStyle (attr, defaultFontStyle);

    // get the font size...
    defaultFontSize = readInt (attr, FONT_SIZE_ATT, defaultFontSize);


    if (defaultFontName == null || defaultFontStyle == -1 || defaultFontSize == -1)
      return null;

    // return the font...
    return new Font (defaultFontName, defaultFontStyle, defaultFontSize);
  }
}
