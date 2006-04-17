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
 * DefaultElementContext.java
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

package org.jfree.layouting.model;

import java.util.HashMap;

public class DefaultElementContext implements ElementContext
{
  private static final String TOTAL_PAGES_COUNTER = "total-pages";

  private ElementContext parent;
  private HashMap counters;

  public DefaultElementContext ()
  {
  }

  public ElementContext getParent ()
  {
    return parent;
  }

  public void setParent (ElementContext parent)
  {
    this.parent = parent;
  }

  /**
   * Returns the value for the given counter. If no counter exists under that name, this
   * method returns 0.
   *
   * @param counterName
   * @return the value for the given counter.
   */
  public int getCounterValue (String counterName)
  {
    if (counters != null)
    {
      final Integer counterValue = (Integer) counters.get(counterName);
      if (counterValue != null)
      {
        return counterValue.intValue();
      }
    }
    if (parent != null)
    {
      return parent.getCounterValue(counterName);
    }
    return 0;
  }

  /**
   * Increments the counter with the given name. If no counter is known under that name,
   * the root node will create one.
   *
   * @param name
   * @param value
   */
  public void incrementCounter (String name, int value)
  {
    if (TOTAL_PAGES_COUNTER.equals(name))
    {
      return;
    }

    int oldValue = getCounterValue(name);
    if (counters == null)
    {
      counters = new HashMap();
    }
    counters.put(name, new Integer(oldValue + value));
  }

  /**
   * Reseting an counter creates a new Counter-Instance. Counters from parent elements are
   * not affected and remain unchanged. All further operations issued by all sub-elements
   * will now work with this counter.
   *
   * @param name
   * @param value
   */
  public void resetCounter (String name, int value)
  {
    if (TOTAL_PAGES_COUNTER.equals(name))
    {
      return;
    }

    if (counters == null)
    {
      counters = new HashMap();
    }
    counters.put(name, new Integer(value));
  }

  public boolean isCounterDefined (String counterName)
  {
    if (parent == null)
    {
      return true;
    }

    return counters.containsKey(counterName);

  }
}
