package org.jfree.layouting.layouter.counters.numeric;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;

import org.jfree.layouting.layouter.counters.CounterStyle;

public abstract class NumericCounterStyle implements CounterStyle
{
  private static final class ReplacementDefinition
  {
    private char original;
    private char replacement;

    public ReplacementDefinition (char original, char replacement)
    {
      this.original = original;
      this.replacement = replacement;
    }

    public char getOriginal ()
    {
      return original;
    }

    public char getReplacement ()
    {
      return replacement;
    }

    public boolean equals (Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final ReplacementDefinition that = (ReplacementDefinition) o;

      if (original != that.original)
      {
        return false;
      }

      return true;
    }

    public int hashCode ()
    {
      return (int) original;
    }
  }

  private HashSet replacements;
  private int base;
  private transient ReplacementDefinition[] cachedDefinitions;
  private String suffix;

  protected NumericCounterStyle (int base, String suffix)
  {
    this.base = base;
    this.suffix = suffix;
    this.replacements = new HashSet();
  }

  public void setReplacementChar (char org, char other)
  {
    this.replacements.add (new ReplacementDefinition(org, other));
    this.cachedDefinitions = null;
  }

  public String getCounterValue (int index)
  {
    if (cachedDefinitions == null)
    {
      cachedDefinitions = (ReplacementDefinition[])
            replacements.toArray(new ReplacementDefinition[replacements.size()]);
    }

    String numeric = Integer.toString(index, base);

    for (int i = 0; i < cachedDefinitions.length; i++)
    {
      ReplacementDefinition def = cachedDefinitions[i];
      numeric = numeric.replace(def.getOriginal(), def.getReplacement());
    }
    return numeric + suffix;
  }
}
