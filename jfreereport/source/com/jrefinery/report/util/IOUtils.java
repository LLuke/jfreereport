/**
 * Date: Jan 26, 2003
 * Time: 6:19:12 PM
 *
 * $Id: IOUtils.java,v 1.1 2003/01/27 03:20:01 taqua Exp $
 */
package com.jrefinery.report.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.Writer;

public class IOUtils
{
  private static IOUtils instance;

  public static IOUtils getInstance()
  {
    if (instance == null)
    {
      instance = new IOUtils();
    }
    return instance;
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

  private List parseName (String name)
  {
    ArrayList list = new ArrayList();
    StringTokenizer strTok = new StringTokenizer(name, "/");
    while (strTok.hasMoreElements())
    {
      String s = (String) strTok.nextElement();
      if (s.length() != 0)
        list.add(s);
    }
    return list;
  }

  private String formatName (List name)
  {
    StringBuffer b = new StringBuffer();
    Iterator it = name.iterator();
    while (it.hasNext())
    {
      b.append(it.next());
      if (it.hasNext())
      {
        b.append("/");
      }
    }
    return b.toString();
  }

  private int startsWithUntil (List baseName, List urlName)
  {
    int minIdx = Math.min (urlName.size(), baseName.size());
    for (int i = 0; i < minIdx; i++)
    {
      String baseToken = (String) baseName.get(i);
      String urlToken = (String) urlName.get(i);
      if (baseToken.equals(urlToken) == false)
      {
        return i;
      }
    }
    return minIdx;
  }

  public static void main (String [] args) throws Exception
  {

    URL base = new File ("C:/").toURL();
    URL test = new File ("C:/localhost/test/piacs/noc").toURL();

    Log.debug (getInstance().createRelativeURL(test, base));
  }

  private boolean isSameService (URL url, URL baseUrl)
  {
    if (url.getProtocol().equals(baseUrl.getProtocol()) == false)
      return false;
    if (url.getHost().equals(baseUrl.getHost()) == false)
      return false;
    if (url.getPort() != baseUrl.getPort())
      return false;

    return true;
  }

  public String createRelativeURL (URL url, URL baseURL)
  {
    if (isFileStyleProtocol(url) && isSameService(url, baseURL))
    {
      ArrayList retval = new ArrayList();

      List urlName = parseName(url.getFile());
      List baseName = parseName(baseURL.getFile());
      int commonIndex = startsWithUntil(urlName, baseName);
      Log.debug ("CommonIndex: " + commonIndex);
      if (commonIndex == 0)
      {
        return url.toExternalForm();
      }

      if (commonIndex == urlName.size())
      {
        // correct the common index if there is some weird mapping detected,
        // fi. the file url is fully included in the base url:
        //
        // base: /file/test/funnybase
        // file: /file/test
        //
        // this could be a valid configuration whereever virtual mappings are
        // allowed.
        commonIndex -= 1;
      }

      if (baseName.size() >= urlName.size())
      {
        int levels = baseName.size() - commonIndex;
        for (int i = 0; i < levels; i++)
        {
          retval.add("..");
        }
      }

      Log.debug ("BaseName= " + baseName);
      Log.debug ("URLName= " + urlName);
      Log.debug ("CommonIndex = " + commonIndex);
      retval.addAll (urlName.subList(commonIndex, urlName.size()));
      return formatName(retval);
    }
    return url.toExternalForm();
  }

  public void copyStreams (InputStream in, OutputStream out)
    throws IOException
  {
    copyStreams(in, out, 4096);
  }

  public void copyStreams (InputStream in, OutputStream out, int buffersize)
    throws IOException
  {
    // create a 4kbyte buffer to read the file
    byte[] bytes = new byte[buffersize];

    // the input stream does not supply accurate available() data
    // the zip entry does not know the size of the data
    while (in.available() != 0)
    {
      int bytesRead = in.read(bytes);
      if (bytesRead > -1)
      {
        out.write(bytes, 0, bytesRead);
      }
      else
      {
        // no more data...
        break;
      }
    }
  }

  public void copyWriter (Reader in, Writer out)
    throws IOException
  {
    copyWriter(in, out, 4096);
  }

  public void copyWriter (Reader in, Writer out, int buffersize)
    throws IOException
  {
    // create a 4kbyte buffer to read the file
    char[] bytes = new char[buffersize];

    // the input stream does not supply accurate available() data
    // the zip entry does not know the size of the data
    int bytesRead = in.read(bytes);
    while (bytesRead > -1)
    {
      out.write(bytes, 0, bytesRead);
      bytesRead = in.read(bytes);
    }
  }

  public String getFileName (URL url)
  {
    String file = url.getFile();
    int last = file.lastIndexOf("/");
    if (last < 0)
      return file;

    return file.substring(last);
  }
}
