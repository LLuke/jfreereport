/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * HtmlLayoutCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlLayoutCreator.java,v 1.1 2004/03/16 18:03:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import org.jfree.report.modules.output.table.base.DefaultLayoutCreator;
import org.jfree.report.modules.output.table.base.SheetLayout;

/**
 * The HtmlLayoutCreator collects the StyleSheet information and
 * builds the table grid for the HTML output.
 * <p>
 * The StyleCollection is shared among all sheet-layouts to create
 * one single global stylesheet.
 */
public class HtmlLayoutCreator extends DefaultLayoutCreator
{
  /** The style collection to build the global stylesheet. */
  private HtmlStyleCollection styleCollection;

  /**
   * Creates a new layout creator for the html output.
   *
   * @param configurationPrefix the configuration prefix.
   */
  public HtmlLayoutCreator (final String configurationPrefix)
  {
    super(configurationPrefix);
    styleCollection = new HtmlStyleCollection();
  }

  /**
   * Creates a new sheet layout instance to collect the grid
   * bounds and the style information for a single table.
   *
   * @param strict defines, whether to use a stricter layouting
   * algorithm.
   * @return the sheetlayout instance to collect the cell bounds
   * of a single page.
   */
  protected SheetLayout createSheetLayout (final boolean strict)
  {
    return new HtmlSheetLayout(strict, getStyleCollection());
  }

  /**
   * Returns the assigned stylecollection.
   *
   * @return the style collection.
   */
  public HtmlStyleCollection getStyleCollection ()
  {
    return styleCollection;
  }
}
