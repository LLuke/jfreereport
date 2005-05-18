/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -----------------------------
 * SwingIconsDemoTableModel.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SwingIconsDemoTableModel.java,v 1.7 2005/03/25 16:37:53 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 * 27-Feb-2003 : Renamed FirstDemoTableModel --> SwingIconsDemoTableModel (DG);
 *
 */

package org.jfree.report.demo;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.ImageIcon;

import org.jfree.io.IOUtils;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A table model implementation for the SwingIconsDemo.java demo application.  The model
 * reads the contents of the file "jlfgr-1_0.jar", which must be reachable via the
 * classpath.
 *
 * @author Thomas Morgner.
 */
public class SwingIconsDemoTableModel extends IconTableModel
{
  /**
   * Creates a new table model.
   */
  public SwingIconsDemoTableModel ()
  {
    this(null);
  }

  /**
   * Creates a new table model.
   *
   * @param url the url for the jlfgr-1_0.jar file (or <code>null</code> to search the
   *            classpath).
   */
  public SwingIconsDemoTableModel (URL url)
  {
    if (url == null)
    {
      url = ObjectUtilities.getResource("/jlfgr-1_0.jar", SwingIconsDemoTableModel.class);
      if (url == null)
      {
        Log.warn("Unable to find jlfgr-1_0.jar inside the classpath.\n"
                + "Unable to load the icons.\n"
                + "Please make sure you have the Java Look and Feel Graphics Repository in "
                + "your classpath.\n"
                + "You may download this jar-file from "
                + "http://developer.java.sun.com/developer/techDocs/hi/repository.");
        return;
      }
    }
    readData(url);
  }

  public boolean readData (final URL url)
  {
    if (url == null)
    {
      throw new NullPointerException("URL given must not be null.");
    }
    clear();
    try
    {
      Log.debug ("Open URL: " + url);
      final InputStream in = new BufferedInputStream(url.openStream());
      final boolean retval = readData(in);
      in.close();
      Log.debug("Loaded: " + getRowCount() + " icons");
      return retval;
    }
    catch (Exception e)
    {
      Log.warn("Failed to load the Icons", e);
      return false;
    }
  }

  /**
   * Reads the icon data from the jar file.
   *
   * @param in the input stream.
   */
  private boolean readData (final InputStream in)
  {
    try
    {
      final ZipInputStream iconJar = new ZipInputStream(in);
      ZipEntry ze = iconJar.getNextEntry();
      while (ze != null)
      {
        final String fullName = ze.getName();
        if (fullName.endsWith(".gif"))
        {
          final String category = getCategory(fullName);
          final String name = getName(fullName);
          final Image image = getImage(iconJar);
          final Long bytes = new Long(ze.getSize());
          //Log.debug ("Add Icon: " + name);
          addIconEntry(name, category, image, bytes);
        }
        iconJar.closeEntry();
        ze = iconJar.getNextEntry();
      }
    }
    catch (IOException e)
    {
      Log.warn("Unable to load the Icons", e);
      return false;
    }
    return true;
  }

  /**
   * Reads an icon from the jar file.
   *
   * @param in the input stream.
   * @return The image.
   */
  private Image getImage (final InputStream in)
  {
    Image result = null;
    final ByteArrayOutputStream byteIn = new ByteArrayOutputStream();
    try
    {
      IOUtils.getInstance().copyStreams(in, byteIn);
      final ImageIcon temp = new ImageIcon(byteIn.toByteArray());
      result = temp.getImage();
    }
    catch (IOException e)
    {
      Log.warn("Unable to read the ZIP-Entry", e);
    }
    return result;
  }

  /**
   * Returns the category.
   *
   * @param fullName the icon file path/name.
   * @return The category extracted from the file name.
   */
  private String getCategory (final String fullName)
  {
    final int start = fullName.indexOf("/") + 1;
    final int end = fullName.lastIndexOf("/");
    return fullName.substring(start, end);
  }

  /**
   * Returns the name.
   *
   * @param fullName the icon file path/name.
   * @return The name extracted from the full name.
   */
  private String getName (final String fullName)
  {
    final int start = fullName.lastIndexOf("/") + 1;
    final int end = fullName.indexOf(".");
    return fullName.substring(start, end);
  }

}
