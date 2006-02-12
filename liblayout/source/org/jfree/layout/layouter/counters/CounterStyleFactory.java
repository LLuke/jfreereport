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
    }
    return factory;
  }

  private HashMap knownCounters;

  public CounterStyleFactory ()
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
      final Object o =
              ObjectUtilities.loadAndInstantiate(counterClass, CounterStyleFactory.class);
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
