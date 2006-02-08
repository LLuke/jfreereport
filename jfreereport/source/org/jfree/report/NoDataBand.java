/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * NoDataBand.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 07.02.2006 : Initial version
 */
package org.jfree.report;

/**
 * Creation-Date: 07.02.2006, 21:51:46
 *
 * @author Thomas Morgner
 */
public class NoDataBand extends Band implements RootLevelBand
{
  /** Constructs a new band (initially empty). */
  public NoDataBand()
  {
  }

  /**
   * Constructs a new band with the given pagebreak attributes. Pagebreak
   * attributes have no effect on subbands.
   *
   * @param pagebreakAfter  defines, whether a pagebreak should be done after
   *                        that band was printed.
   * @param pagebreakBefore defines, whether a pagebreak should be done before
   *                        that band gets printed.
   */
  public NoDataBand(final boolean pagebreakBefore, final boolean pagebreakAfter)
  {
    super(pagebreakBefore, pagebreakAfter);
  }

  /**
   * Assigns the report definition to this band.
   *
   * @param reportDefinition the report definition or null, if the band is not
   *                         part of a valid report definition.
   */
  public void setReportDefinition(final ReportDefinition reportDefinition)
  {
    super.setReportDefinition(reportDefinition);
  }
}
