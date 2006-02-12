package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class UppercaseHexadecimalCounterStyle implements CounterStyle
{
  public UppercaseHexadecimalCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return Integer.toHexString(index).toUpperCase();
  }
}
