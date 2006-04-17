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
 * TextDecorationStyleReadHandler.java
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
