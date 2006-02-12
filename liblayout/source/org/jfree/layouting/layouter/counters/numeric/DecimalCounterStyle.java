package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class DecimalCounterStyle implements CounterStyle
{
  public DecimalCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return Integer.toString(index);
  }
}
