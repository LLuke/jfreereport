/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 18.12.2005, 20:55:31
 *
 * @author Thomas Morgner
 */
public class FontStyleResolveHandler extends ConstantsResolveHandler
{
  public FontStyleResolveHandler()
  {
    addNormalizeValue(FontStyle.ITALIC);
    addNormalizeValue(FontStyle.NORMAL);
    addNormalizeValue(FontStyle.OBLIQUE);
    setFallback(FontStyle.NORMAL);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

}
