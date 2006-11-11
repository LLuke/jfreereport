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
 * PageableOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageableOutputProcessor.java,v 1.4 2006/10/22 14:58:25 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.pageable;

import org.jfree.layouting.output.OutputProcessor;

/**
 * A pageable processor generates zero or more pages per event. For the sake
 * of performance, an PageableOutputProcessor should implement some sort of
 * caching so that requesting pages from the same chunk does not result in a
 * full recomputation.
 *
 * For each logical page, a set of one or more physical pages is generated.
 * The pageable output processor allows to query pages by their logical
 * page number and by their physical number.
 *
 * The page content should not be exposed to the caller.
 *
 * @author Thomas Morgner
 */
public interface PageableOutputProcessor extends OutputProcessor
{
  public int getLogicalPageCount();
  public int getPhysicalPageCount();

  public LogicalPageKey getLogicalPage (int page);
  public PhysicalPageKey getPhysicalPage (int page);

  /**
   * Checks, whether the 'processingFinished' event had been received at least
   * once.
   * 
   * @return
   */
  public boolean isPaginationFinished();
}
