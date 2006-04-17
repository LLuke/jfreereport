/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * AWTFontRegistry.java
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
package org.jfree.fonts.awt;

import java.awt.GraphicsEnvironment;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontMetricsFactory;

/**
 * A very simple font registry wrapping around the AWT font classes.
 *
 * @author Thomas Morgner
 */
public class AWTFontRegistry implements FontRegistry
{

  public AWTFontRegistry()
  {
  }

  public void initialize()
  {
  }

  /**
   * Creates a new font metrics factory. That factory is specific to a certain
   * font registry and is not required to handle font records from foreign font
   * registries.
   * <p/>
   * A font metrics factory should never be used on its own. It should be
   * embedded into and used by a FontStorage implementation.
   *
   * @return
   */
  public FontMetricsFactory createMetricsFactory()
  {
    return new AWTFontMetricsFactory();
  }

  public FontFamily getFontFamily(String name)
  {
    return new AWTFontFamily(name);
  }

  public String[] getRegisteredFamilies()
  {
    GraphicsEnvironment genv =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
    return genv.getAvailableFontFamilyNames();
  }

  public String[] getAllRegisteredFamilies()
  {
    return getRegisteredFamilies();
  }
}
