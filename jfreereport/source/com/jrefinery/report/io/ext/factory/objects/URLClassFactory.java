/**
 * Date: Jan 11, 2003
 * Time: 3:04:11 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.ClassFactory;

import java.net.URL;

public class URLClassFactory extends ClassFactory
{
  public URLClassFactory(URL baseURL)
  {
    registerClass(URL.class, new URLObjectDescription(baseURL));
  }
}
