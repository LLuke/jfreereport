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
 * ReportStateConstants.java
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
 * 19-Feb-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report;

/**
 * Constants used by the ReportState class.
 */
public interface ReportStateConstants {

    /** A constant representing the START state. */
    public static final int START = 0;

    /** A constant representing the GROUP_START state. */
    public static final int GROUP_START = 1;

    /** A constant representing the PRE_GROUP_HEADER state. */
    public static final int PRE_GROUP_HEADER = 2;

    /** A constant representing the POST_GROUP_HEADER state. */
    public static final int POST_GROUP_HEADER = 3;

    /** A constant representing the PRE_ITEM_GROUP_HEADER state. */
    public static final int PRE_ITEM_GROUP_HEADER = 4;

    /** A constant representing the POST_ITEM_GROUP_HEADER state. */
    public static final int POST_ITEM_GROUP_HEADER = 5;

    /** A constant representing the IN_ITEM_GROUP state. */
    public static final int IN_ITEM_GROUP = 6;

    /** A constant representing the PRE_ITEM_GROUP_FOOTER state. */
    public static final int PRE_ITEM_GROUP_FOOTER = 7;

    /** A constant representing the POST_ITEM_GROUP_FOOTER state. */
    public static final int POST_ITEM_GROUP_FOOTER = 8;

    /** A constant representing the PRE_GROUP_FOOTER state. */
    public static final int PRE_GROUP_FOOTER = 9;

    /** A constant representing the POST_GROUP_FOOTER state. */
    public static final int POST_GROUP_FOOTER = 10;

    /** A constant representing the GROUP_FINISH state. */
    public static final int GROUP_FINISH = 11;

    /** A constant representing the PRE_REPORT_FOOTER state. */
    public static final int PRE_REPORT_FOOTER = 12;

    /** The FINISH state. */
    public static final int FINISH = 13;

}