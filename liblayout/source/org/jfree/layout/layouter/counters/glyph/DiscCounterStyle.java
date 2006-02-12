package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class DiscCounterStyle implements CounterStyle
{
  public DiscCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u2022";
  }
}
