/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ---------------------
 * StylesComparator.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StylesComparator.java,v 1.2 2003/02/21 11:31:13 mungady Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.util.Comparator;

import com.jrefinery.report.targets.style.ElementStyleSheet;

/**
 * A styles comparator.
 * 
 * @author Thomas Morgner.
 */
public class StylesComparator implements Comparator
{
  /**
   * Compares its two arguments for order.  Returns a negative integer,
   * zero, or a positive integer as the first argument is less than, equal
   * to, or greater than the second.<p>
   *
   * The implementor must ensure that <tt>sgn(compare(x, y)) ==
   * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>compare(x, y)</tt> must throw an exception if and only
   * if <tt>compare(y, x)</tt> throws an exception.)<p>
   *
   * The implementor must also ensure that the relation is transitive:
   * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
   * <tt>compare(x, z)&gt;0</tt>.<p>
   *
   * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
   * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
   * <tt>z</tt>.<p>
   *
   * It is generally the case, but <i>not</i> strictly required that
   * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
   * any comparator that violates this condition should clearly indicate
   * this fact.  The recommended language is "Note: this comparator
   * imposes orderings that are inconsistent with equals."
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the second.
   * @throws ClassCastException if the arguments' types prevent them from
   *                            being compared by this Comparator.
   */
  public int compare(Object o1, Object o2)
  {
    ElementStyleSheet s1 = (ElementStyleSheet) o1;
    ElementStyleSheet s2 = (ElementStyleSheet) o2;

    if (s1.isSubStyleSheet(s2)) 
    {
      return -1;
    }
    if (s2.isSubStyleSheet(s1)) 
    {
      return 1;
    }
    return 0;
  }
}
