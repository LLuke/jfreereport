/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * MappingConstants.java
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
package org.jfree.pixie.wmf;

/**
 * Various MappingConstants defined in the Windows API.
 */
public interface MappingConstants
{
  public static final int MM_TEXT = 1;
  public static final int MM_LOMETRIC = 2;
  public static final int MM_HIMETRIC = 3;
  public static final int MM_LOENGLISH = 4;
  public static final int MM_HIENGLISH = 5;
  public static final int MM_TWIPS = 6;
  public static final int MM_ISOTROPIC = 7;
  public static final int MM_ANISOTROPIC = 8;

  /* Min and Max Mapping Mode values */
  public static final int MM_MIN = MM_TEXT;
  public static final int MM_MAX = MM_ANISOTROPIC;
  public static final int MM_MAX_FIXEDSCALE = MM_TWIPS;
}
