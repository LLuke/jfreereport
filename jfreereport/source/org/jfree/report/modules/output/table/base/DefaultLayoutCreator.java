/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * LayoutCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DefaultLayoutCreator.java,v 1.3 2005/01/25 00:12:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Feb 26, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.ReportDefinition;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaElement;

public class DefaultLayoutCreator extends AbstractTableCreator
        implements LayoutCreator
{
  private boolean open;
  private SheetLayoutCollection sheetLayoutCollection;
  private SheetLayout currentSheet;
  private String configurationPrefix;

  public DefaultLayoutCreator (final String configurationPrefix)
  {
    if (configurationPrefix == null)
    {
      throw new NullPointerException();
    }
    this.configurationPrefix = configurationPrefix;
  }

  /**
   * Starts the report processing. This method is called only once per report processing.
   * The TableCreator might use the report definition to configure itself and to perform
   * startup operations.
   *
   * @param report the report definition.
   */
  public void open (final ReportDefinition report)
  {
    open = true;
    final boolean globalLayout = (report.getReportConfiguration().getConfigProperty
            (configurationPrefix + "." + TableProcessor.GLOBAL_LAYOUT,
                    TableProcessor.GLOBAL_LAYOUT_DEFAULT).equals("true"));
    sheetLayoutCollection = new SheetLayoutCollection(globalLayout);
  }

  /**
   * Begins a table. A table is considered a closed entity, it usually represents a sheet
   * or a single page. Table headers and table properties can be defined using the given
   * report definition.
   *
   * @param report the report definiton.
   */
  public void beginTable (final ReportDefinition report)
  {
    final boolean strictLayout = (report.getReportConfiguration().getConfigProperty
            (configurationPrefix + "." + TableProcessor.STRICT_LAYOUT,
                    TableProcessor.STRICT_LAYOUT_DEFAULT).equals("true"));
    setSheetLayout(createSheetLayout(strictLayout));
  }

  protected void setSheetLayout (final SheetLayout sheetLayout)
  {
    this.currentSheet = sheetLayout;
  }

  protected SheetLayout createSheetLayout (final boolean strict)
  {
    return new SheetLayout(strict);
  }

  /**
   * Returns the currently processed sheet.
   *
   * @return the current sheet.
   */
  protected SheetLayout getCurrentSheet ()
  {
    return currentSheet;
  }

  public SheetLayoutCollection getSheetLayoutCollection ()
  {
    return sheetLayoutCollection;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   * <p/>
   *
   * @param e the element.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected void processElement (final MetaElement e)
  {
    currentSheet.add(e);
  }

  /**
   * Add the specified band definition to the table sheet. By default, Band definitions
   * are not used to create content, but they might be important for the layout. it is up
   * to the implementor to decide whether to use the supplied content of the band (if
   * any).
   *
   * @param e the element.
   * @return true, if the band is fully processed and the children should be ignored,
   *         false to indicate that we need the children to complete the process.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected boolean processBandDefinition (final MetaBand e)
  {
    // only add the bounds, ignore the content ...
    currentSheet.add(e);
    return false;
  }

  /**
   * Finishes the current table.
   */
  public void endTable ()
  {
    currentSheet.pageCompleted();
    sheetLayoutCollection.addLayout(currentSheet);
    currentSheet = null;
  }

  /**
   * Closes the report processing.
   */
  public void close ()
  {
    open = false;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return always false, as the layout is a global thing.
   */
  public boolean flush ()
  {
    return false;
  }

  /**
   * Checks, whether the current table contains content. Returns true, if there is no
   * current table open.
   *
   * @return true, if the table does not contain content, false otherwise.
   */
  public boolean isEmpty ()
  {
    if (currentSheet == null)
    {
      return true;
    }
    return currentSheet.isEmpty();
  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen ()
  {
    return open;
  }
}
