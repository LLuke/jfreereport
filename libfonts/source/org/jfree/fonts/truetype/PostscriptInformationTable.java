package org.jfree.fonts.truetype;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class PostscriptInformationTable implements FontTable
{
  private static final long TABLE_ID =
          ('p' << 24 | 'o' << 16 | 's' << 8 | 't');

  public PostscriptInformationTable()
  {
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
