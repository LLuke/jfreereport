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
 * ------------------------------
 * VerifyCellDataFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerifyCellDataFactory.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

import java.awt.geom.Rectangle2D;

import org.jfree.report.modules.output.table.base.TableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableCellData;
import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.Band;

/**
 * A cell data factory which creates test content, which can be verified
 * against a predefined layout.
 * 
 * @author Thomas Morgner
 */
public class VerifyCellDataFactory implements TableCellDataFactory
{
  
  /**
   * Creates a new verify cell data factory.
   *
   */
  public VerifyCellDataFactory()
  {
  }

  /**
   * Creates a {@link TableCellData} for an {@link Element}. The generated CellData
   * should contain copies of all needed element attributes, as the element instance
   * will be reused in the later report processing.
   * <p>
   * If the tablemodel does not support the element type, return <code>null</code>.
   *
   * @param e  the element that should be converted into TableCellData.
   * @param rect  the element's bounds within the table (specified in points).
   *
   * @return The generated TableCellData object, or <code>null</code> if element type is
   *         not supported.
   */
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    boolean isBackground = (e.getContentType().equals(ShapeElement.CONTENT_TYPE));
    if (e instanceof Band)
    {
      return new VerifyBandArea(rect, e.getName());
    }
    if (isBackground)
    {
      return new VerifyCellBackground(rect, e.getName());
    }
    return new VerifyCellContent(rect, e.getName());
  }
}
