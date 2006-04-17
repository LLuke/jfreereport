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
 * TextConstants.java
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
 * The TextConstants were defined in the Windows-API and are used do define the appearance
 * of Wmf-Text.
 */
public interface TextConstants
{
  public static final int TA_NOUPDATECP = 0x0000; //bin 00.00000000
  public static final int TA_UPDATECP = 0x0001; //bin 00.00000001

  public static final int TA_LEFT = 0x0000; //bin 00.00000000
  public static final int TA_RIGHT = 0x0002; //bin 00.00000010
  public static final int TA_CENTER = 0x0006; //bin 00.00000110

  public static final int TA_TOP = 0x0000; //bin 00.00000000
  public static final int TA_BOTTOM = 0x0008; //bin 00.00001000
  public static final int TA_BASELINE = 0x0018; //bin 00.00011000
  public static final int TA_RTLREADING = 0x0100; //bin 01.00000000
  public static final int TA_MASK = (TA_BASELINE + TA_CENTER + TA_UPDATECP + TA_RTLREADING);

  public static final int VTA_BASELINE = TA_BASELINE;
  public static final int VTA_LEFT = TA_BOTTOM;
  public static final int VTA_RIGHT = TA_TOP;
  public static final int VTA_CENTER = TA_CENTER;
  public static final int VTA_BOTTOM = TA_RIGHT;
  public static final int VTA_TOP = TA_LEFT;

}
