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
 * ----------------
 * FontFactory.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: FontFactory.java,v 1.9 2002/12/11 00:51:19 mungady Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 20-May-2002 : The default font is no longer hardcoded into the factory. It uses the Bands
 *               default font as fallback.
 */
package com.jrefinery.report.io.simple;

import com.jrefinery.report.io.ParserUtil;
import com.jrefinery.report.io.ReportDefinitionException;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.xml.sax.Attributes;

/**
 * Parses the font specifications for bands and text elements.
 *
 * @author Thomas Morgner
 */
public class FontFactory implements ReportDefinitionTags
{
  public static class FontInformation
  {
    private String fontname;
    private Integer fontSize;
    private Boolean isBold;
    private Boolean isItalic;

    public FontInformation()
    {
    }

    public String getFontname()
    {
      return fontname;
    }

    public void setFontname(String fontname)
    {
      this.fontname = fontname;
    }

    public Integer getFontSize()
    {
      return fontSize;
    }

    public void setFontSize(Integer fontSize)
    {
      this.fontSize = fontSize;
    }

    public Boolean getBold()
    {
      return isBold;
    }

    public void setBold(Boolean bold)
    {
      isBold = bold;
    }

    public Boolean getItalic()
    {
      return isItalic;
    }

    public void setItalic(Boolean italic)
    {
      isItalic = italic;
    }
  }

  /**
   * Default constructor.
   */
  public FontFactory ()
  {
  }

  public static void applyFontInformation (ElementStyleSheet es, FontInformation fi)
  {
    if (fi.getFontname() != null)
      es.setStyleProperty(ElementStyleSheet.FONT, fi.getFontname());
    if (fi.getFontSize() != null)
      es.setStyleProperty(ElementStyleSheet.FONTSIZE, fi.getFontSize());
    if (fi.getItalic() != null)
      es.setStyleProperty(ElementStyleSheet.ITALIC, fi.getItalic());
    if (fi.getBold() != null)
      es.setStyleProperty(ElementStyleSheet.BOLD, fi.getBold());
  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails.
   *
   * @param attr  the element attributes.
   * @param name  the attribute name.
   *
   * @return the int value.
   */
  protected Integer readInt (Attributes attr, String name)
  {
    String val = attr.getValue (name);
    if (val == null)
    {
      return null;
    }
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException e)
    {
      // swallow the exception, the default value will be returned
    }
    return null;
  }

  /**
   * Reads the fontstyle for an attribute set. The font style is appended to the given
   * font style definition.
   * <p>
   * This implementation does not support underline or strikethrough attributes of the DTD.
   *
   * @param attr  the element attributes.
   */
  private void readSimpleFontStyle (Attributes attr, FontInformation target)
  {
    String fontStyle = attr.getValue (FONT_STYLE_ATT);

    if (fontStyle != null)
    {
      if (fontStyle.equals ("bold"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.FALSE);
      }
      else if (fontStyle.equals ("italic"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals ("bold-italic"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.TRUE);
      }
    }

    if (attr.getValue(FS_BOLD) != null)
    {
      target.setBold(new Boolean(ParserUtil.parseBoolean (attr.getValue (FS_BOLD), false)));
    }

    if (attr.getValue(FS_ITALIC) != null)
    {
      target.setItalic(new Boolean(ParserUtil.parseBoolean (attr.getValue (FS_ITALIC), false)));
    }
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's attributes.
   *
   * @param attr  the element attributes.
   *
   * @throws com.jrefinery.report.io.ReportDefinitionException if the font cannot be created.
   */
  public void createFont (Attributes attr, ElementStyleSheet target)
          throws ReportDefinitionException
  {
    // get the font name...
    String elementFontName = attr.getValue (FONT_NAME_ATT);
    if (elementFontName != null)
    {
      target.setStyleProperty(ElementStyleSheet.FONT, elementFontName);
    }

    FontInformation fi = new FontInformation();
    // get the font style...
    readSimpleFontStyle (attr, fi);

    if (fi.getBold() != null)
    {
      target.setStyleProperty(ElementStyleSheet.BOLD, fi.getBold());
    }
    if (fi.getItalic() != null)
    {
      target.setStyleProperty(ElementStyleSheet.ITALIC, fi.getItalic());
    }

    // get the font size...
    Integer elementFontSize = readInt (attr, FONT_SIZE_ATT);
    if (elementFontSize != null)
    {
      target.setStyleProperty(ElementStyleSheet.FONTSIZE, elementFontSize);
    }
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's attributes.
   *
   * @param attr  the element attributes.
   *
   * @throws com.jrefinery.report.io.ReportDefinitionException if the font cannot be created.
   */
  public FontInformation createFont (Attributes attr)
          throws ReportDefinitionException
  {
    // get the font name...
    FontInformation fi = new FontInformation();

    String elementFontName = attr.getValue (FONT_NAME_ATT);
    if (elementFontName != null)
    {
      fi.setFontname(elementFontName);
    }

    // get the font style...
    readSimpleFontStyle (attr, fi);

    // get the font size...
    Integer elementFontSize = readInt (attr, FONT_SIZE_ATT);
    if (elementFontSize != null)
    {
      fi.setFontSize(elementFontSize);
    }
    return fi;
  }


}
