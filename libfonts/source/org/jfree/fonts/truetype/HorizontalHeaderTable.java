package org.jfree.fonts.truetype;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class HorizontalHeaderTable implements FontTable
{
  private static final long TABLE_ID =
          ('h' << 24 | 'h' << 16 | 'e' << 8 | 'a');

  public HorizontalHeaderTable()
  {
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
