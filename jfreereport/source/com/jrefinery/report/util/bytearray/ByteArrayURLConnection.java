/**
 *
 *  Date: 26.06.2002
 *  ByteArrayURLConnection.java
 *  ------------------------------
 *  26.06.2002 : ...
 */
package com.jrefinery.report.util.bytearray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ByteArrayURLConnection extends URLConnection
{
  private byte[] byteArray;

  public ByteArrayURLConnection (URL url, byte[] theArray)
  {
    super (url);
    if (theArray == null) throw new NullPointerException ();
    byteArray = theArray;
  }

  public void connect () throws IOException
  {
    // ignore everything
    connected = true;
  }

  public int getContentLength ()
  {
    return byteArray.length;
  }

  public InputStream getInputStream () throws IOException
  {
    return new ByteArrayInputStream (byteArray);
  }
}