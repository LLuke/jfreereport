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
 * VerticalAlignResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: VerticalAlignResolveHandler.java,v 1.3 2006/07/11 13:29:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.computed.line;

import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class VerticalAlignResolveHandler extends ConstantsResolveHandler
{
  public VerticalAlignResolveHandler ()
  {
    addNormalizeValue(VerticalAlign.BASELINE);
    addNormalizeValue(VerticalAlign.BOTTOM);
    addNormalizeValue(VerticalAlign.CENTRAL);
    addNormalizeValue(VerticalAlign.MIDDLE);
    addNormalizeValue(VerticalAlign.SUB);
    addNormalizeValue(VerticalAlign.SUPER);
    addNormalizeValue(VerticalAlign.TEXT_BOTTOM);
    addNormalizeValue(VerticalAlign.TEXT_TOP);
    addNormalizeValue(VerticalAlign.TOP);
    // we do not detect scripts right now ...
    setFallback(VerticalAlign.BASELINE);
  }
  
}
