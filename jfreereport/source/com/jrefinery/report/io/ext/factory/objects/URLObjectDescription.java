/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: URLObjectDescription.java,v 1.3 2003/01/27 03:17:43 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.IOUtils;

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

  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof URL == false)
    {
      throw new ObjectFactoryException("Is no instance of URL");
    }

    URL comp = (URL) o;
    setParameter("value", IOUtils.getInstance().createRelativeURL(comp, baseURL));
  }

}
