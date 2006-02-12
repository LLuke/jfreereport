package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class CheckCounterStyle implements CounterStyle
{
  public CheckCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u2713";
  }
}
