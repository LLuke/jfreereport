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
 * ----------------------
 * HtmlReferenceData.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlReferenceData.java,v 1.6 2003/06/29 16:59:30 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

/**
 * The base class for all HtmlReferences. HtmlReferences link the main document
 * with the supplementary data, like images and stylesheets.
 * <p>
 * If the reference is external, then the referenced content is stored outside
 * the main Html-File.
 *
 * @author Thomas Morgner
 */
public abstract class HtmlReferenceData
{
  /** A flag indicating whether this reference points to external data.  */
  private boolean external;

  /**
   * A reference: a fragment which could be inserted into the generated HTML-Code.
   *
   * @param external if the generated reference points to an external resource.
   */
  protected HtmlReferenceData(final boolean external)
  {
    this.external = external;
  }

  /**
   * Returns true, if this reference points to an external resource.
   *
   * @return true, if the reference is external, false otherwise.
   */
  public boolean isExternal()
  {
    return external;
  }

  /**
   * Generates the reference fragment, which should be inserted into the HTML-Code.
   * Which content is returned depends on the reference type and the target filesystem.
   *
   * @return the reference code, which should be inserted into the generated HTML-Code.
   */
  public abstract String getReference();
}
