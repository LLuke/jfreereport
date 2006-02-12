package org.jfree.layouting.model.lists;

import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.input.style.keys.list.ListStylePosition;
import org.jfree.ui.Drawable;

public class ListSpecification
{
  private CounterStyle counterStyle;
  private ListStylePosition position;
  private Drawable image;

  public ListSpecification ()
  {
  }

  public CounterStyle getCounterStyle ()
  {
    return counterStyle;
  }

  public void setCounterStyle (CounterStyle counterStyle)
  {
    this.counterStyle = counterStyle;
  }

  public Drawable getImage ()
  {
    return image;
  }

  public void setImage (Drawable image)
  {
    this.image = image;
  }

  public ListStylePosition getPosition ()
  {
    return position;
  }

  public void setPosition (ListStylePosition position)
  {
    this.position = position;
  }
}
