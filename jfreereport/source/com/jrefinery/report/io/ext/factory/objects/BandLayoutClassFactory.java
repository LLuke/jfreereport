/**
 * Date: Jan 10, 2003
 * Time: 9:04:41 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManager;

public class BandLayoutClassFactory extends ClassFactory
{
  public BandLayoutClassFactory()
  {
    registerClass(BandLayoutManager.class, new ClassLoaderObjectDescription());
  }
}
