/**
 * Date: Jan 29, 2003
 * Time: 3:34:38 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.output;

import java.io.OutputStream;

public class PlainTextPageFactory
{
  private TextPageType pageType;
  private OutputStream out;

  public PlainTextPageFactory(OutputStream out, TextPageType pageType)
  {
    this.out = out;
    this.pageType = pageType;
  }

  public PlainTextPage createPage (int w, int h)
  {
    if (pageType == TextPageType.PLAIN)
    {
      return new PlainTextPage(w, h);
    }
    else if (pageType == TextPageType.IBM)
    {
      return new IBMPrinterTextPage(w,h);
    }
    else
    {
      return new EpsonLQTextPage(w,h);
    }

  }

  public OutputStream getOutputStream()
  {
    return out;
  }
}
