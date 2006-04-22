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
 * StaticExpressionRuntimeData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StaticExpressionRuntimeData.java,v 1.1 2006/04/18 11:49:11 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.data;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.structure.Element;
import org.jfree.report.ReportData;
import org.jfree.util.Configuration;

/**
 * This class holds all expression-runtime information, which are known when
 * the expression gets added to the datarow. Once added, they won't change
 * anymore.
 *
 * @author Thomas Morgner
 */
public class StaticExpressionRuntimeData
{
  private Element declaringParent;
  private Configuration configuration;
  private ResourceBundleFactory resourceBundleFactory;
  private OutputProcessorMetaData metaData;
  private ReportData data;
  private int currentRow;

  public StaticExpressionRuntimeData()
  {
  }

  public int getCurrentRow()
  {
    return currentRow;
  }

  public void setCurrentRow(final int currentRow)
  {
    this.currentRow = currentRow;
  }

  public ReportData getData()
  {
    return data;
  }

  public void setData(final ReportData data)
  {
    this.data = data;
  }

  /**
   * The output descriptor is a simple string collections consisting of the
   * following components: exportclass/type/subtype
   * <p/>
   * For example, the PDF export would be: pageable/pdf The StreamHTML export
   * would return table/html/stream
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor()
  {
    OutputProcessorMetaData metaData = getOutputProcessorMetaData();
    if (metaData == null)
    {
      return null;
    }
    return metaData.getExportDescriptor();
  }

  public OutputProcessorMetaData getOutputProcessorMetaData()
  {
    return metaData;
  }

  public void setOutputProcessorMetaData(final OutputProcessorMetaData metaData)
  {
    this.metaData = metaData;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  public void setDeclaringParent(final Element declaringParent)
  {
    this.declaringParent = declaringParent;
  }

  public void setConfiguration(final Configuration configuration)
  {
    this.configuration = configuration;
  }

  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public Element getDeclaringParent()
  {
    return declaringParent;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }
}
