/**
 * Date: Jan 26, 2003
 * Time: 6:33:59 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

public class ImageReferenceData extends HtmlReferenceData
{
  private String reference;

  public ImageReferenceData(String reference)
  {
    super(true);
    this.reference = reference;
  }

  public String getReference()
  {
    return reference;
  }
}
