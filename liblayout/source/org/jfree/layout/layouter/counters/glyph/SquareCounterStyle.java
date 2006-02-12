package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class SquareCounterStyle implements CounterStyle
{
  public SquareCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u25fc";
  }
}
