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
 * TextDecorationReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TextDecorationStyleReadHandler.java,v 1.1 2006/02/12 21:57:22 taqua Exp $
 *
 * Changes
 * -------------------------
 * 02.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.keys.text.TextDecorationStyle;

/**
 * Creation-Date: 02.12.2005, 20:09:00
 *
 * @author Thomas Morgner
 */
public class TextDecorationStyleReadHandler extends OneOfConstantsReadHandler
{
  public TextDecorationStyleReadHandler()
  {
    super(false);
    addValue(TextDecorationStyle.DASHED);
    addValue(TextDecorationStyle.DOT_DASH);
    addValue(TextDecorationStyle.DOT_DOT_DASH);
    addValue(TextDecorationStyle.DOTTED);
    addValue(TextDecorationStyle.DOUBLE);
    addValue(TextDecorationStyle.NONE);
    addValue(TextDecorationStyle.SOLID);
    addValue(TextDecorationStyle.WAVE);
  }
}
