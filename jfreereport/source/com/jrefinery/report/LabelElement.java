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
 * -----------------
 * LabelElement.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.StaticDataSource;


/**
 * An element that draws static text within a report band.
 * @deprecated form this element by stacking it together by using filters
 */
public class LabelElement extends TextElement
{

  /** Text to display. */
  private StaticDataSource label;


  /**
   * Constructs a label element using float coordinates.
   * @deprecated form this element by stacking it together by using filters
   */
  public LabelElement ()
  {
    label = new StaticDataSource();
    getTextFilter().setDataSource(label);
  }

  /**
   * Returns the label.
   * @return The label.
   * @deprecated form this element by stacking it together by using filters
   */
  public String getLabel ()
  {
    return String.valueOf(label.getValue());
  }

  /**
   * defines the label text.
   * @param label the new label text.
   * @deprecated form this element by stacking it together by using filters
   */
  public void setLabel (String label)
  {
    this.label.setValue(label);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("Label={ name=");
    b.append (getName ());
    b.append (", bounds=");
    b.append (getBounds ());
    b.append (", font=");
    b.append (getFont ());
    b.append (", text=");
    b.append (getLabel ());
    b.append ("}");
    return b.toString ();
  }

}
