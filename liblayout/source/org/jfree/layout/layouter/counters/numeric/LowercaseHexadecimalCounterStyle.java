package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class LowercaseHexadecimalCounterStyle implements CounterStyle
{
  public LowercaseHexadecimalCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return Integer.toHexString(index);
  }
}
