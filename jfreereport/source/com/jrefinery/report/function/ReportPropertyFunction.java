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
 * ---------------------------
 * ReportPropertyFunction.java
 * ---------------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1 (DG);
 * 18-Apr-2002 : Using the generator to create a function will create
 *               the function using the default constructor. In this 
 *               case, field was null and raised a nullpointerexception.
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 */

package com.jrefinery.report.function;

import com.jrefinery.report.JFreeReport;

/**
 * A report function that returns a report property.
 */
public class ReportPropertyFunction extends AbstractFunction {

    /** The function value. */
    protected Object value;
    private String field;
    
    public ReportPropertyFunction ()
    {
      this (null, null);
    }

    /**
     * Constructs a new function.
     *
     * @param name The function name.
     * @param propertyNam The property name.
     */
    public ReportPropertyFunction(String name, String propertyName) {

        super(name);
        this.field = propertyName;
        this.value = "-";

    }

    /**
     * Receives notification that a new report is about to start.
     */
    public void startReport(JFreeReport report) {

      Object value = report.getProperty(field);
      if (value!=null) 
      {
        this.value = value;
      }
    }

    /**
     * Returns the function's value.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns a copy of this function.
     */
    public Object clone() {

        Object result = null;

        try {
            result = super.clone();
        }
        catch (CloneNotSupportedException e) {
            // this should never happen...
            System.err.println("ReportProertyFunction: clone not supported");
        }

        return result;

    }

    public String getField ()
    {
      return field;
    }
    
    public void setField (String field)
    {
      if (field == null)
        throw new NullPointerException ();
      this.field = field;  
    }


   public boolean isInitialized ()
   {
     String fieldProp = getProperty ("field");
     if (fieldProp == null)
     {
        System.out.println ("No Such Property : field");
        return false;
     }
     setField (fieldProp);
     return true;       
   }
  
}