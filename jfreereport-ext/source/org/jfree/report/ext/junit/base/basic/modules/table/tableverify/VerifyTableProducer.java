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
 * VerifyTableProducer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerifyTableProducer.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.10.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

import java.util.List;
import java.awt.geom.Rectangle2D;

import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableGridLayout;
import org.jfree.report.modules.output.table.base.TableGridPosition;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;
import org.jfree.report.modules.output.table.base.TableProducer;

public class VerifyTableProducer extends TableProducer
{
  private boolean isOpen;
  private VerifyCellDataFactory factory;
  private VerifyPattern pattern;

  /**
   * Creates a new TableProducer. This constructor must be used to complete the content
   * creation after the pagination was done.
   *
   * @param gridBoundsCollection the layout information implementation used to create
   * the table layout.
   */
  public VerifyTableProducer(final TableLayoutInfo gridBoundsCollection,
                             final VerifyPattern pattern)
  {
    super(gridBoundsCollection);
    factory = new VerifyCellDataFactory();
    if (pattern == null)
    {
      throw new NullPointerException("Pattern is null.");
    }
    this.pattern = pattern;

  }

  /**
   * Creates a new TableProducer. This constructor should be used to create an
   * producer for the repagination process.
   *
   * @param gridBoundsCollection the layout information implementation used to create
   * the table layout.
   * @param strictLayout the strict layout flag. Set to true, to enable the strict
   * layout mode.
   */
  public VerifyTableProducer(final TableLayoutInfo gridBoundsCollection,
                             final boolean strictLayout)
  {
    super(gridBoundsCollection, strictLayout);
    factory = new VerifyCellDataFactory();
  }

  /**
   * Closes the report and finishs the report writing. Any used resource should
   * be freed when this method returns. The current page is already closed.
   * This method is called only once for a given instance.
   */
  public void close()
  {
    isOpen = false;
  }

  /**
   * Write the collected data. This method is called when ever it is safe to
   * commit all previous content. An auto-commit is also performed after the page
   * has ended.
   * <p>
   * Implementations have to take care, that empty commits do not produce any
   * output. Successfully written content must be removed.
   */
  public void commit()
  {
    Rectangle2D cellBounds = new Rectangle2D.Float();
    if (isDummy() == false)
    {
      TableGridLayout layout = layoutGrid();
      for (int y = 0; y < layout.getHeight(); y++)
      {
        pattern.addRow();
        for (int x = 0; x < layout.getWidth(); x++)
        {
          if (layout.containsData(x, y) == false)
          {
            continue;
          }

          cellBounds = createCellBounds(layout, x, y, cellBounds);
          TableGridLayout.Element gridPosition = layout.getData(x, y);
          final TableGridPosition root = gridPosition.getRoot();
          final TableCellBackground bg = createTableCellStyle
              (gridPosition.getBackground(), cellBounds);
          if (root == null)
          {
            // just apply the background, if any ...
            if (bg != null)
            {
              pattern.addCell(new VerifyCell(x, y, 1, 1));
            }
            continue;
          }

          if (root.isOrigin(x, y))
          {
            pattern.addCell(new VerifyCell(x, y, root.getColSpan(), root.getRowSpan()));
          }
        }
      }
      clearCells();
    }
  }

  public static TableCellBackground createStaticTableCellStyle
      (List background, Rectangle2D cellBounds)
  {
    return TableProducer.createStaticTableCellStyle(background, cellBounds);
  }

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public TableCellDataFactory getCellDataFactory()
  {
    return factory;
  }

  /**
   * Returns true, if the TableProducer is open. Only open producers
   * are able to write TableCells or to create TableCellData from Elements.
   *
   * @return checks, whether the TableProducer is open.
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  /**
   * Starts the report writing. This method is called before any other report handling
   * method is called. This method is called only once for a given instance.
   */
  public void open()
  {
    isOpen = true;
  }
}
