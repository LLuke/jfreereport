/**
 * Date: Jan 10, 2003
 * Time: 9:04:41 PM
 *
 * $Id: BandLayoutClassFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManager;

public class BandLayoutClassFactory extends ClassFactoryImpl
{
  public BandLayoutClassFactory()
  {
    registerClass(BandLayoutManager.class, new ClassLoaderObjectDescription());
  }
}
