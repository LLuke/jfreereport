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
 * ---------------
 * PageHeader.java
 * ---------------
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
 * A report band used to print information at the top of every page in the report.  There is an
 * option to suppress the page header on the first page.
 */
public class PageHeader extends Band {

    /** Show the header on the first page? */
    protected boolean displayOnFirstPage;

    /**
     * Constructs a page header.
     *
     * @param height The height (in points).
     */
    public PageHeader(float height) {
        this(height, null, false);
    }

    /**
     * Constructs a page header
     */
    public PageHeader(float height, boolean displayOnFirstPage) {
        this(height, null, displayOnFirstPage);
    }

    /**
     * Constructs a header with the specified height containing the specified elements.
     * @param height The height of the band.
     * @param elements The collection of elements to add to the band.
     */
    public PageHeader(float height, Collection elements) {
        this(height, elements, false);
    }

    /**
     * Constructs a header with the specified height containing the specified
     * collection of elements, and sets the flag that determines whether or not the header is
     * displayed on the first page.
     *
     * @param height The height of the band.
     * @param elements The collection of elements to add to the band.
     * @param showOnFirstPage A flag that determines whether or not the header is shown on the
     *                        first page.
     */
    public PageHeader(float height, Collection elements,
                      boolean displayOnFirstPage) {

        super(height, elements);
        this.displayOnFirstPage = displayOnFirstPage;

    }

    /**
     * Returns true if the header should be shown on the first page.
     * <P>
     * You might decide to suppress the page header on page 1 of the report if there is a report
     * header.
     *
     * @return True if the header should be shown on the first page.
     */
    public boolean displayOnFirstPage() {
        return this.displayOnFirstPage;
    }

}