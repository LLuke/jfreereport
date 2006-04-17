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
 * FontStyleKeys.java
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

package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-fonts/
 *
 * @author Thomas Morgner
 */
public class FontStyleKeys
{
  /**
   * Font-Effects are not used yet. It should be implemented later, if possible.
   */
  public static final StyleKey FONT_EFFECT =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-effect", false, true, false);

  /**
   * Font-Emphasize is not used yet. It is needed for proper Asian font
   * support.
   */
  public static final StyleKey FONT_EMPHASIZE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-emphasize-style", false, true, false);

  /**
   * Font-Emphasize is not used yet. It is needed for proper Asian font
   * support.
   */
  public static final StyleKey FONT_EMPHASIZE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-emphasize-position", false, true, false);
  /**
   * The font-family holds the fully resolved name of an valid font.
   * The font-family value may be null, if the specified font resolved to 'none'.
   */
  public static final StyleKey FONT_FAMILY =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-family", false, true, false);

  /**
   * The font-size holds the size of the font in points.
   */
  public static final StyleKey FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-size", false, true, false);

  /**
   * The font-size-adjust is not used for now.
   */
  public static final StyleKey FONT_SIZE_ADJUST =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-size-adjust", false, true, false);

  /**
   * The font-smooth controls the anti-aliasing for the rendering process.
   * This may affect the rendered font size. Resolving the font-smooth
   * property must be done elsewhere (translating auto, never, always or the
   * size specifications into a boolean).
   */
  public static final StyleKey FONT_SMOOTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-smooth", false, true, false);

  public static final StyleKey FONT_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-style", false, true, false);

  public static final StyleKey FONT_VARIANT =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-variant", false, true, false);

  public static final StyleKey FONT_WEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-weight", false, true, false);

  public static final StyleKey FONT_STRETCH =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-stretch", false, true, false);
  /**
   *  Used in conjunction with text-align-last: size;
   */
  public static final StyleKey MIN_FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("min-font-size", false, true, false);
  /**
   *  Used in conjunction with text-align-last: size;
   */
  public static final StyleKey MAX_FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("max-font-size", false, true, false);

  private FontStyleKeys () {}


}
