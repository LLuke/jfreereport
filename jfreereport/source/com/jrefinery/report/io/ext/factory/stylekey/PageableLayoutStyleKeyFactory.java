/**
 * Date: Jan 9, 2003
 * Time: 9:00:14 PM
 *
 * $Id: PageableLayoutStyleKeyFactory.java,v 1.3 2003/01/29 03:13:00 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;

public class PageableLayoutStyleKeyFactory extends AbstractStyleKeyFactory
{
  public PageableLayoutStyleKeyFactory()
  {
    // separate factory ... later ...
    addKey(BandLayoutManager.LAYOUTMANAGER);
    addKey(StaticLayoutManager.ABSOLUTE_POS);
    addKey(StaticLayoutManager.DYNAMIC_HEIGHT);
  }
}
