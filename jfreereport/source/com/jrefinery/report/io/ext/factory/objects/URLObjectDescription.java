/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;

import java.net.URL;

public class URLObjectDescription extends AbstractObjectDescription
{
  private URL baseURL;

  public URLObjectDescription(URL baseUrl)
  {
    super(URL.class);
    this.baseURL = baseUrl;
    setParameterDefinition("value", String.class);
  }

  public Object createObject()
  {
    String o = (String) getParameter("value");
    try
    {
      return new URL(baseURL, o);
    }
    catch (Exception e)
    {
      return null;
    }
  }
}
