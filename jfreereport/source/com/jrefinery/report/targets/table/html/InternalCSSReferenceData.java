/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------------------
 * InternalCSSReferenceData.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: InternalCSSReferenceData.java,v 1.6 2003/06/27 14:25:25 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

/**
 * Encapsulates an internal stylesheet definition.
 *
 * @author Thomas Morgner
 */
public class InternalCSSReferenceData extends HtmlReferenceData
{
  /** the stylesheet definition. */
  private String styleData;

  /**
   * Creates an internal style sheet definition.
   *
   * @param data the style sheet definition data.
   */
  public InternalCSSReferenceData(final String data)
  {
    super(false);
    styleData = data;
  }

  /**
   * Generates the reference fragment, which should be inserted into the HTML-Code.
   * Which content is returned depends on the reference type and the target filesystem.
   *
   * @return the reference code, which should be inserted into the generated HTML-Code.
   */
  public String getReference()
  {
    return styleData;
  }
}
