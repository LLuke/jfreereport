/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------
 * PhysicalPage.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PhysicalPage.java,v 1.2 2002/12/04 16:21:33 mungady Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.physicals;

import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.operations.PhysicalOperation;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.ArrayList;

/**
 * Represents a physical page.
 * <p>
 * Here comes the last step of production, the bands are transfered from the Logical page
 * to the physical page.  That page does not know anything specific about bandtypes and how to 
 * handle them, is just able to print all that is fed into the page and that's it.
 *
 * It will not decide when to make a pagebreak, logical page is responsible for that.
 * It is a simple operation container ...
 *
 * @author Thomas Morgner.
 */
public class PhysicalPage
{
  /** Storage for band operations. */
  private ArrayList bandOperations;

  /** The physical page format. */
  private PageFormat pageFormat;
  
  /** The bounds. */
  private Rectangle2D bounds;

  /**
   * Creates a new physical page.
   * <p>
   * PageFormat from OutputTarget, bounds define the position in the global context.
   *
   * @param pf  the page format.
   * @param bounds  the bounds.
   */
  public PhysicalPage(PageFormat pf, Rectangle2D bounds)
  {
    bandOperations = new ArrayList();
    pageFormat = pf;
    this.bounds = bounds;
  }

  /** 
   * Adds an operation to the page.
   *
   * @param op  the operation.
   */
  public void addOperation(PhysicalOperation op)
  {
    bandOperations.add(op);
  }

  /**
   * Writes the page to an output target.
   * 
   * @param ot  the output target.
   *
   */
  public void write(OutputTarget ot) throws OutputTargetException
  {
    ot.beginPage(this);
    PhysicalOperation[] ops = new PhysicalOperation[bandOperations.size()];
    ops = (PhysicalOperation[]) bandOperations.toArray(ops);

    for (int i = 0; i < ops.length; i++)
    {
      ops[i].performOperation(ot);
    }

    ot.endPage();
  }

  public void flush()
  {
    bandOperations.clear();
  }

  /**
   * Return the bounds of this Physical Page. These bounds are defined by the
   * logical page, and the physical page should use these bounds to correct the
   * placement of the given elements.
   *
   * @return the bounds
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  public PageFormat getPageFormat ()
  {
    return pageFormat;
  }

  public boolean isEmpty ()
  {
    return bandOperations.size() == 0;
  }
}
