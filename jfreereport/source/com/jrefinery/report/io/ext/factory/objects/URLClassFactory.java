/**
 * Date: Jan 11, 2003
 * Time: 3:04:11 PM
 *
 * $Id: URLClassFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.net.URL;

public class URLClassFactory extends ClassFactoryImpl
{
  public URLClassFactory(URL baseURL)
  {
    registerClass(URL.class, new URLObjectDescription(baseURL));
  }
}
