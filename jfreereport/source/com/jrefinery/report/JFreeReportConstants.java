/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -------------------------
 * JFreeReportConstants.java
 * -------------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportConstants.java,v 1.13 2003/02/25 14:06:32 taqua Exp $
 *
 * Changes
 * -------
 * 28-Feb-2002 : Version 1, code transferred out of JFreeReport.java (DG);
 * 07-May-2002 : Removed info constants, these are now in JFreeReportInfo class in the
 *               JFreeReport.java source file.  Added action constants (DG);
 * 10-May-2002 : Documentation
 * 09-Jun-2002 : Removed the action commands, actions connect directly to their source.
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

/**
 * An interface that defines some useful constants used by the {@link JFreeReport} and 
 * {@link ReportState} classes.
 * <p>
 * A number of report property keys are defined.  These can be used to access report properties
 * using the <code>getProperty(String)</code> method in the <code>JFreeReport</code> class.
 *
 * @author David Gilbert
 */
public interface JFreeReportConstants
{

  /** Key for the 'report name' property. */
  public static final String NAME_PROPERTY = "report.name";

  /** Key for the 'report date' property. */
  public static final String REPORT_DATE_PROPERTY = "report.date";

  /** Key for the 'report page format' property. */
  public static final String REPORT_PAGEFORMAT_PROPERTY = "report.pageformat";

  /** Key for the 'report prepare run' property. */
  public static final String REPORT_PREPARERUN_PROPERTY = "report.preparerun";

  /** Key for the 'report definition source' property. */
  public static final String REPORT_DEFINITION_SOURCE = "report.definition.source";

  /** Key for the 'report definition content base' property. */
  public static final String REPORT_DEFINITION_CONTENTBASE = "report.definition.contentbase";

  /**
   * Key for the 'report page count' property.
   * 
   * @deprecated pagecount should be calculated by functions. The property is
   *             no longer filled by the ReportProcessor.
   */
  public static final String REPORT_PAGECOUNT_PROPERTY = "report.pagecount";

}
