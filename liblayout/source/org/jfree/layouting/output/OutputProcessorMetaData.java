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
 * OutputProcessorMetaData.java
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
package org.jfree.layouting.output;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;

/**
 * Creation-Date: 14.12.2005, 13:47:00
 *
 * @author Thomas Morgner
 */
public interface OutputProcessorMetaData
{
  public boolean isFeatureSupported (OutputProcessorFeature.BooleanOutputProcessorFeature feature);
  public double getNumericFeatureValue (OutputProcessorFeature.NumericOutputProcessorFeature feature);

  public FontFamily getDefaultFontFamily();
  public FontFamily getFontFamily(FontFamilyValues genericName);
  public double getFontSize (FontSizeConstant constant);

  /**
   * Returns the media type of the output target. This corresponds directly to
   * the CSS defined media types and is used as a selector.
   *
   * @return the media type of the output target.
   */
  public String getMediaType();
  public String getExportDescriptor();
}
