package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class CircleCounterStyle implements CounterStyle
{
  public CircleCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u25e6";
  }
}
