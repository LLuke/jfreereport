package org.jfree.layouting.input.style;

import java.io.Serializable;

/**
 * Creation-Date: 26.10.2005, 14:05:23
 *
 * @author Thomas Morgner
 */
public final class StyleKey implements Serializable, Cloneable
{
  private int index;

  /**
   * The name of the style key.
   */
  private String name;

  /**
   * Whether this stylekey is transient. Transient keys denote temporary values
   * stored in the stylesheet. Such keys should never be written into long
   * term persistent states.
   */
  private boolean trans;

  private boolean inherited;
  private boolean listOfValues;

  /**
   * This constructor is intentionally 'package protected'.
   */
  /**
   * Creates a new style key.
   *
   * @param name      the name (never null).
   */
  protected StyleKey(final String name,
           final boolean trans,
           final boolean inherited,
           final boolean listOfValues,
           final int index)
  {
    if (name == null)
    {
      throw new NullPointerException("StyleKey.setName(...): null not permitted.");
    }

    this.name = name;
    this.trans = trans;
    this.inherited = inherited;
    this.listOfValues = listOfValues;
    this.index = index;
  }

  /**
   * Returns the name of the key.
   *
   * @return the name.
   */
  public String getName ()
  {
    return name;
  }

  public int getIndex()
  {
    return index;
  }

  public boolean isListOfValues()
  {
    return listOfValues;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof StyleKey))
    {
      return false;
    }

    final StyleKey key = (StyleKey) o;

    if (name.equals(key.name) == false)
    {
      return false;
    }
    return true;
  }

  public boolean isInherited()
  {
    return inherited;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of
   * hashtables such as those provided by <code>java.util.Hashtable</code>.
   * <p/>
   *
   * @return a hash code value for this object.
   */
  public int hashCode ()
  {
    return index;
  }

  /**
   * Checks, whether this stylekey denotes a temporary computation result.
   *
   * @return true, if the key is transient, false otherwise.
   */
  public boolean isTransient ()
  {
    return trans;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
  public String toString()
  {
    return "StyleKey{" +
            "name='" + name + "'" +
            ", trans=" + trans +
            ", inherited=" + inherited +
            ", listOfValues=" + listOfValues +
            "}";
  }
  public Object clone ()
          throws CloneNotSupportedException
  {
    return super.clone();
  }
}
