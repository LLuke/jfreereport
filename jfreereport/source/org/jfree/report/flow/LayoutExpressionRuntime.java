/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * LayoutExpressionRuntime.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LayoutExpressionRuntime.java,v 1.2 2006/04/22 16:18:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.expressions.ExpressionRuntime;
import org.jfree.report.DataRow;
import org.jfree.report.ReportData;
import org.jfree.report.structure.Element;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.util.Configuration;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 04.03.2006, 16:41:49
 *
 * @author Thomas Morgner
 */
public class LayoutExpressionRuntime implements ExpressionRuntime
{
  private DataRow dataRow;
  private Configuration configuration;
  private ResourceBundleFactory resourceBundleFactory;
  private ReportData reportData;
  private Element declaringParent;
  private OutputProcessorMetaData metaData;
  private int currentRow;

  public LayoutExpressionRuntime()
  {
  }

  public void setCurrentRow(final int currentRow)
  {
    this.currentRow = currentRow;
  }

  public void setDataRow(final DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  public void setConfiguration(final Configuration configuration)
  {
    this.configuration = configuration;
  }

  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public void setData(final ReportData reportData)
  {
    this.reportData = reportData;
  }

  public void setDeclaringParent(final Element declaringParent)
  {
    this.declaringParent = declaringParent;
  }

  public void setOutputMetaData(final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
  }


  /**
   * Returns the datarow.
   *
   * @return
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  /**
   * Returns the report data used in this section. If subreports are used, this
   * does not reflect the complete report data.
   * <p/>
   * All access to the report data must be properly synchronized. Failure to do
   * so may result in funny results. Do not assume that the report data will be
   * initialized on the current cursor positon.
   *
   * @return
   */
  public ReportData getData()
  {
    return reportData;
  }

  public Element getDeclaringParent()
  {
    return declaringParent;
  }

  public OutputProcessorMetaData getOutputMetaData()
  {
    return metaData;
  }

  public int getCurrentRow()
  {
    return currentRow;
  }
}
