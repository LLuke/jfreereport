/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultFontMapper.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.util.HashMap;

import org.jfree.report.style.FontDefinition;

public class DefaultFontMapper implements FontMapper
{
  private HashMap fontMapping;
  private byte defaultFont;

  public DefaultFontMapper ()
  {
    fontMapping = new HashMap();
    defaultFont = PrinterDriver.SELECT_FONT_ROMAN;
  }

  public void addFontMapping (final String fontName, final byte printerCode)
  {
    if (fontName == null)
    {
      throw new NullPointerException();
    }
    fontMapping.put(fontName, new Byte(printerCode));
  }

  public void removeFontMapping (final String fontName)
  {
    fontMapping.remove(fontName);
  }

  public byte getPrinterFont (final FontDefinition fontDefinition)
  {
    final Byte b = (Byte) fontMapping.get(fontDefinition.getFontName());
    if (b != null)
    {
      return b.byteValue();
    }
    return handleDefault(fontDefinition);
  }

  protected byte handleDefault (final FontDefinition fd)
  {
    if (fd.isCourier())
    {
      return PrinterDriver.SELECT_FONT_COURIER;
    }
    if (fd.isSerif())
    {
      return PrinterDriver.SELECT_FONT_ROMAN;
    }
    if (fd.isSansSerif())
    {
      return PrinterDriver.SELECT_FONT_OCR_A;
    }
    return defaultFont;
  }

  public byte getDefaultFont ()
  {
    return defaultFont;
  }

  public void setDefaultFont (final byte defaultFont)
  {
    this.defaultFont = defaultFont;
  }
}
