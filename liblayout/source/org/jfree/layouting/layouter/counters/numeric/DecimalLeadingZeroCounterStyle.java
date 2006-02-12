package org.jfree.layouting.layouter.counters.numeric;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class DecimalLeadingZeroCounterStyle implements CounterStyle
{
  public DecimalLeadingZeroCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    if (Math.abs(index) < 10)
    {
      if (index < 0)
      {
        return "-0" + Integer.toString(-index);
      }
      else
      {
        return "0" + Integer.toString(index);
      }
    }
    return Integer.toString(index);
  }
}
