/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ----------------------------------
 * PhysicalPage.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
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

public class PhysicalPage
{
  // here comes the last step of production, the bands are transfered from the Logical page
  // to the physical page.
  // that page does not know anything specific about bandtypes and how to handle them, is just
  // able to print all that is fed into the page and that's it.

  // will not decide when to make a pagebreak, logical page is responsible for that.
  // is a simple operation container ...
  //
  private ArrayList bandOperations;

  private PageFormat pageFormat;
  private Rectangle2D bounds;

  /**
   * PageFormat from OT, bounds define the position in the global context
   */
  public PhysicalPage(PageFormat pf, Rectangle2D bounds)
  {
    bandOperations = new ArrayList();
    pageFormat = pf;
    this.bounds = bounds;
  }

  public void addOperation(PhysicalOperation op)
  {
    bandOperations.add(op);
  }

  public void write(OutputTarget ot) throws OutputTargetException
  {
//    Log.debug ("------------------------------- BEGIN ----------------------------");
//    long time = System.currentTimeMillis();
//    Log.debug ("Time: " + time);

    ot.beginPage(this);
    PhysicalOperation[] ops = new PhysicalOperation[bandOperations.size()];
    ops = (PhysicalOperation[]) bandOperations.toArray(ops);

    for (int i = 0; i < ops.length; i++)
    {
      ops[i].performOperation(ot);
    }

    ot.endPage();
//    Log.debug ("------------------------------- END ----------------------------");
//    Log.debug ("Time: " + (System.currentTimeMillis() - time));
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
