/**
 * Date: Jan 26, 2003
 * Time: 6:33:52 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

public class InternalCSSReferenceData extends HtmlReferenceData
{
  private String styleData;

  public InternalCSSReferenceData(String data)
  {
    super(false);
    styleData = data;
  }

  public String getReference()
  {
    return styleData;
  }
}
