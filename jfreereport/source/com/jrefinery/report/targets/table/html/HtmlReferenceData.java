/**
 * Date: Jan 26, 2003
 * Time: 5:47:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

public abstract class HtmlReferenceData
{
  private boolean external;

  /**
   * A reference: a fragment which could be inserted into
   * @param external
   */
  protected HtmlReferenceData(boolean external)
  {
    this.external = external;
  }

  public boolean isExternal()
  {
    return external;
  }

  public abstract String getReference();
}
