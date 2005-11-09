package org.jfree.fonts.truetype;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class MaximumProfileTable implements FontTable
{
  private static final long TABLE_ID =
          ('m' << 24 | 'a' << 16 | 'x' << 8 | 'p');

  public MaximumProfileTable()
  {
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
