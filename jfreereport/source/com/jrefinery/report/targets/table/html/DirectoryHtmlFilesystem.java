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
 * DirectoryHtmlFilesystem.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DirectoryHtmlFilesystem.java,v 1.5 2003/02/02 23:43:52 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.IOUtils;
import com.jrefinery.report.util.ImageComparator;
import com.jrefinery.report.util.StringUtil;
import com.keypoint.PngEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;

public class DirectoryHtmlFilesystem implements HtmlFilesystem
{
  private class CounterRef
  {
    public int count;
  }

  private File rootFile;
  private File dataDirectory;
  private FileOutputStream rootStream;
  private Hashtable usedNames;
  private Hashtable usedURLs;
  private Hashtable encodedImages;
  private ImageComparator comparator;
  private boolean copyExternalImages;

  public DirectoryHtmlFilesystem (File file)
    throws IOException
  {
    this (file, file.getParentFile());
  }

  public DirectoryHtmlFilesystem (File file, File dataDirectory)
    throws IOException
  {
    this.usedNames = new Hashtable();
    this.usedURLs = new Hashtable();
    this.encodedImages = new Hashtable();
    this.comparator = new ImageComparator();

    if (file.exists() && file.isFile() == false)
    {
      throw new IOException ("The given file-parameter does not point to a data file");
    }
    else
    {
      this.rootFile = file;
    }
    if (dataDirectory == null)
    {
      this.dataDirectory = file.getParentFile();
    }
    else
    {
      this.dataDirectory = dataDirectory;
    }

  }

  public void setCopyExternalImages(boolean copyExternalImages)
  {
    this.copyExternalImages = copyExternalImages;
  }

  // contains the HTML file
  public OutputStream getRootStream()
      throws IOException
  {
    if (rootStream == null)
    {
      rootStream = new FileOutputStream(rootFile);
    }
    return rootStream;
  }

  protected boolean isSupportedImageFormat (URL url)
  {
    String file = url.getFile();
    if (StringUtil.endsWithIgnoreCase(file, ".jpg"))
    {
      return true;
    }
    if (StringUtil.endsWithIgnoreCase(file, ".jpeg"))
    {
      return true;
    }
    if (StringUtil.endsWithIgnoreCase(file, ".png"))
    {
      return true;
    }
    if (StringUtil.endsWithIgnoreCase(file, ".gif"))
    {
      return true;
    }
    return false;
  }

  public HtmlReferenceData createImageReference(ImageReference reference)
    throws IOException
  {
    if (reference.getSourceURL() == null)
    {
      PngEncoder encoder = new PngEncoder (reference.getImage(), true, 0, 9);
      byte[] data = encoder.pngEncode();

      Object object = comparator.createCompareData(data);
      String name = (String) encodedImages.get(object);
      if (name == null)
      {
        // encode the picture ...
        File dataFile = new File (dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        OutputStream in = new BufferedOutputStream (new FileOutputStream(dataFile));
        in.write(data);
        in.flush();
        in.close();
        name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), dataDirectory.toURL());
        encodedImages.put (object, name);
      }
      return new ImageReferenceData(name);
    }
    else if (isSupportedImageFormat(reference.getSourceURL()) == false)
    {
      URL url = reference.getSourceURL();
      String name = (String) usedURLs.get(url);
      if (name == null)
      {
        PngEncoder encoder = new PngEncoder (reference.getImage(), true, 0, 9);
        byte[] data = encoder.pngEncode();

        // encode the picture ...
        File dataFile = new File (dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        OutputStream in = new BufferedOutputStream (new FileOutputStream(dataFile));
        in.write(data);
        in.flush();
        in.close();
        name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), dataDirectory.toURL());
        usedURLs.put(url, name);
      }
      return new ImageReferenceData(name);
    }
    else if (isCopyExternalImages ())
    {
      URL url = reference.getSourceURL();
      String name = (String) usedURLs.get(url);
      if (name == null)
      {
        File dataFile = new File (dataDirectory, createName(IOUtils.getInstance().getFileName(url)));
        InputStream urlIn = new BufferedInputStream(reference.getSourceURL().openStream());
        OutputStream fout = new BufferedOutputStream(new FileOutputStream(dataFile));
        IOUtils.getInstance().copyStreams(urlIn, fout);
        urlIn.close();
        fout.close();

        name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), dataDirectory.toURL());
        usedURLs.put(url, name);
      }
      return new ImageReferenceData(name);
    }
    else
    {
      String baseName = IOUtils.getInstance().createRelativeURL( reference.getSourceURL(),
                                                                 dataDirectory.toURL());
      return new ImageReferenceData(baseName);
    }
  }

  public boolean isCopyExternalImages ()
  {
    return copyExternalImages;
  }

  private String createName (String base)
  {
    CounterRef ref = (CounterRef) usedNames.get (base);
    if (ref == null)
    {
      ref = new CounterRef();
      usedNames.put(base, ref);
      return base;
    }
    else
    {
      ref.count++;
      return base + ref.count;
    }
  }

  public HtmlReferenceData createCSSReference(String styleSheet)
    throws IOException
  {
    File refFile = new File(dataDirectory, createName ("style") + ".css");
    OutputStream fout = new BufferedOutputStream (new FileOutputStream(refFile));
    fout.write(styleSheet.getBytes());
    fout.close();
    String baseName = IOUtils.getInstance().createRelativeURL( refFile.toURL(),
                                                               dataDirectory.toURL());
    return new HRefReferenceData(baseName);
  }

  public void close() throws IOException
  {
    rootStream.close();
  }
}
