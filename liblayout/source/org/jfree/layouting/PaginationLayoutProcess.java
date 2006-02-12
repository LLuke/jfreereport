/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * PaginationLayoutProcess.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: PaginationLayoutProcess.java,v 1.1 2006/02/12 21:38:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting;

import org.jfree.layouting.layouter.state.LayoutSavePoint;
import org.jfree.layouting.normalizer.pagable.PaginatingNormalizer;
import org.jfree.layouting.normalizer.Normalizer;

/**
 * Creation-Date: 05.12.2005, 18:59:19
 *
 * @author Thomas Morgner
 */
public interface PaginationLayoutProcess extends LayoutProcess
{
  public LayoutSavePoint getSavePoint(int pos);

  public int getSavePointCount();

  public void addSavePoint(final LayoutSavePoint sp);

  // todo: Handle pages ...
  public Normalizer getNormalizer();

  // todo connect with content layout process ...
}
