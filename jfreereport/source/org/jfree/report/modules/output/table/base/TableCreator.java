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
 * TableCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * Feb 26, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;

/**
 * The table creator is a streaming interface for the table producers.
 * Metabands get processed by TableCreators to either compute a layout
 * or to create the actual content.
 * <p>
 * The order of the metabands is not defined, it depends on the tablewriter
 * implementation. It is guaranteed, that once a metaband is <code>flush</code>ed,
 * it is safe to write the band to the output stream.
 * <p>
 * When flush is called, it is guaranteed, that the subsequent bands will
 * be printed below the flushed bands. Flushed and non-flushed content will
 * never overlap.
 */
public interface TableCreator
{
  /**
   * Starts the report processing. This method is called only once
   * per report processing. The TableCreator might use the report
   * definition to configure itself and to perform startup operations.
   *
   * @param report the report definition.
   */
  public void open(ReportDefinition report) throws ReportProcessingException;

  /**
   * Begins a table. A table is considered a closed entity, it usually
   * represents a sheet or a single page. Table headers and table properties
   * can be defined using the given report definition.
   *
   * @param report the report definiton.
   */
  public void beginTable (ReportDefinition report) throws ReportProcessingException;

  /**
   * Processes the given metaband. The MetaBandProducer has already collected
   * all necessary data to allow the content creation. Table implementors
   * should provide their own MetaBandProducer if they need additional properties.
   *
   * @param band the metaband that is processed.
   */
  public void processBand (MetaBand band);

  /**
   * Finishes the current table.
   */
  public void endTable () throws ReportProcessingException;

  /**
   * Closes the report processing.
   */
  public void close() throws ReportProcessingException;

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen();

  /**
   * Checks, whether the current table contains content. Returns true,
   * if there is no current table open.
   *
   * @return true, if the table does not contain content, false otherwise.
   */
  public boolean isEmpty();

  /**
   * Commits all bands. See the class description for details on the
   * flushing process.
   * 
   * @return true, if the content was flushed, false otherwise.
   */
  public boolean flush() throws ReportProcessingException;
}
