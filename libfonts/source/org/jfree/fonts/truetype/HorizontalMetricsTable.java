package org.jfree.fonts.truetype;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class HorizontalMetricsTable implements FontTable
{
  private static final long TABLE_ID =
          ('h' << 24 | 'm' << 16 | 't' << 8 | 'x');

  public HorizontalMetricsTable()
  {
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
