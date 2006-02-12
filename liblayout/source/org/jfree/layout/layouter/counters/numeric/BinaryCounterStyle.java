package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class BinaryCounterStyle implements CounterStyle
{
  public BinaryCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return Integer.toBinaryString(index);
  }
}
