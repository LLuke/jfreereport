/**
 * Date: Nov 20, 2002
 * Time: 4:36:03 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.contents;

public class ContentType
{
  public static final ContentType Text = new ContentType("Text");
  public static final ContentType Shape = new ContentType("Shape");
  public static final ContentType Image = new ContentType("Image");
  public static final ContentType Container = new ContentType("Container");

  private final String myName; // for debug only

  private ContentType(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }
}
