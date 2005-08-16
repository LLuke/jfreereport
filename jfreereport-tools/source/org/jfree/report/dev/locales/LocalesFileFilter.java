package org.jfree.report.dev.locales;

import java.io.FilenameFilter;
import java.io.File;

public class LocalesFileFilter implements FilenameFilter
{
  private String prefix;

  public LocalesFileFilter (final String prefix)
  {
    this.prefix = prefix;
  }

  /**
   * Tests if a specified file should be included in a file list.
   *
   * @param dir  the directory in which the file was found.
   * @param name the name of the file.
   * @return <code>true</code> if and only if the name should be included in the file
   *         list; <code>false</code> otherwise.
   */
  public boolean accept (final File dir, final String name)
  {
    if (name.startsWith(prefix) == false)
    {
      return false;
    }
    if (name.endsWith(".properties") == false)
    {
      return false;
    }
    return true;
  }
}
