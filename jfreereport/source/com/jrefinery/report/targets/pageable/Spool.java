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
 * ----------
 * Spool.java
 * ----------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Spool.java,v 1.5 2003/01/16 15:35:35 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.targets.base.operations.PhysicalOperation;
import com.jrefinery.report.util.Log;

import java.util.ArrayList;

/** 
 * A spool is a sequence of operations (instances of PhysicalOperation) that can be
 * applied to an OutputTarget.
 *
 * @author Thomas Morgner.
 */
public class Spool implements Cloneable
{
  /** Storage for the operations. */
  private ArrayList operations;

  /**
   * Creates a new spool.
   */
  public Spool ()
  {
    this.operations = new ArrayList();
  }

  public boolean isEmpty ()
  {
    return operations.isEmpty();
  }

  /**
   * Returns an array of operations.
   *
   * @return an array of operations.
   */
  public com.jrefinery.report.targets.base.operations.PhysicalOperation[] getOperations ()
  {
    com.jrefinery.report.targets.base.operations.PhysicalOperation[] ops = new com.jrefinery.report.targets.base.operations.PhysicalOperation[operations.size()];
    ops = (com.jrefinery.report.targets.base.operations.PhysicalOperation[]) operations.toArray(ops);
    return ops;
  }

  /**
   * Adds an operation to the spool.
   *
   * @param op  the operation.
   */
  public void addOperation (com.jrefinery.report.targets.base.operations.PhysicalOperation op)
  {
    operations.add (op);
  }

  /**
   * Appends the operations stored in a spool to the end of this spool's list.
   *
   * @param spool  the spool.
   */
  public void merge(Spool spool)
  {
    operations.addAll(spool.operations);
  }

  /**
   * Clones the spool.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException if cloning is not supported.
   */
  public Object clone () throws CloneNotSupportedException
  {
    Spool s = (Spool) super.clone();
    s.operations = (ArrayList) operations.clone();
    return s;
  }
}
