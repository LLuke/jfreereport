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
 * PendingContentStorage.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PendingContentStorage.java,v 1.1 2006/07/11 13:51:01 taqua Exp $
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
 * This class holds pending content. Pending content is all content that has
 * not been printed yet. Aside from the normal flow, there are other pending
 * storages created for each new value of the 'move-to' or 'move-to-replace'
 * content targets.
 * <p/>
 * Pending content on position:fixed elements remains sticky; on new pages this
 * content will be printed again. (This implies, that fixed-content cannot span
 * more than one page).
 *
 * @author Thomas Morgner
 */
public interface PendingContentStorage
{
  public void startedBlock(final LayoutContext context);

  public void startedInline(final LayoutContext context);

  public void addContent(final LayoutContext context, final ContentToken content);

  public void finishedBlock();

  public void finishedInline();
  
}
