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
 * ReportTarget.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.DataFlags;
import org.jfree.report.JFreeReport;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;

/**
 * The report target is responsible for the content creation. There are targets
 * which forward all incomming calls to LibLayout, while other targets process
 * the content directly.
 *
 * @author Thomas Morgner
 */
public interface ReportTarget
{
  void processNode (Node node, ExpressionRuntime runtime)
          throws DataSourceException;

  void processContentElement (ContentElement node,
                              DataFlags value,
                              ExpressionRuntime runtime)
          throws DataSourceException;

  void startElement (Element node, ExpressionRuntime runtime)
          throws DataSourceException;

  void endElement (Element node, ExpressionRuntime runtime)
          throws DataSourceException;

  void startReport (JFreeReport report)
          throws DataSourceException;

  void endReport (JFreeReport report)
          throws DataSourceException;

}
