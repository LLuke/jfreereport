/**
 * Date: Jan 24, 2003
 * Time: 11:00:35 PM
 *
 * $Id: FirstDemoTableModel.java,v 1.1 2003/01/25 02:50:56 taqua Exp $
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.IOUtils;

import javax.swing.ImageIcon;
import javax.swing.table.TableModel;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FirstDemoTableModel extends IconTableModel
{
  public FirstDemoTableModel()
  {
    this(null);
  }

  public FirstDemoTableModel(URL url)
  {
    if (url == null)
    {
      url = this.getClass().getResource("/jlfgr-1_0.jar");
      if (url == null)
      {
        Log.warn ("Unable to find jlfgr-1_0.jar\n" +
                  "Unable to load the icons.\n" +
                  "Please make sure you have the Java Look and Feel Graphics Repository in your classpath.\n" +
                  "You may download this jar-file from http://developer.java.sun.com/developer/techDocs/hi/repository.");
        return;
      }
    }

    try
    {
      Log.debug ("Open URL: " + url);
      InputStream in = new BufferedInputStream(url.openStream());
      readData(in);
      in.close();
    }
    catch (Exception e)
    {
      Log.debug("Failed to load the Icons", e);
    }
  }

  private void readData(InputStream in) //copied from First.java
  {
    try
    {
      ZipInputStream iconJar = new ZipInputStream(in);
      Log.debug ("IconJar: " + iconJar.available());

      ZipEntry ze = iconJar.getNextEntry();
      while (ze != null)
      {
        String fullName = ze.getName();
        Log.debug("Load Entry:" + fullName + " ZE: " + ze.getSize());
        if (fullName.endsWith(".gif"))
        {
          String category = getCategory(fullName);
          String name = getName(fullName);
          Image image = getImage(iconJar);
          Long bytes = new Long(ze.getSize());
          addIconEntry(name, category, image, bytes);
        }
        iconJar.closeEntry();
        ze = iconJar.getNextEntry();
      }
    }
    catch (IOException e)
    {
      Log.debug("Unable to load the ICONS");
    }
  }

  private Image getImage(InputStream in) //copied from First.java
  {
    Image result = null;
    ByteArrayOutputStream byteIn = new ByteArrayOutputStream();
    try
    {
      IOUtils.getInstance().copyStreams(in, byteIn);
      ImageIcon temp = new ImageIcon(byteIn.toByteArray());
      result = temp.getImage();
    }
    catch (IOException e)
    {
      Log.debug("Unable to read the ZIP-Entry", e);
    }
    return result;
  }

  private String getCategory(String fullName) //copied from First.java
  {
    int start = fullName.indexOf("/") + 1;
    int end = fullName.lastIndexOf("/");
    return fullName.substring(start, end);
  }

  private String getName(String fullName) //copied from First.java
  {
    int start = fullName.lastIndexOf("/") + 1;
    int end = fullName.indexOf(".");
    return fullName.substring(start, end);
  }

  public static void main (String [] args)
  {
    TableModel mo = new FirstDemoTableModel();
    Log.debug ("  " + mo.getRowCount());
  }
}
