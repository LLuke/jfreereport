package org.jfree.report.util;

public class IntList
{
  private int[] data;
  private int size;
  private int increment;

  public IntList (final int capacity)
  {
    data = new int[capacity];
    increment = capacity;
  }

  private void ensureCapacity (final int c)
  {
    if (data.length <= c)
    {
      final int[] newData = new int[Math.max(data.length + increment, c + 1)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  public void add (final int value)
  {
    ensureCapacity(size);
    data[size] = value;
    size += 1;
  }

  public int get (final int index)
  {
    if (index >= size)
    {
      throw new IndexOutOfBoundsException();
    }
    return data[index];
  }

  public void clear ()
  {
    size = 0;
  }

  public int size ()
  {
    return size;
  }

  public int[] toArray ()
  {
    final int[] retval = new int[size];
    System.arraycopy(data, 0, retval, 0, size);
    return retval;
  }
}
