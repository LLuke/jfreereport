/**
 * Date: Jan 10, 2003
 * Time: 9:07:48 PM
 *
 * $Id: URLObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.io.ext.factory.objects.AbstractObjectDescription;
import com.jrefinery.report.util.Log;

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
    if (isFileStyleProtocol(comp))
    {
      String externalForm = comp.getFile();
      int relativePos = startsWithUntil(baseURL.getFile(), externalForm);
      relativePos = externalForm.lastIndexOf("/", relativePos);
      if (externalForm.length() > (relativePos + 1))
      {
        String externalValue = externalForm.substring(relativePos + 1);
        setParameter("value", externalValue);
        return;
      }
    }

    setParameter("value", comp.toExternalForm());
  }

  private boolean isFileStyleProtocol (URL url)
  {
    if (url.getProtocol().equals("http")) return true;
    if (url.getProtocol().equals("https")) return true;
    if (url.getProtocol().equals("ftp")) return true;
    if (url.getProtocol().equals("file")) return true;
    if (url.getProtocol().equals("jar")) return true;
    if (url.getProtocol().equals("http")) return true;
    return false;
  }

  private int startsWithUntil (String base, String comp)
  {
    int length = Math.min (base.length(), comp.length());
    for (int i = 0; i < length; i++)
    {
      if (base.charAt(i) != comp.charAt(i)) return i;
    }
    return length;
  }

  public static void main (String [] args) throws Exception
  {
    URL base = new URL ("http://localhost/test/file");
    URL test = new URL ("http://localhost/test/pics/noc");

    URLObjectDescription od = new URLObjectDescription(base);
    od.setParameterFromObject(test);
    Log.debug (od.getParameter("value"));
  }
}
