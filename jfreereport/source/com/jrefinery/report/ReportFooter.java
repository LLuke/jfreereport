/* =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------------
 * ReportFooter.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 *
 */

package com.jrefinery.report;

import java.util.Collection;

/**
 * A report band that appears as the very last band on the report.
 */
public class ReportFooter extends Band {

    /** A flag that indicates that the report footer should appear on its own page. */
    protected boolean ownPage;

    /**
     * Constructs a report footer containing no elements.
     *
     * @param height The band height (in points).
     */
    public ReportFooter(float height) {
        this(height, null, false);
    }

    /**
     * Constructs a report footer containing some elements.
     *
     * @param height The band height (in points).
     * @param elements The elements.
     */
    public ReportFooter(float height, Collection elements) {
        this(height, elements, false);
    }

    /**
     * Constructs a report footer containing no elements.
     *
     * @param height The band height (in points).
     * @param ownPage A flag controlling whether or not a page break is inserted prior to the
     *                report footer.
     */
    public ReportFooter(float height, boolean ownPage) {
        this(height, null, ownPage);
    }

    /**
     * Constructs a report footer with the specified height, containing the specified elements.
     *
     * @param height The height of the band.
     * @param elements The collection of elements in the band.
     */
    public ReportFooter(float height, Collection elements, boolean ownPage) {
        super(height, elements);
        this.ownPage = ownPage;
    }

    /**
     * Returns true if the report footer should be shown on its own page.
     */
    public boolean getOwnPage() {
        return this.ownPage;
    }

}