/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------
 * ImageReferenceData.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageReferenceData.java,v 1.3 2003/08/25 14:29:32 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

/**
 * Defines a external image reference to an image file. The image is included
 * in the generated content using the &lt;IMG&gt; html-tag.
 *
 * @author Thomas Morgner
 */
public class ImageReferenceData extends HtmlReferenceData
{
  /** the referenced file name. */
  private final String reference;

  /**
   * Creates a new external image reference.
   *
   * @param reference the referenced file name.
   */
  public ImageReferenceData(final String reference)
  {
    super(true);
    if (reference == null)
    {
      throw new NullPointerException("reference string is null.");
    }
    this.reference = reference;
  }

  /**
   * Generates the reference fragment, which should be inserted into the HTML-Code.
   * This implementation returns the referenced file name.
   *
   * @return the referenced file name.
   */
  public String getReference()
  {
    return reference;
  }
}
