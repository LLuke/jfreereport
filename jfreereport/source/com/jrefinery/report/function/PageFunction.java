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
 * PageFunction.java
 * -----------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 *
 */

package com.jrefinery.report.function;

/**
 * A report function that counts pages.
 */
public class PageFunction extends AbstractFunction {

    /** The page. */
    private int page;

    /**
     * Default constructor.
     */
    public PageFunction() {
        this(null);
    }

    /**
     * Constructs a named function.
     */
    public PageFunction(String name) {
        super(name);
        page = 1;
    }

    /**
     * Receives notification from the report engine that a new page is starting.
     */
    public void startPage(int page) {
  	this.page = page;
    }

    /**
     * Returns the function value.
     */
    public Object getValue() {
  	return new Integer(page);
    }

    public boolean isInitialized ()
    {
      return true;
    }
}