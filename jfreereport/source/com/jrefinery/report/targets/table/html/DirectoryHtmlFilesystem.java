/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ----------------------------
 * DirectoryHtmlFilesystem.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DirectoryHtmlFilesystem.java,v 1.18 2003/06/27 14:25:25 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.ImageComparator;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.util.WaitingImageObserver;
import com.keypoint.PngEncoder;
import org.jfree.io.IOUtils;

/**
 * Writes the generated Html-File and the supplementary data files (images and
 * external Stylesheet definition) into a directory. The data files can be written
 * into a separated data directory.
 * <p>
 * External referenced content can either be copied into the data directory or could
 * be included as linked content. This behaviour is controled by the <code>copyExternalImages</code>
 * flag.
 *
 * @author Thomas Morgner
 */
public class DirectoryHtmlFilesystem implements HtmlFilesystem
{
  /** a simple counter carrier. */
  private class CounterRef
  {
    /** a counter. */
    public int count;
  }

  /** the root file to store the generated main html file. */
  private File rootFile;

  /** the handle to the data directory. */
  private File dataDirectory;

  /** the root stream for writing the file. */
  private FileOutputStream rootStream;

  /** A collection of all used file names for generating external content. */
  private HashMap usedNames;

  /** A collection of all referenced external content. */
  private HashMap usedURLs;

  /** A collection of all previously encoded images. */
  private HashMap encodedImages;

  /** the image comparator used to compare generated images. */
  private ImageComparator comparator;

  /** A flag indicating whether to copy external references into the data directory. */
  private boolean copyExternalImages;

  /**
   * Creates a new DirectoryHtmlFilesystem, using the <code>file</code> as root
   * file and the file's directory as data directory to store the generated data files.
   *
   * @param file the filename of the root html file.
   * @throws IOException if an error occurs.
   */
  public DirectoryHtmlFilesystem(final File file)
      throws IOException
  {
    this(file, file.getParentFile());
  }

