/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultPendingContentStorage.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;

/**
 * The default implementation builds a complete model of the generated content
 * (which can be very large!) of all page areas and contains the expected
 * behaviour for fixed and page area content.
 *
 * On fixed- and page areas, the content does not get commited; it will be
 * marked as processed but not removed from the storage. On pagebreaks, the
 * processed flag can be reset. A manual commit is possible.
 *
 * A pending content storage always represents a single normal flow. Absolute
 * or floating elements create their own normal flow.
 *
 * Pending content is not layoutable until it has been inserted into page.
 *
 * @author Thomas Morgner
 */
public class DefaultPendingContentStorage implements PendingContentStorage
{
  private boolean performAutoCommit;

  public DefaultPendingContentStorage()
  {
  }

  public void startedBlock(final LayoutContext context)
  {

  }

  public void startedInline(final LayoutContext context)
  {

  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
  {

  }

  public void finishedBlock()
  {

  }

  public void finishedInline()
  {

  }
}
