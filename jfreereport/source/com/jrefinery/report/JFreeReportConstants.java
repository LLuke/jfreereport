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
 * -------------------------
 * JFreeReportConstants.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 28-Feb-2002 : Version 1, code transferred out of JFreeReport.java (DG);
 *
 */

package com.jrefinery.report;

import java.util.Arrays;
import java.util.List;
import com.jrefinery.JCommon;
import com.jrefinery.ui.about.Licences;
import com.jrefinery.ui.about.Contributor;
import com.jrefinery.ui.about.Library;

public interface JFreeReportConstants {

    /** The name of the library. */
    public static final String NAME = "JFreeReport";

    /** Version number. */
    public static final String VERSION = "0.7.0";

    /** Information. */
    public static final String INFO = "http://www.object-refinery.com/jfreereport";

    /** Copyright. */
    public static final String COPYRIGHT = "(C)opyright 2000-2002, Simba Management Limited "
                                          +"and Contributors";

    /** The licence. */
    public static final String LICENCE = Licences.LGPL;

    /** The contributors. */
    public static final List CONTRIBUTORS = Arrays.asList(

        new Contributor[] {
            new Contributor("David Gilbert", "david.gilbert@jrefinery.com"),
            new Contributor("Thomas Morgner", "-")
        }

    );

    /** The libraries that JFreeReport uses. */
    public static final List LIBRARIES = Arrays.asList(

        new Library[] {

            new Library(JCommon.NAME, JCommon.VERSION, "LGPL", JCommon.INFO),
            new Library("iText", "0.85", "LGPL", "http://www.lowagie.com/iText/index.html"),
            new Library("GNU JAXP", "1.0beta1", "GPL with library exception",
                        "http://www.gnu.org/software/classpathx/jaxp/")

        }

    );

    /** A useful constant that signals that a page is full. */
    public static final boolean PAGE_FULL = true;

    /** A useful constant that signals that a page is not yet full. */
    public static final boolean PAGE_NOT_FULL = false;

}