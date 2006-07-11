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
 * GraphemeClassifier.java
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
package org.jfree.layouting.renderer.text;

/**
 * Creation-Date: 11.06.2006, 17:11:16
 *
 * @author Thomas Morgner
 */
public class GraphemeClassifier
{
  public static final int OTHER = 0;

  public static final int CR = 0x01;
  public static final int LF = 0x02;
  public static final int CONTROL = 0x03;

  public static final int EXTEND = 4;

  public static final int L   = 0x08; // 0000 0000 1000;
  public static final int LV  = 0x18; // 0000 0001 1000;
  public static final int V   = 0x38; // 0000 0011 1000;
  public static final int T   = 0x68; // 0000 0110 1000;
  public static final int LVT = 0x48; // 0000 0100 1000;

  public static final int ANY_HANGUL_MASK = 0x8;
  public static final int   V_OR_LV_MASK = 0x18;
  public static final int   V_OR_T_MASK = 0x28;
  public static final int LVT_OR_T_MASK = 0x48;

  public GraphemeClassifier()
  {
  }

  public int getGraphemeClassification(int codePoint)
  {
    if (codePoint == 0x0D)
    {
      return CR;
    }
    if (codePoint == 0x0A)
    {
      return LF;
    }
    return OTHER;
  }
}
