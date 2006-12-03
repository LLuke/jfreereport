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
 * CounterStyleFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CounterStyleFactory.java,v 1.3 2006/07/11 13:29:48 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.counters;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;

public class CounterStyleFactory
{
  private static final CounterStyle DEFAULTCOUNTER = new DecimalCounterStyle();

  private static CounterStyleFactory factory;
  public static final String PREFIX = "org.jfree.layouting.numbering.";

  public static synchronized CounterStyleFactory getInstance ()
  {
    if (factory == null)
    {
      factory = new CounterStyleFactory();
      factory.registerDefaults();
    }
    return factory;
  }

  private HashMap knownCounters;

  private CounterStyleFactory ()
  {
    knownCounters = new HashMap();
  }

  public void registerDefaults ()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final Iterator it = config.findPropertyKeys(PREFIX);
    while (it.hasNext())
    {
      final String key = (String) it.next();
      final String counterClass = config.getConfigProperty(key);
      if (counterClass == null)
      {
        continue;
      }
      final Object o = ObjectUtilities.loadAndInstantiate
          (counterClass, CounterStyleFactory.class, CounterStyle.class);
      if (o instanceof CounterStyle)
      {
        final String name = key.substring(PREFIX.length());
        knownCounters.put (name, o);
      }
    }
  }

  public CounterStyle getCounterStyle (String name)
  {
    CounterStyle cs = (CounterStyle) knownCounters.get(name);
    if (cs != null)
    {
      return cs;
    }
    return DEFAULTCOUNTER;
  }
}
