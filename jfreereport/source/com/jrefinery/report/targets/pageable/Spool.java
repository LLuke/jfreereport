/**
 * Date: Nov 30, 2002
 * Time: 11:24:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.targets.pageable.operations.PhysicalOperation;

import java.util.ArrayList;

public class Spool implements Cloneable
{
  private ArrayList operations;

  public Spool ()
  {
    this.operations = new ArrayList();
  }

  public PhysicalOperation[] getOperations ()
  {
    PhysicalOperation[] ops = new PhysicalOperation[operations.size()];
    ops = (PhysicalOperation[]) operations.toArray(ops);
    return ops;
  }

  public void addOperation (PhysicalOperation op)
  {
    operations.add (op);
  }

  public void merge(Spool spool)
  {
    operations.addAll(spool.operations);
  }

  public Object clone () throws CloneNotSupportedException
  {
    Spool s = (Spool) super.clone();
    s.operations = (ArrayList) operations.clone();
    return s;
  }
}
