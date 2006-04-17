/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * DummyOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.output.pageable.dummy;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;
import org.jfree.layouting.output.OutputProcessorFeature;

public class DummyOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  private FontStorage fontStorage;

  public DummyOutputProcessorMetaData (FontStorage fontStorage)
  {
    super(fontStorage.getFontRegistry());
    this.fontStorage = fontStorage;

    setFamilyMapping(FontFamilyValues.CURSIVE, "sans-serif");
    setFamilyMapping(FontFamilyValues.FANTASY, "Verdana");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "monospaced");
    setFamilyMapping(FontFamilyValues.SERIF, "serif");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "sans-serif");

    setNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION, 300);
    setNumericFeatureValue(OutputProcessorFeature.FONT_SMOOTH_THRESHOLD, 8);

  }

  public FontFamily getDefaultFontFamily ()
  {
    final FontRegistry fontRegistry = fontStorage.getFontRegistry();
    final FontFamily ff = fontRegistry.getFontFamily("sans-serif");
    if (ff != null)
    {
      return ff;
    }
    final String[] families = fontRegistry.getRegisteredFamilies();
    if (families.length == 0)
    {
      return null;
    }
    return fontRegistry.getFontFamily(families[0]);
  }

  public String getExportDescriptor()
  {
    return "pageable/dummy";
  }
}
