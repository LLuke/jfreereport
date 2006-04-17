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
 * ArabicIndicCounterStyle.java
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

package org.jfree.layouting.layouter.counters.numeric;

public class ArabicIndicCounterStyle extends NumericCounterStyle
{
  public ArabicIndicCounterStyle ()
  {
    super(10, ".");
    setReplacementChar('0', '\u0660');
    setReplacementChar('1', '\u0661');
    setReplacementChar('2', '\u0662');
    setReplacementChar('3', '\u0663');
    setReplacementChar('4', '\u0664');
    setReplacementChar('5', '\u0665');
    setReplacementChar('6', '\u0666');
    setReplacementChar('7', '\u0667');
    setReplacementChar('8', '\u0668');
    setReplacementChar('9', '\u0669');
  }


}
