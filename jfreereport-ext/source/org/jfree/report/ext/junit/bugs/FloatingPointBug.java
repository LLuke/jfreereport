/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------
 * FloatingPointBug.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FloatingPointBug.java,v 1.1 2003/09/12 17:51:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.09.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.bugs;

public class FloatingPointBug
{
  private static float getValue(int mant, int exp)
  {
    double retval = (mant * StrictMath.pow(10, exp));
    System.out.println(retval);
    return (float) retval;
  }

  public static void main(String[] args)
  {
    double d = 8e+307;
    System.out.println(4.0 * d * 0.5);
    System.out.println(2.0 * d);
  }
}
