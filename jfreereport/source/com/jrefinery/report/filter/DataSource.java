/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * DataSource.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 20-May-2002 : Initial version
 */
package com.jrefinery.report.filter;

/**
 * A DataSource is a producer in the data chain. Common Sources are StaticSources (predefined
 * data), ReportDataSources (data filled from the reports data set) or FunctionDataSource (the
 * data is filled by quering an assigned function).
 */
public interface DataSource
{
  public Object getValue ();
}
