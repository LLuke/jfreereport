/**
 * Date: Jan 26, 2003
 * Time: 5:47:15 PM
 *
 * $Id: HtmlReferenceData.java,v 1.1 2003/01/27 03:20:01 taqua Exp $
 */
package com.jrefinery.report.targets.table.rtf;

public abstract class RTFReferenceData
{
  private boolean external;

  /**
   * A reference: a fragment which could be inserted into
   * @param external
   */
  protected RTFReferenceData(boolean external)
  {
    this.external = external;
  }

  public boolean isExternal()
  {
    return external;
  }

  public abstract String getReference();
}
