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
 * ProxyExpressionRuntime.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ProxyExpressionRuntime.java,v 1.1 2006/04/30 09:49:10 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.expressions;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.report.DataRow;
import org.jfree.report.ReportData;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.structure.Element;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 22.04.2006, 14:17:54
 *
 * @author Thomas Morgner
 */
public class ProxyExpressionRuntime implements ExpressionRuntime
{
  private ExpressionRuntime parent;

  public ProxyExpressionRuntime(final ExpressionRuntime parent)
  {
    if (parent == null)
    {
      throw new NullPointerException();
    }
    this.parent = parent;
  }

  /**
   * Returns the datarow.
   *
   * @return
   */
  public DataRow getDataRow()
  {
    return parent.getDataRow();
  }

  public Configuration getConfiguration()
  {
    return parent.getConfiguration();
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return parent.getResourceBundleFactory();
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
    return parent.getData();
  }

  public int getCurrentRow()
  {
    return parent.getCurrentRow();
  }

  public Element getDeclaringParent()
  {
    return parent.getDeclaringParent();
  }

  public OutputProcessorMetaData getOutputMetaData()
  {
    return parent.getOutputMetaData();
  }
}