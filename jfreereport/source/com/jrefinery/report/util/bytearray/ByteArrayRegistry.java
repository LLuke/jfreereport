/**
 *
 *  Date: 26.06.2002
 *  ByteArrayRegistry.java
 *  ------------------------------
 *  26.06.2002 : ...
 */
package com.jrefinery.report.util.bytearray;

import java.util.Hashtable;
import java.util.Enumeration;
import java.net.URL;
import java.net.URLConnection;

public class ByteArrayRegistry
{
  private static ByteArrayRegistry singleton;

  private Hashtable dataStore;

  public ByteArrayRegistry ()
  {
    this.dataStore = new Hashtable();
  }

  public byte[] get (String name)
  {
    return (byte[])dataStore.get(name);
  }

  public void put (String name, byte[] data)
  {
    dataStore.put (name, data);
  }

  public void remove (String name)
  {
    dataStore.remove(name);
  }

  public void clear ()
  {
    dataStore.clear();
  }

  public Enumeration getRegisteredNames ()
  {
    return dataStore.keys ();
  }

  public static ByteArrayRegistry getInstance ()
  {
    if (singleton == null)
    {
      singleton = new ByteArrayRegistry();
    }
    return singleton;
  }

  public static void registerProtocol ()
  {
    String protoHandler = System.getProperty("java.protocol.handler.pkgs", "");
    if (protoHandler.indexOf("com.jrefinery.report.util") != -1) return;
    System.setProperty("java.protocol.handler.pkgs", protoHandler + "|com.jrefinery.report.util");
  }

  public static void main (String [] main) throws Exception
  {
    registerProtocol();
    getInstance().put("test.png", new byte[11]);
    URL url = new URL ("bytearray://test.png");
    URLConnection c = url.openConnection();
    System.out.println (c.getContentLength());
  }
}