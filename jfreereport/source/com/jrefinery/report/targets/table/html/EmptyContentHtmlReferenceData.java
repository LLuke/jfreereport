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
 * ----------------------------------
 * EmptyContentHtmlReferenceData.java
 * ----------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EmptyContentHtmlReferenceData.java,v 1.4 2003/02/26 16:42:26 mungady Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

/**
 * Stores a marker, that the referenced cell is empty. HTML requires empty cells to
 * be filled with an invisible character, or the cell is ignored. Ignored cells tend
 * to destroy the table layout, so we make sure that every cell has content inside.
 * <p>
 * This reference is used to mark empty external image references, when no external
 * entity was referenced and no external content creation is allowed.
 *
 * @author Thomas Morgner
 */
public class EmptyContentHtmlReferenceData extends HtmlReferenceData
{
  /**
   * Creates a new EmptyContentHtmlReferenceData.
   */
  public EmptyContentHtmlReferenceData()
  {
    super(false);
  }

  /**
   * Gets the reference data that should be filled into the cell.
   *
   * @return the empty content, '&amp;nbsp;'.
   */
  public String getReference()
  {
    return "&nbsp;";
  }
}
