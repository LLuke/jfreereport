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
 * AlignmentAdjust.java
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
package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 24.11.2005, 16:50:16
 *
 * @author Thomas Morgner
 */
public class AlignmentAdjust extends CSSConstant
{
  public static final AlignmentAdjust BASELINE =
          new AlignmentAdjust("baseline");
  public static final AlignmentAdjust AFTER_EDGE =
          new AlignmentAdjust("after-edge");
  public static final AlignmentAdjust BEFORE_EDGE =
          new AlignmentAdjust("before-edge");
  public static final AlignmentAdjust TEXT_AFTER_EDGE =
          new AlignmentAdjust("text-after-edge");
  public static final AlignmentAdjust TEXT_BEFORE_EDGE =
          new AlignmentAdjust("text-before-edge");

  public static final AlignmentAdjust ALPHABETIC =
          new AlignmentAdjust("alphabetic");
  public static final AlignmentAdjust HANGING =
          new AlignmentAdjust("hanging");
  public static final AlignmentAdjust IDEOGRAPHIC =
          new AlignmentAdjust("ideographic");
  public static final AlignmentAdjust MATHEMATICAL =
          new AlignmentAdjust("alphabetic");
  public static final AlignmentAdjust CENTRAL =
          new AlignmentAdjust("central");
  public static final AlignmentAdjust MIDDLE =
          new AlignmentAdjust("middle");

  private AlignmentAdjust(String name)
  {
    super(name);
  }
}
