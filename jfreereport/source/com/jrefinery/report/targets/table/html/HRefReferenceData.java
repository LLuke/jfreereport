/**
 * Date: Jan 26, 2003
 * Time: 6:34:32 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

public class HRefReferenceData extends HtmlReferenceData
{
  private String reference;

  public HRefReferenceData(String reference)
  {
    super(true);
    this.reference = reference;
  }

  public String getReference()
  {
    return "href=\"" + reference + "\"";
  }
}
