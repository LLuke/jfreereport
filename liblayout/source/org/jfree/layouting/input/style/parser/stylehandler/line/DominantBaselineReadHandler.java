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
 * DominantBaselineReadHandler.java
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
package org.jfree.layouting.input.style.parser.stylehandler.line;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.keys.line.DominantBaseline;

/**
 * Creation-Date: 28.11.2005, 18:12:27
 *
 * @author Thomas Morgner
 */
public class DominantBaselineReadHandler extends OneOfConstantsReadHandler
{
  public DominantBaselineReadHandler()
  {
    super(true);
    addValue(DominantBaseline.ALPHABETIC);
    addValue(DominantBaseline.CENTRAL);
    addValue(DominantBaseline.HANGING);
    addValue(DominantBaseline.IDEOGRAPHIC);
    addValue(DominantBaseline.MATHEMATICAL);
    addValue(DominantBaseline.MIDDLE);
    addValue(DominantBaseline.NO_CHANGE);
    addValue(DominantBaseline.RESET_SIZE);
    addValue(DominantBaseline.TEXT_AFTER_EDGE);
    addValue(DominantBaseline.TEXT_BEFORE_EDGE);
    addValue(DominantBaseline.USE_SCRIPT);
  }
}
