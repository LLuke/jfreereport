/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * ReadOnlyListIterator.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.util;

import java.util.ListIterator;

/**
 * A readOnly list iterator that will ignore all operations which could modify its contents.
 */
public class ReadOnlyListIterator implements ListIterator
{
  private ListIterator it;

  public ReadOnlyListIterator (ListIterator it)
  {
    this.it = it;
  }

  public void add (Object o)
  {
  }

  public boolean hasNext ()
  {
    return it.hasNext ();
  }

  public Object next ()
  {
    return it.next ();
  }

  public boolean hasPrevious ()
  {
    return it.hasPrevious ();
  }

  public Object previous ()
  {
    return it.previous ();
  }

  public int nextIndex ()
  {
    return it.nextIndex ();
  }

  public int previousIndex ()
  {
    return it.previousIndex ();
  }

  public void remove ()
  {
  }

  public void set (Object o)
  {
  }
}

