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
 * ---------
 * AnchorElement.java
 * ---------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AnchorElement.java,v 1.2 2005/02/23 19:31:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-02-01 : Initial version
 */
package org.jfree.report;

/**
 * The anchor element creates targets for hyperlinks.
 *
 * @author Thomas Morgner
 */
public class AnchorElement extends Element
{
  /**
   * The content type.
   */
  public static final String CONTENT_TYPE = "X-Anchor";

  /**
   * Creates a new anchor element.
   */
  public AnchorElement ()
  {
  }

  /**
   * Defines the content-type for this element. The content-type is used as a hint how to
   * process the contents of this element. An element implementation should restrict
   * itself to the content-type set here, or the reportprocessing may fail or the element
   * may not be printed.
   * <p/>
   * An element is not allowed to change its content-type after ther report processing has
   * started.
   * <p/>
   * If an content-type is unknown to the output-target, the processor should ignore the
   * content or clearly document its internal reprocessing. Ignoring is preferred.
   *
   * @return the content-type as string.
   */
  public String getContentType ()
  {
    return CONTENT_TYPE;
  }
}