  /**
   * Creates a new DirectoryHtmlFilesystem, using the <code>file</code> as root
   * file and the <code>dataDirectory</code> to store the generated data files.
   *
   * @param file the filename of the root html file.
   * @param dataDirectory the name of the data directory, where the files should be created.
   * @throws IOException if an error occurs.
   */
  public DirectoryHtmlFilesystem(final File file, final File dataDirectory)
      throws IOException
  {
    this.usedNames = new HashMap();
    this.usedURLs = new HashMap();
    this.encodedImages = new HashMap();
    this.comparator = new ImageComparator();

    if (file.exists() && file.isFile() == false)
    {
      throw new IOException("The given file-parameter does not point to a data file");
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

  /**
   * Returns true, if external content should be copied into the DataDirectory of
   * the ZIPFile or false if the content should be included as linked content.
   * <p>
   * Linked content reduces the filesize, but the reader of the report will need access
   * to the linked files. If you pan to use the report offline, then it is best to
   * copy all referenced data into the zip file.
   *
   * @return true, if external referenced content should be copied into the ZIP file,
   * false otherwise.
   */
  public boolean isCopyExternalImages()
  {
    return copyExternalImages;
  }

  /**
   * Defines, whether external content should be copied into the DataDirectory of
   * the ZIPFile or should be included as linked content.
   * <p>
   * Linked content reduces the filesize, but the reader of the report will need access
   * to the linked files. If you pan to use the report offline, then it is best to
   * copy all referenced data into the zip file.
   *
   * @param copyExternalImages true, if external referenced content should be copied into the
   *                           ZIP file, false otherwise.
   */
  public void setCopyExternalImages(final boolean copyExternalImages)
  {
    this.copyExternalImages = copyExternalImages;
  }

  /**
   * The root stream is used to write the main HTML-File. Any external content is
   * referenced from this file.
   *
   * @return the output stream of the main HTML file.
   * @throws IOException if an IO error occured, while providing the root stream.
   */
  public OutputStream getRootStream()
      throws IOException
  {
    if (rootStream == null)
    {
      rootStream = new FileOutputStream(rootFile);
    }
    return rootStream;
  }

  /**
   * Tests, whether the given URL points to a supported file format for common
   * browsers. Returns true if the URL references a JPEG, PNG or GIF image, false
   * otherwise.
   * <p>
   * The checked filetypes are the ones recommended by the W3C.
   *
   * @param url the url that should be tested.
   * @return true, if the content type is supported by the browsers, false otherwise.
   */
  protected boolean isSupportedImageFormat(final URL url)
  {
    final String file = url.getFile();
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

  /**
   * Creates a HtmlReference for ImageData. If the target filesystem does not support
   * this reference type, return an empty content reference, but never null.
   *
   * @param reference the image reference containing the data.
   * @return the generated HtmlReference, never null.
   * @throws IOException if IO errors occured while creating the reference.
   * @see DirectoryHtmlFilesystem#isSupportedImageFormat
   */
  public HtmlReferenceData createImageReference(final ImageReference reference)
      throws IOException
  {
    if (reference.getSourceURL() == null)
    {
      final WaitingImageObserver obs = new WaitingImageObserver(reference.getImage());
      obs.waitImageLoaded();

      final PngEncoder encoder = new PngEncoder(reference.getImage(),
          PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
      final byte[] data = encoder.pngEncode();

      final Object object = comparator.createCompareData(data);
      String name = (String) encodedImages.get(object);
      if (name == null)
      {
        // encode the picture ...
        final File dataFile = new File(dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        final OutputStream in = new BufferedOutputStream(new FileOutputStream(dataFile));
        in.write(data);
        in.flush();
        in.close();
        name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), dataDirectory.toURL());
        encodedImages.put(object, name);
      }
      return new ImageReferenceData(name);
    }
    else if (isSupportedImageFormat(reference.getSourceURL()) == false)
    {
      final URL url = reference.getSourceURL();
      String name = (String) usedURLs.get(url);
      if (name == null)
      {
        final WaitingImageObserver obs = new WaitingImageObserver(reference.getImage());
        obs.waitImageLoaded();

        final PngEncoder encoder = new PngEncoder(reference.getImage(),
            PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
        final byte[] data = encoder.pngEncode();

        // encode the picture ...
        final File dataFile = new File(dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        final OutputStream in = new BufferedOutputStream(new FileOutputStream(dataFile));
        in.write(data);
        in.flush();
        in.close();
        name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), dataDirectory.toURL());
        usedURLs.put(url, name);
      }
      return new ImageReferenceData(name);
    }
    else if (isCopyExternalImages())
    {
      final URL url = reference.getSourceURL();
      String name = (String) usedURLs.get(url);
      if (name == null)
      {
        final File dataFile
            = new File(dataDirectory, createName(IOUtils.getInstance().getFileName(url)));
        final InputStream urlIn = new BufferedInputStream(reference.getSourceURL().openStream());
        final OutputStream fout = new BufferedOutputStream(new FileOutputStream(dataFile));
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
      final String baseName = IOUtils.getInstance().createRelativeURL(reference.getSourceURL(),
          dataDirectory.toURL());
      return new ImageReferenceData(baseName);
    }
  }

  /**
   * Creates an unique name for resources in the data directory.
   *
   * @param base the basename.
   * @return the unique name generated using the basename.
   */
  private String createName(final String base)
  {
    CounterRef ref = (CounterRef) usedNames.get(base);
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

  /**
   * Creates a HtmlReference for StyleSheetData. If the target filesystem does not
   * support external stylesheets, return an inline stylesheet reference.
   *
   * @param styleSheet the stylesheet data, which should be referenced.
   * @return the generated HtmlReference, never null.
   * @throws IOException if IO errors occured while creating the reference.
   */
  public HtmlReferenceData createCSSReference(final String styleSheet)
      throws IOException
  {
    final File refFile = new File(dataDirectory, createName("style") + ".css");
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(refFile));
    fout.write(styleSheet.getBytes());
    fout.close();
    final String baseName = IOUtils.getInstance().createRelativeURL(refFile.toURL(),
        dataDirectory.toURL());
    return new HRefReferenceData(baseName);
  }

  /**
   * Close the Filesystem and write any buffered content. The filesystem will not
   * be accessed, after close was called.
   *
   * @throws IOException if the close operation failed.
   */
  public void close() throws IOException
  {
    rootStream.close();
  }
}
