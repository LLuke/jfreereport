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
 * $Id: LabelElement.java,v 1.1.1.1 2002/04/25 17:02:13 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 *
 */

package com.jrefinery.report;


/**
 * An element that draws static text within a report band.
 */
public class LabelElement extends TextElement
{

  /** Text to display. */
  private String label;


  /**
   * Constructs a label element using float coordinates.
   */
  public LabelElement ()
  {
  }

  /**
   * Returns the label.
   * @return The label.
   */
  public String getFormattedText ()
  {
    return String.valueOf (label);
  }

  /**
   * Returns the label.
   * @return The label.
   */
  public String getLabel ()
  {
    return getFormattedText ();
  }

  /**
   * defines the label text.
   * @param label the new label text.
   */
  public void setLabel (String label)
  {
    this.label = label;
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