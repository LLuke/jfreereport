package org.jfree.layouting.layouter.counters.other;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class AsterisksCounterStyle implements CounterStyle
{
  public AsterisksCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    StringBuffer b = new StringBuffer(index);
    for (int i = 0; i < index; i++)
    {
      b.append("*");
    }
    return b.toString();
  }
}
