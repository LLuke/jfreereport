/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DirectoryHtmlFilesystem.java,v 1.8 2004/03/16 15:09:53 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.awt.Image;
import java.awt.Toolkit;

import com.keypoint.PngEncoder;
import org.jfree.io.IOUtils;
import org.jfree.report.modules.output.table.html.util.CounterReference;
import org.jfree.report.modules.output.table.html.ref.ExternalStyleSheetReference;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.html.ref.ImageReference;
import org.jfree.report.modules.output.table.html.ref.EmptyContentReference;
import org.jfree.report.util.ImageComparator;
import org.jfree.report.util.StringUtil;
import org.jfree.report.util.WaitingImageObserver;
import org.jfree.report.ImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.LocalImageContainer;

/**
 * Writes the generated Html-File and the supplementary data files (images and external
 * Stylesheet definition) into a directory. The data files can be written into a separated
 * data directory.
 * <p/>
 * External referenced content can either be copied into the data directory or could be
 * included as linked content. This behaviour is controled by the
 * <code>copyExternalImages</code> flag.
 *
 * @author Thomas Morgner
 */
public class DirectoryHtmlFilesystem
        implements HtmlFilesystem
{
  /**
   * the root file to store the generated main html file.
   */
  private File rootFile;

  /**
   * the handle to the data directory.
   */
  private File dataDirectory;

  /**
   * the root stream for writing the file.
   */
  private FileOutputStream rootStream;

  /**
   * A collection of all used file names for generating external content.
   */
  private final HashMap usedNames;

  /**
   * A collection of all referenced external content.
   */
  private final HashMap usedImages;

  /**
   * A collection of all previously encoded images.
   */
  private final HashMap encodedImages;

  /**
   * the image comparator used to compare generated images.
   */
  private final ImageComparator comparator;

  /**
   * A flag indicating whether to copy external references into the data directory.
   */
  private boolean copyExternalImages;

  /**
   * A flag defining whether to use the digest image compare method.
   */
  private boolean digestImageCompare;

  /**
   * Creates a new DirectoryHtmlFilesystem, using the <code>file</code> as root file and
   * the file's directory as data directory to store the generated data files.
   *
   * @param file the filename of the root html file.
   * @throws IOException if an error occurs.
   */
  public DirectoryHtmlFilesystem (final File file)
          throws IOException
  {
    this(file, file.getParentFile());
  }

  /**
   * Creates a new DirectoryHtmlFilesystem, using the <code>file</code> as root file and
   * the <code>dataDirectory</code> to store the generated data files.
   *
   * @param file          the filename of the root html file.
   * @param dataDirectory the name of the data directory, where the files should be
   *                      created.
   * @throws IOException if an error occurs.
   */
  public DirectoryHtmlFilesystem (final File file, final File dataDirectory)
          throws IOException
  {
    this.usedNames = new HashMap();
    this.usedImages = new HashMap();
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
   * Returns true, if external content should be copied into the DataDirectory of the
   * ZIPFile or false if the content should be included as linked content.
   * <p/>
   * Linked content reduces the filesize, but the reader of the report will need access to
   * the linked files. If you pan to use the report offline, then it is best to copy all
   * referenced data into the zip file.
   *
   * @return true, if external referenced content should be copied into the ZIP file,
   *         false otherwise.
   */
  public boolean isCopyExternalImages ()
  {
    return copyExternalImages;
  }

  /**
   * Defines, whether external content should be copied into the DataDirectory of the
   * ZIPFile or should be included as linked content.
   * <p/>
   * Linked content reduces the filesize, but the reader of the report will need access to
   * the linked files. If you pan to use the report offline, then it is best to copy all
   * referenced data into the zip file.
   *
   * @param copyExternalImages true, if external referenced content should be copied into
   *                           the ZIP file, false otherwise.
   */
  public void setCopyExternalImages (final boolean copyExternalImages)
  {
    this.copyExternalImages = copyExternalImages;
  }

  /**
   * The root stream is used to write the main HTML-File. Any external content is
   * referenced from this file.
   *
   * @return the output stream of the main HTML file.
   *
   * @throws IOException if an IO error occured, while providing the root stream.
   */
  public OutputStream getRootStream ()
          throws IOException
  {
    if (rootStream == null)
    {
      rootStream = new FileOutputStream(rootFile);
    }
    return rootStream;
  }

  /**
   * Tests, whether the given URL points to a supported file format for common browsers.
   * Returns true if the URL references a JPEG, PNG or GIF image, false otherwise.
   * <p/>
   * The checked filetypes are the ones recommended by the W3C.
   *
   * @param url the url that should be tested.
   * @return true, if the content type is supported by the browsers, false otherwise.
   */
  protected boolean isSupportedImageFormat (final URL url)
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
   * Creates a HtmlReference for ImageData. If the target filesystem does not support this
   * reference type, return an empty content reference, but never null.
   *
   * @param reference the image reference containing the data.
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if IO errors occured while creating the reference.
   * @see DirectoryHtmlFilesystem#isSupportedImageFormat
   */
  public HtmlReference createImageReference (final ImageContainer reference)
          throws IOException
  {
    final Image image;

    // The image has an assigned URL ...
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) reference;

      final URL url = urlImage.getSourceURL();
      final String name = (String) usedImages.get(url);
      if (name != null)
      {
        return new ImageReference(name);
      }

      // and is one of the supported image formats ...
      // we we can embedd it directly ...
      if (isSupportedImageFormat(urlImage.getSourceURL()))
      {
        // check, whether we should copy the contents into the local
        // data directory ...
        if (isCopyExternalImages() && urlImage.isLoadable())
        {
          final File dataFile = new File
                  (dataDirectory, createName(IOUtils.getInstance().getFileName(url)));
          final InputStream urlIn = new BufferedInputStream
                  (urlImage.getSourceURL().openStream());
          final OutputStream fout = new BufferedOutputStream
                  (new FileOutputStream(dataFile));
          IOUtils.getInstance().copyStreams(urlIn, fout);
          urlIn.close();
          fout.close();

          final String filename = IOUtils.getInstance().createRelativeURL
                  (dataFile.toURL(), rootFile.toURL());
          usedImages.put(url, filename);
          return new ImageReference(filename);
        }
        else
        {
          final String baseName = IOUtils.getInstance().createRelativeURL
                  (urlImage.getSourceURL(), rootFile.toURL());
          usedImages.put(url, baseName);
          return new ImageReference(baseName);
        }
        // done: Remote image with supported format
      }

      // The image is not directly embeddable, so we have to convert it
      // into a supported format (PNG in this case)

      // if the image is not loadable, we can't do anything ...
      // print the default empty content instead ...
      if (urlImage.isLoadable() == false)
      {
        return new EmptyContentReference();
      }

      // todo BUG: what if the user gives us a valid picture*.png file?

      // Check, whether the imagereference contains an AWT image.
      if (reference instanceof LocalImageContainer)
      {
        // if so, then we can use that image instance for the recoding
        final LocalImageContainer li = (LocalImageContainer) reference;
        image = li.getImage();
      }
      else
      {
        image = Toolkit.getDefaultToolkit().createImage(url);
      }
      // now encode the image. We don't need to create digest data
      // for the image contents, as the image is perfectly identifyable
      // by its URL
      final String entryName = encodeImage(image, false);
      usedImages.put(url, entryName);
      return new ImageReference(entryName);
    }
    // check, whether the image is a local image
    else if (reference instanceof LocalImageContainer)
    {
      // that image has no useable URL, but contains local image
      // data, so we can start to encode it. We will create a
      // fingerprint of the image contents to cache the image.
      //
      // This will not save any time (we,, it even costs more time), but
      // will help to reduce the space used by the output.
      //
      // LocalImageContainer instances are free to supply more
      // suitable comparator information
      final LocalImageContainer li = (LocalImageContainer) reference;
      image = li.getImage();
      if (li.isIdentifiable())
      {
        final Object identity = li.getIdentity();
        String name = (String) usedImages.get(identity);
        if (name == null)
        {
          name = encodeImage(image, false);
          usedImages.put (identity, name);
        }
        return new ImageReference(name);
      }
      return new ImageReference(encodeImage(image, true));
    }
    else
    {
      // it is neither a local nor a URL image container, we don't handle
      // that..
      return new EmptyContentReference();
    }
  }

  /**
   * Encodes the given image as PNG, stores the image in the generated
   * file and returns the name of the new image file.
   *
   * @param image the image to be encoded
   * @param createComparator true, if the image creation should be cached to
   * avoid duplicate images
   * @return the name of the image, never null.
   * @throws IOException if an IO erro occured.
   */
  private String encodeImage (final Image image, final boolean createComparator)
    throws IOException
  {
    // quick caching ... use a weak list ...
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();

    final PngEncoder encoder = new PngEncoder(image,
            PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
    final byte[] data = encoder.pngEncode();

    final Object object;
    if (createComparator)
    {
      object = comparator.createCompareData(data, isDigestImageCompare() == false);
      final String name = (String) encodedImages.get(object);
      if (name != null)
      {
        return name;
      }
    }
    else
    {
      object = null;
    }

    // write the encoded picture ...
    final File dataFile = new File(dataDirectory, createName("picture") + ".png");
    // a png encoder is included in JCommon ...
    final OutputStream out = new BufferedOutputStream(new FileOutputStream(dataFile));
    out.write(data);
    out.flush();
    out.close();
    final String name = IOUtils.getInstance().createRelativeURL(dataFile.toURL(), rootFile.toURL());
    if (createComparator)
    {
      encodedImages.put(object, name);
    }
    return name;
  }

  /**
   * Creates an unique name for resources in the data directory.
   *
   * @param base the basename.
   * @return the unique name generated using the basename.
   */
  private String createName (final String base)
  {
    CounterReference ref = (CounterReference) usedNames.get(base);
    if (ref == null)
    {
      ref = new CounterReference();
      usedNames.put(base, ref);
      return base;
    }
    else
    {
      ref.increase();
      return base + ref.getCount();
    }
  }

  /**
   * Creates a HtmlReference for StyleSheetData. If the target filesystem does not support
   * external stylesheets, return an inline stylesheet reference.
   *
   * @param styleSheet the stylesheet data, which should be referenced.
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if IO errors occured while creating the reference.
   */
  public HtmlReference createCSSReference (final String styleSheet)
          throws IOException
  {
    final File refFile = new File(dataDirectory, createName("style") + ".css");
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(refFile));
    fout.write(styleSheet.getBytes());
    fout.close();
    final String baseName = IOUtils.getInstance().createRelativeURL(refFile.toURL(),
            rootFile.toURL());
    return new ExternalStyleSheetReference(baseName);
  }

  /**
   * Close the Filesystem and write any buffered content. The filesystem will not be
   * accessed, after close was called.
   *
   * @throws IOException if the close operation failed.
   */
  public void close ()
          throws IOException
  {
    rootStream.close();
  }

  /**
   * Returns, whether to use digest image compare instead of internal java methods. This
   * method reduces memory consumption for the price of complexer computations (and
   * reduced execution speed).
   *
   * @return true, if the digest compare should be used, false otherwise.
   */
  public boolean isDigestImageCompare ()
  {
    return digestImageCompare;
  }

  /**
   * Defines, whether to use digest image compare instead of internal java methods. This
   * method reduces memory consumption for the price of complexer computations (and
   * reduced execution speed).
   *
   * @param digestImageCompare set to true, if the digest compare should be used, false
   *                           otherwise.
   */
  public void setDigestImageCompare (final boolean digestImageCompare)
  {
    this.digestImageCompare = digestImageCompare;
  }

}
