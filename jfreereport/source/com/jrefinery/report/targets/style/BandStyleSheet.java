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
 * -------------------
 * BandStyleSheet.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandStyleSheet.java,v 1.3 2002/12/07 20:53:13 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.style;

/**
 * A band style sheet. Defines some base StyleKeys for all Bands.
 *
 * @author Thomas Morgner
 */
public class BandStyleSheet extends ElementStyleSheet
{
  /** A key for the band's 'page break before' flag. */
  public static final StyleKey PAGEBREAK_BEFORE = StyleKey.getStyleKey("pagebreak-before", 
                                                                       Boolean.class);

  /** A key for the band's 'page break after' flag. */
  public static final StyleKey PAGEBREAK_AFTER = StyleKey.getStyleKey("pagebreak-after", 
                                                                      Boolean.class);

  /** A key for the band's 'display on first page' flag. */
  public static final StyleKey DISPLAY_ON_FIRSTPAGE = StyleKey.getStyleKey("display-on-firstpage", 
                                                                           Boolean.class);

  /** A key for the band's 'display on last page' flag. */
  public static final StyleKey DISPLAY_ON_LASTPAGE = StyleKey.getStyleKey("display-on-lastpage", 
                                                                          Boolean.class);

  /** A key for the band's 'repeat header' flag. */
  public static final StyleKey REPEAT_HEADER = StyleKey.getStyleKey("repeat-header", 
                                                                    Boolean.class);

  /**
   * Creates a new band style-sheet.
   *
   * @param name  the name.
   */
  public BandStyleSheet(String name)
  {
    super(name);
  }
}
