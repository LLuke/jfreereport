/**
 * Date: Jan 9, 2003
 * Time: 9:00:14 PM
 *
 * $Id: PageableLayoutStyleKeyFactory.java,v 1.2 2003/01/21 17:11:37 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.io.ext.factory.stylekey.AbstractStyleKeyFactory;

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
