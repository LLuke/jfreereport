/**
 * Date: Jan 29, 2003
 * Time: 5:08:11 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

public class TextPageType
{
  public static final TextPageType PLAIN = new TextPageType("PLAIN");
  public static final TextPageType EPSON = new TextPageType("EPSON");
  public static final TextPageType IBM = new TextPageType("IBM");

  private final String myName; // for debug only

  private TextPageType(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }
}
