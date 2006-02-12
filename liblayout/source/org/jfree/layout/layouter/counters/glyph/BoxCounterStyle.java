package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class BoxCounterStyle implements CounterStyle
{
  public BoxCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u25a1";
  }
}
