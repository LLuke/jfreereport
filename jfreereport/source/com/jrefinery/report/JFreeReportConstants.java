/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -------------------------
 * JFreeReportConstants.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: JFreeReportConstants.java,v 1.10 2002/08/20 21:13:05 taqua Exp $
 *
 * Changes
 * -------
 * 28-Feb-2002 : Version 1, code transferred out of JFreeReport.java (DG);
 * 07-May-2002 : Removed info constants, these are now in JFreeReportInfo class in the
 *               JFreeReport.java source file.  Added action constants (DG);
 * 10-May-2002 : Documentation
 * 09-Jun-2002 : Removed the action commands, actions connect directly to their source.
 */

package com.jrefinery.report;

/**
 * Some constants for JFreeReport and PreviewPane.
 *
 * @author DG
 */
public interface JFreeReportConstants
{

  /** Report property key. */
  public static final String NAME_PROPERTY = "report.name";

  /** Report property key. */
  public static final String REPORT_DATE_PROPERTY = "report.date";

  /** Report property key. */
  public static final String REPORT_PAGEFORMAT_PROPERTY = "report.pageformat";

  /** Report property key. */
  public static final String REPORT_PAGECOUNT_PROPERTY = "report.pagecount";

  /** Report property key. */
  public static final String REPORT_PREPARERUN_PROPERTY = "report.preparerun";

  /** Report property key. */
  public static final String REPORT_DEFINITION_SOURCE = "report.definition.source";

  /** Report property key. */
  public static final String REPORT_DEFINITION_CONTENTBASE = "report.definition.contentbase";

  /** A useful constant that signals that a page is full. */
  public static final boolean PAGE_FULL = true;

  /** A useful constant that signals that a page is not yet full. */
  public static final boolean PAGE_NOT_FULL = false;

}
