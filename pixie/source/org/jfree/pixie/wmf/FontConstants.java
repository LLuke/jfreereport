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
 * FontConstants.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  David R. Harris
 * Contributor(s):   Thomas Morgner
 *
 * $Id: FontConstants.java,v 1.1 2003/03/09 20:38:16 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.pixie.wmf;

/**
 * Various FontConstants defined in the Windows API.
 */
public interface FontConstants
{
  public static final int DEFAULT_QUALITY = 0;
  public static final int DRAFT_QUALITY = 1;
  public static final int PROOF_QUALITY = 2;
  public static final int NONANTIALIASED_QUALITY = 3;
  public static final int ANTIALIASED_QUALITY = 4;

  public static final int DEFAULT_PITCH = 0;
  public static final int FIXED_PITCH = 1;
  public static final int VARIABLE_PITCH = 2;
  public static final int MONO_FONT = 8;

  /* Font Families */
  /* Don't care or don't know. */
  public static final int FF_DONTCARE = (0 << 4);

  /**
   * Variable stroke width, serifed.
   * Times Roman, Century Schoolbook, etc.
   */
  public static final int FF_ROMAN = (1 << 4);

  /*
   * Variable stroke width, sans-serifed.
   * Helvetica, Swiss, etc.
   */
  public static final int FF_SWISS = (2 << 4);

  /*
   * Constant stroke width, serifed or sans-serifed.
   * Pica, Elite, Courier, etc.
   */
  public static final int FF_MODERN = (3 << 4);

  /* Cursive, etc. */
  public static final int FF_SCRIPT = (4 << 4);

  /* Old English, etc. */
  public static final int FF_DECORATIVE = (5 << 4);

  /* Font Weights */
  public static final int FW_DONTCARE = 0;
  public static final int FW_THIN = 100;
  public static final int FW_EXTRALIGHT = 200;
  public static final int FW_LIGHT = 300;
  public static final int FW_NORMAL = 400;
  public static final int FW_MEDIUM = 500;
  public static final int FW_SEMIBOLD = 600;
  public static final int FW_BOLD = 700;
  public static final int FW_EXTRABOLD = 800;
  public static final int FW_HEAVY = 900;

  public static final int FW_ULTRALIGHT = FW_EXTRALIGHT;
  public static final int FW_REGULAR = FW_NORMAL;
  public static final int FW_DEMIBOLD = FW_SEMIBOLD;
  public static final int FW_ULTRABOLD = FW_EXTRABOLD;
  public static final int FW_BLACK = FW_HEAVY;


}