package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class OctalCounterStyle implements CounterStyle
{
  public OctalCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return Integer.toOctalString(index);
  }
}
