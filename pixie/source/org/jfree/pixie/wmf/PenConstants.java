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
 * PenConstants.java
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
 * The PenConstants were defined in the Windows-API and are used do define
 * the appearance of Wmf-Pens.
 */
public interface PenConstants
{
  /* Pen Styles */
  public static final int PS_SOLID = 0;
  public static final int PS_DASH = 1;       /* -------  */
  public static final int PS_DOT = 2;       /* .......  */
  public static final int PS_DASHDOT = 3;       /* _._._._  */
  public static final int PS_DASHDOTDOT = 4;       /* _.._.._  */
  public static final int PS_NULL = 5;
  public static final int PS_INSIDEFRAME = 6;
  public static final int PS_USERSTYLE = 7;
  public static final int PS_ALTERNATE = 8;
  public static final int PS_STYLE_MASK = 0x0000000F;

  public static final int PS_ENDCAP_ROUND = 0x00000000;
  public static final int PS_ENDCAP_SQUARE = 0x00000100;
  public static final int PS_ENDCAP_FLAT = 0x00000200;
  public static final int PS_ENDCAP_MASK = 0x00000F00;

  public static final int PS_JOIN_ROUND = 0x00000000;
  public static final int PS_JOIN_BEVEL = 0x00001000;
  public static final int PS_JOIN_MITER = 0x00002000;
  public static final int PS_JOIN_MASK = 0x0000F000;

  public static final int PS_COSMETIC = 0x00000000;
  public static final int PS_GEOMETRIC = 0x00010000;
  public static final int PS_TYPE_MASK = 0x000F0000;

  /**
   * ___ ___ ___
   */
  public static final float[] DASH_DASH =
          {
            6f, 2f
          };

  /**
   * _ _ _ _ _ _
   */
  public static final float[] DASH_DOT =
          {
            2f, 2f
          };

  /**
   * ___ _ ___ _
   */
  public static final float[] DASH_DASHDOT =
          {
            6f, 2f, 2f, 2f
          };

  /**
   * ___ _ _ ___
   */
  public static final float[] DASH_DASHDOTDOT =
          {
            6f, 2f, 2f, 2f, 2f, 2f
          };

}