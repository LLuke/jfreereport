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
 * VerifyTableProcessor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: VerifyTableProcessor.java,v 1.1 2003/10/11 21:36:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table.tableverify;

import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.base.TableProducer;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;

public class VerifyTableProcessor extends TableProcessor
{
  private boolean globalLayout;
  private VerifyPattern content;

  public VerifyTableProcessor(final JFreeReport report) 
      throws ReportProcessingException, FunctionInitializeException
  {
    super(report);
  }

  public boolean isGlobalLayout()
  {
    return globalLayout;
  }

  public void setGlobalLayout(boolean globalLayout)
  {
    this.globalLayout = globalLayout;
  }

  /**
   * Creates a dummy TableProducer. The TableProducer is responsible to compute the layout.
   *
   * @return the created table producer, never null.
   */
  protected TableProducer createDummyProducer()
  {
    return new VerifyTableProducer
        (new TableLayoutInfo(isGlobalLayout(),
            getReport().getDefaultPageFormat()), isStrictLayout());
  }

  public VerifyPattern getContent()
  {
    return content;
  }

  public void setContent(VerifyPattern content)
  {
    this.content = content;
  }

  /**
   * Creates a TableProducer. The TableProducer is responsible to create the table.
   *
   * @param gridLayoutBounds the grid layout that contain the bounds from the pagination run.
   * @return the created table producer, never null.
   */
  protected TableProducer createProducer(TableLayoutInfo gridLayoutBounds)
  {
    return new VerifyTableProducer(gridLayoutBounds, getContent());
  }

  /**
   * Gets the report configuration prefix for that processor. This prefix defines
   * how to map the property names into the global report configuration.
   *
   * @return the report configuration prefix.
   */
  protected String getReportConfigurationPrefix()
  {
    return "org.jfree.report.ext.junit.base.basic.modules.table.tableverify.";
  }
}
