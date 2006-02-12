/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * OutputProcessorMetaData.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: OutputProcessorMetaData.java,v 1.1 2006/02/12 21:40:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
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
}
