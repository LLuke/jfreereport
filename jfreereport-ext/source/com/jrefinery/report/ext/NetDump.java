/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------
 * NetDump.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NetDump.java,v 1.3 2003/06/10 18:17:24 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.ext;

import com.jrefinery.report.util.Log;

import java.net.URL;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * The NetDump utility can be used to trace simple HTTP-Calls.
 *
 * @author Thomas Morgner
 */
public class NetDump
{
  /**
   * THe connection info is used to extract all necessary information from
   * the given URL.
   */
  private static class ConnectionInfo
  {
    /** the name of the host, to which to connect. */
    private String host;
    /** the port on the target host. */
    private int port;
    /** the URI which should be queried. */
    private String uri;

    /**
     * Creates a new ConnectionInfo object for the given URL.
     *
     * @param url the URL to which to connect to.
     */
    public ConnectionInfo(final URL url)
    {
      host = url.getHost();
      port = url.getPort();
      if (port == -1)
      {
        port = 80;
      }

      final String file = url.getFile();
//      String query = url.getQuery();
      final String ref = url.getRef();

      uri = file;
/*      if (query != null)
      {
        uri += "?";
        uri += query;
      }*/
      if (ref != null)
      {
        uri += "#";
        uri += ref;
      }
    }

    /**
     * Gets the host to which to connect.
     *
     * @return the target host.
     */
    public String getHost()
    {
      return host;
    }

    /**
     * Gets the server port on the host, to which to connect.
     *
     * @return the port on the server.
     */
    public int getPort()
    {
      return port;
    }

    /**
     * Gets the URI, that should be queried.
     *
     * @return the target URI.
     */
    public String getUri()
    {
      return uri;
    }
  }

  /**
   * Connects to the given URL using the specified HTTP method, something like
   * GET or POST.
   *
   * @param args the connection arguments, the method followed by an url.
   */
  public static void main (final String [] args)
  {
    if (args.length != 2)
    {
      Log.error ("Need an Method + URL as parameter");
      System.exit(1);
    }

    try
    {
      final String method = args[0];
      final URL url = new URL (args[1]);
      if (url.getProtocol().equals("http") == false)
      {
        Log.error ("The given url must be a HTTP url");
        System.exit(1);
      }

      final ConnectionInfo ci = new ConnectionInfo(url);
      Log.debug ("Connecting to: " + ci.getHost() + ":" + ci.getPort());
      final Socket socket = new Socket(ci.getHost(), ci.getPort());
      final OutputStream out = socket.getOutputStream();
      final StringBuffer b = new StringBuffer();
      b.append(method.toUpperCase());
      b.append(" ");
      b.append(ci.getUri());
      b.append(" HTTP/1.0\n");
      b.append("\n");
      Log.debug (b.toString());
      out.write(b.toString().getBytes());

      final InputStream in = socket.getInputStream();
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line = reader.readLine();
      while (line != null)
      {
        System.out.println (line);
        line = reader.readLine();
      }
      in.close();
    }
    catch (Exception e)
    {
      Log.error("Failed to perform request: ", e);
      System.exit(1);
    }
    System.exit(0);
  }
}
