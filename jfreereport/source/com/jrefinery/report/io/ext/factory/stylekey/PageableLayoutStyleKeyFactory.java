/**
 * Date: Jan 9, 2003
 * Time: 9:00:14 PM
 *
 * $Id: PageableLayoutStyleKeyFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.pageable.bandlayout.StaticLayoutManager;
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
