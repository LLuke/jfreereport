package org.jfree.layouting.layouter.counters.glyph;

import org.jfree.layouting.layouter.counters.CounterStyle;

public class HyphenCounterStyle implements CounterStyle
{
  public HyphenCounterStyle ()
  {
  }

  public String getCounterValue (int index)
  {
    return "\u2013";
  }
}
