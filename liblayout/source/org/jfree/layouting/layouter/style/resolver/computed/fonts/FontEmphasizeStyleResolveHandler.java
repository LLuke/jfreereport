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
 * FontEmphasizeStyleResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontEmphasizeStyleResolveHandler.java,v 1.1 2006/02/12 21:49:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.input.style.keys.font.FontEmphasizeStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 18.12.2005, 16:32:08
 *
 * @author Thomas Morgner
 */
public class FontEmphasizeStyleResolveHandler  extends ConstantsResolveHandler
{
  public FontEmphasizeStyleResolveHandler()
  {
    addNormalizeValue(FontEmphasizeStyle.ACCENT);
    addNormalizeValue(FontEmphasizeStyle.CIRCLE);
    addNormalizeValue(FontEmphasizeStyle.DISC);
    addNormalizeValue(FontEmphasizeStyle.DOT);
    addNormalizeValue(FontEmphasizeStyle.NONE);
  }
}
