/**
 * Date: Jan 26, 2003
 * Time: 7:00:56 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

public class EmptyContentHtmlReferenceData extends HtmlReferenceData
{
  public EmptyContentHtmlReferenceData()
  {
    super(false);
  }

  public String getReference()
  {
    return "&nbsp;";
  }
}
