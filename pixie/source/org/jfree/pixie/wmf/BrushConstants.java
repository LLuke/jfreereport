/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * BrushConstants.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BrushConstants.java,v 1.2 2004/01/19 18:36:25 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

/**
 * The BrushConstants were defined in the Windows-API and are used do define the
 * appearance of Wmf-Brushes.
 */
public interface BrushConstants
{
  /* Brush Styles */
  public static final int BS_SOLID = 0;
  public static final int BS_NULL = 1;
  public static final int BS_HOLLOW = BS_NULL;
  public static final int BS_HATCHED = 2;
  public static final int BS_PATTERN = 3;
  public static final int BS_INDEXED = 4;
  public static final int BS_DIBPATTERN = 5;
  public static final int BS_DIBPATTERNPT = 6;
  public static final int BS_PATTERN8X8 = 7;
  public static final int BS_DIBPATTERN8X8 = 8;
  public static final int BS_MONOPATTERN = 9;

  /* Hatch Style: -----. */
  public static final int HS_HORIZONTAL = 0;
  /* Hatch Style: |||||. */
  public static final int HS_VERTICAL = 1;
  /* Hatch Style: \\\\\\\ . */
  public static final int HS_FDIAGONAL = 2;
  /* Hatch Style: //////// . */
  public static final int HS_BDIAGONAL = 3;
  /* Hatch Style: +++++++ . */
  public static final int HS_CROSS = 4;
  /* Hatch Style: XXXXXXX . */
  public static final int HS_DIAGCROSS = 5;

  public static final int TRANSPARENT = 1;
  public static final int OPAQUE = 2;
}