/**
 *
 *  Date: 26.06.2002
 *  Handler.java
 *  ------------------------------
 *  26.06.2002 : ...
 */
package com.jrefinery.report.util.bytearray;

import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.net.URL;
import java.io.IOException;

public class Handler extends URLStreamHandler
{
  protected URLConnection openConnection (URL url) throws IOException
  {
    byte[] data = ByteArrayRegistry.getInstance().get (url.getFile());
    if (data == null) throw new IOException("No such data registered");
    return new ByteArrayURLConnection(url, data);
  }

  protected void parseURL (URL url, String s, int start, int limit)
  {
    String protocol = url.getProtocol();
    String file = s.substring(start + 2, limit);
    setURL(url, protocol, "", 0, null, null,file, null, null);
  }

  protected String toExternalForm (URL url)
  {
    StringBuffer b = new StringBuffer ();
    b.append (url.getProtocol());
    b.append ("://");
    b.append (url.getFile());
    return b.toString();
  }
}