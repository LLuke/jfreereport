/**
 * Date: Jan 12, 2003
 * Time: 8:45:23 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.simple;

public class NameGenerator
{
  public NameGenerator()
  {
  }

  /**
   * the namecounter is used to create unique element names. After a name has been
   * created, the counter is increased by one.
   */
  private int nameCounter;

  /**
   * If a name is supplied, then this method simply returns it.  Otherwise, if name is null, then
   * a unique name is generating by appending a number to the prefix '@anonymous'.
   *
   * @param name The name.
   *
   * @return a non-null name.
   */
  public String generateName (String name)
  {
    if (name == null)
    {
      nameCounter += 1;
      return "@anonymous" + Integer.toHexString (nameCounter);
    }
    return name;
  }

}
