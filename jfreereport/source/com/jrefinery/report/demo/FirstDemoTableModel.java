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
 * -------------------
 * FirstDemoTableModel.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);Stefan Prange
 *
 * $Id: FirstDemoTableModel.java,v 1.3 2003/02/02 23:43:49 taqua Exp $
 *
 * Changes:
 * --------
 * 24-Jan-2003 : Initial version.
 *
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.util.IOUtils;
import com.jrefinery.report.util.Log;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This is the TableModel implementation for the "First"-demo. The model reads
 * the contents of the file "jlfgr-1_0.jar", which must be reachable via the classpath.
 */
public class FirstDemoTableModel extends IconTableModel
{
  /**
   * DefaultConstructor, searches the icons on the classpath.
   */
  public FirstDemoTableModel()
  {
    this(null);
  }

  /**
   * Creates the FirstDemoTableModel and loads its contents from the supplied
   * URL (or searches the default jar file on the classpath if the URL is null).
   *
   * @param url the URL of the "jlfgr" jar file, or null, if the file should
   * be searched on the class path.
   */
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

  /**
   * Reads the data file.
   *
   * @param in the input stream pointing to the JLFGR-Jar file
   */
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

  /**
   * Reads an icon from the given input stream.
   *
   * @param in the Icon input stream.
   * @return the loaded image.
   */
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

  /**
   * Constructs the icon category from the given file name.
   *
   * @param fullName the file name of the icon.
   * @return the category extracted from the file name.
   */
  private String getCategory(String fullName) //copied from First.java
  {
    int start = fullName.indexOf("/") + 1;
    int end = fullName.lastIndexOf("/");
    return fullName.substring(start, end);
  }

  /**
   * Gets the icon name.
   *
   * @param fullName the file name of the icon file.
   * @return the extracted icon name.
   */
  private String getName(String fullName) //copied from First.java
  {
    int start = fullName.lastIndexOf("/") + 1;
    int end = fullName.indexOf(".");
    return fullName.substring(start, end);
  }
}
