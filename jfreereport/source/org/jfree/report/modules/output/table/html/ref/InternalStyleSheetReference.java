/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: InternalStyleSheetReference.java,v 1.2.2.1 2004/12/13 19:27:09 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html.ref;

/**
 * Encapsulates an internal stylesheet definition.
 *
 * @author Thomas Morgner
 */
public class InternalStyleSheetReference extends HtmlReference
{
  /** the stylesheet definition. */
  private final String styleData;

  /**
   * Creates an internal style sheet definition.
   *
   * @param data the style sheet definition data.
   */
  public InternalStyleSheetReference(final String data)
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
  public String getReferenceData()
  {
    return styleData;
  }
}
