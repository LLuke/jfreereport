package org.jfree.report;

import java.io.Serializable;

public class Anchor implements Serializable
{
  private String name;

  public Anchor (final String target)
  {
    if (target == null)
    {
      throw new NullPointerException();
    }
    this.name = target;
  }

  public String getName ()
  {
    return name;
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof Anchor))
    {
      return false;
    }

    final Anchor anchor = (Anchor) o;

    if (!name.equals(anchor.name))
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    return name.hashCode();
  }
}
