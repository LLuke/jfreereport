/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ----------------------
 * HRefReferenceData.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HRefReferenceData.java,v 1.4 2003/02/25 15:42:38 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

/**
 * Denotes a untagged href-reference. This reference is used to address external
 * stylesheet definitions.
 * 
 * @author Thomas Morgner
 */
public class HRefReferenceData extends HtmlReferenceData
{
  /** the referenced file or URL. */
  private String reference;

  /**
   * creates a new HREF-Reference for the given file.
   *
   * @param reference the referenced resource.
   */
  public HRefReferenceData(String reference)
  {
    super(true);
    if (reference == null) 
    {
      throw new NullPointerException();
    }
    this.reference = reference;
  }

  /**
   * Gets the referenced resource as HREF-Reference.
   *
   * @return the HREF resource reference.
   */
  public String getReference()
  {
    return "href=\"" + reference + "\"";
  }
}
