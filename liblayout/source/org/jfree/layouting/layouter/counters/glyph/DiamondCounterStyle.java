package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class DiamondCounterStyle implements CounterStyle
{
  public DiamondCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u25c6";
  }
}
