/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: MonospaceFontRegistry.java,v 1.2 2007/05/14 09:00:06 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.fonts.monospace;

import java.util.HashMap;

import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetricsFactory;

/**
 * Creation-Date: 13.05.2007, 13:12:04
 *
 * @author Thomas Morgner
 */
public class MonospaceFontRegistry implements FontRegistry
{
  private HashMap fontFamilies;
  private float lpi;
  private float cpi;
  private MonospaceFontFamily fallback;

  public MonospaceFontRegistry(final float lpi, final float cpi)
  {
    this.lpi = lpi;
    this.cpi = cpi;
    this.fontFamilies = new HashMap();
    this.fallback = new MonospaceFontFamily("Monospace");
  }

  public void add (final MonospaceFontFamily family)
  {
    this.fontFamilies.put (family.getFamilyName(), family);
  }

  public MonospaceFontFamily getFallback()
  {
    return fallback;
  }

  public void setFallback(final MonospaceFontFamily fallback)
  {
    this.fallback = fallback;
  }

  public void initialize()
  {

  }

  /**
   * Tries to find a font family with the given name, looking through all alternative font names if neccessary.
   *
   * @param name
   * @return the font family or null, if there is no such family.
   */
  public FontFamily getFontFamily(final String name)
  {
    final FontFamily fontFamily = (FontFamily) fontFamilies.get(name);
    if (fontFamily != null)
    {
      return fontFamily;
    }
    return fallback;
  }

  public String[] getRegisteredFamilies()
  {
    return (String[]) fontFamilies.keySet().toArray(new String[fontFamilies.size()]);
  }

  public String[] getAllRegisteredFamilies()
  {
    return (String[]) fontFamilies.keySet().toArray(new String[fontFamilies.size()]);
  }

  /**
   * Creates a new font metrics factory. That factory is specific to a certain font registry and is not required to
   * handle font records from foreign font registries.
   * <p/>
   * A font metrics factory should never be used on its own. It should be embedded into and used by a FontStorage
   * implementation.
   *
   * @return
   */
  public FontMetricsFactory createMetricsFactory()
  {
    return new MonospaceFontMetricsFactory(lpi, cpi);
  }
}
