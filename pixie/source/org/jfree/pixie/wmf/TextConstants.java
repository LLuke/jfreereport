/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * TextConstants.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: MfLogBrush.java,v 1.1 2003/02/25 20:58:07 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

/**
 * The TextConstants were defined in the Windows-API and are used do define
 * the appearance of Wmf-Text.
 */
public interface TextConstants
{
  public static final int TA_NOUPDATECP                = 0x0000; //bin 00.00000000
  public static final int TA_UPDATECP                  = 0x0001; //bin 00.00000001

  public static final int TA_LEFT                      = 0x0000; //bin 00.00000000
  public static final int TA_RIGHT                     = 0x0002; //bin 00.00000010
  public static final int TA_CENTER                    = 0x0006; //bin 00.00000110

  public static final int TA_TOP                       = 0x0000; //bin 00.00000000
  public static final int TA_BOTTOM                    = 0x0008; //bin 00.00001000
  public static final int TA_BASELINE                  = 0x0018; //bin 00.00011000
  public static final int TA_RTLREADING                = 0x0100; //bin 01.00000000
  public static final int TA_MASK       = (TA_BASELINE+TA_CENTER+TA_UPDATECP+TA_RTLREADING);

  public static final int VTA_BASELINE = TA_BASELINE;
  public static final int VTA_LEFT     = TA_BOTTOM;
  public static final int VTA_RIGHT    = TA_TOP;
  public static final int VTA_CENTER   = TA_CENTER;
  public static final int VTA_BOTTOM   = TA_RIGHT;
  public static final int VTA_TOP      = TA_LEFT;

}