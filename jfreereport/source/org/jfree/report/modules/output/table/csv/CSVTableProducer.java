/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ---------------------
 * CSVTableProducer.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVTableProducer.java,v 1.6 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.table.csv;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Properties;

import org.jfree.report.modules.output.csv.CSVQuoter;
import org.jfree.report.modules.output.table.base.TableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableGridLayout;
import org.jfree.report.modules.output.table.base.TableGridPosition;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;
import org.jfree.report.modules.output.table.base.TableProducer;

/**
 * The TableProducer is responsible for creating the produced Table. After
 * the writer has finished the band layout process, the layouted bands are
 * forwarded into the TableProducer. The TableProducer coordinates the cell
 * creation process and collects the generated TableCellData. The raw CellData
 * objects are later transformed into a TableGridLayout.
 * <p>
 * This class defines the global contract and provides some helper methods for
 * the implementors.
 *
 * @author Thomas Morgner.
 */
public class CSVTableProducer extends TableProducer
{
  /** The (character stream) writer that is used to write the generated contents. */
  private PrintWriter writer;

  /** The CSVQuoter that is used when writing the content. */
  private CSVQuoter quoter;

  /**
   * The class used to convert each {@link org.jfree.report.Element}
   * into a {@link CSVCellData} instance.
   */
  private CSVCellDataFactory cellDataFactory;

  /** A flag that maintains the open state. */
  private boolean isOpen;

  /**
   * Creates a new <code>CSVTableProducer</code> that will compute
   * a layout for the output.
   *
   * @param strict  the strict mode that is used for the layouting.
   * @param gridBoundsCollection the table layout that will contain the
   * computed grid boundaries.
   */
  public CSVTableProducer(final TableLayoutInfo gridBoundsCollection, final boolean strict)
  {
    super(gridBoundsCollection, strict);
    this.cellDataFactory = new CSVCellDataFactory();
  }

  /**
   * Creates a CSVTableProducer that will use the given table layout to
   * produce an output.
   *
   * @param writer the writer that will receive the generated contents.
   * @param gridBoundsCollection the tablelayout that contains the grid
   * boundries from the repagination process.
   */
  public CSVTableProducer(final TableLayoutInfo gridBoundsCollection, final Writer writer)
  {
    super(gridBoundsCollection);
    if (writer == null)
    {
      throw new NullPointerException("Writer is null");
    }
    this.cellDataFactory = new CSVCellDataFactory();
    this.writer = new PrintWriter(writer);
  }

  /**
   * Handles the opening of the producer. Only maintains the open state.
   */
  public void open()
  {
    isOpen = true;
  }

  /**
   * Handles the closing of the producer. Only maintains the open state.
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
   * output.
   */
  public void commit()
  {
    if (isDummy() == false && isLayoutContainsContent())
    {
      generateContent(layoutGrid());
      clearCells();
    }
  }

  /**
   * Generates the output.
   *
   * @param layout  contains the layouted CSVCellData objects.
   */
  private void generateContent(final TableGridLayout layout)
  {
    for (int y = 0; y < layout.getHeight(); y++)
    {
      for (int x = 0; x < layout.getWidth(); x++)
      {
        final TableGridLayout.Element gridPosition = layout.getData(x, y);
        if (gridPosition == null)
        {
          writer.print(quoter.getSeparator());
          continue;
        }

        if (gridPosition.getRoot() == null)
        {
          writer.print(quoter.getSeparator());
          continue;
        }

        final TableGridPosition pos = gridPosition.getRoot();
        if (pos.isOrigin(x, y) == false)
        {
          // colspanned cell
          writer.print(quoter.getSeparator());
          continue;
        }

        if (pos.getElement() != null)
        {
          final CSVCellData cellData = (CSVCellData) pos.getElement();

          writer.print(quoter.doQuoting(cellData.getValue()));
          writer.print(quoter.getSeparator());
          //x += pos.getColSpan() - 1;
        }
        else
        {
          // pure background cell
          writer.print(quoter.getSeparator());
          continue;
        }
      }
      writer.println();
    }

  }

  /**
   * Gets the CSVTableProducer's table cell data factory.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
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
   * Configures the table producer by reading the configuration settings from
   * the given map.
   *
   * @param configuration the configuration supplied by the table processor.
   */
  public void configure(final Properties configuration)
  {
    super.configure(configuration);
    this.quoter = new CSVQuoter(getSeparator());
  }

  /**
   * Returns the field separator from the configuration.
   * 
   * @return the separator.
   */
  protected String getSeparator()
  {
    return getProperty(CSVTableProcessor.SEPARATOR_KEY, ",");
  }

  /**
   * Returns always true, as the CSV module always generates a global layout.
   * @see org.jfree.report.modules.output.table.base.TableProducer#isGlobalLayout()
   *
   * @return true.
   */
  public boolean isGlobalLayout()
  {
    return true;
  }
}
