/**
 * =============================================================
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
 * ----------------
 * DataElement.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DataElement.java,v 1.9 2002/07/03 18:49:45 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Changed constructors from public --> protected (DG);
 * 10-May-2002 : Removed all complex constructors and declared abstract
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 * 04-Jun-2002 : Documentation, declared the internal function getReportDataSource final.
 * 02-Jul-2002 : Simpliefied TextElements filter handling
 * 04-Jul-2002 : Serializable and Cloneable
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.ReportDataSource;
import com.jrefinery.report.filter.DataFilter;
import com.jrefinery.report.filter.FunctionDataSource;

/**
 * The base class for all report elements that display data (that is, information from the report's
 * data source) rather than just static information.
 * <p>
 * This element is deprecated since release 0.7.3. Use the filter API to form the same
 * functionality. The filter API provides the class ReportDataSource for accessing fields
 * of the report datasources current row.
 *
 * @deprecated form this element by stacking it together by using filters
 */
public abstract class DataElement extends TextElement
{
  private ReportDataSource fieldsource;

  /**
   * Constructs a data element using float coordinates.
   * The fieldname is initialized to an empty string.
   * @deprecated Use filters to form a data element
   */
  protected DataElement()
  {
    fieldsource = new ReportDataSource();
    fieldsource.setField("");

    // Register this elements data source with the text elements string filter.
    setDataSource(fieldsource);
  }

  /**
   * Sets the fieldname for this element.
   *
   * @throws NullPointerException if the field is null.
   * @deprecated Use filters to form a data element
   */
  public void setField(String fieldname)
  {
    if (fieldname == null)
      throw new NullPointerException("Fieldname must not be null for field " + getName());

    fieldsource.setField(fieldname);
  }

  /**
   * Returns the name of the field in the data source that this element obtains its data from.
   * @return The field name.
   * @deprecated Use filters to form a data element
   */
  public String getField()
  {
    return fieldsource.getField();
  }

  /**
   * @returns the reportdatasource assigned to this field. Make sure you add this to the
   * end of the chain or you will not see any results.
   */
  protected final ReportDataSource getReportDataSource ()
  {
    return fieldsource;
  }


  public Object clone () throws CloneNotSupportedException
  {
    DataElement e = (DataElement) super.clone();
    if ((e.getDataSource() instanceof ReportDataSource) == false)
    {
      throw new CloneNotSupportedException("Modified data element is not clonable");
    }
    e.fieldsource = (ReportDataSource) e.getDataSource();
    return e;
  }

}
