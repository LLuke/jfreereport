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
 * ----------------------
 * ZIPHtmlFilesystem.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ZIPHtmlFilesystem.java,v 1.12 2005/03/03 23:00:02 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.keypoint.PngEncoder;
import org.jfree.io.IOUtils;
import org.jfree.report.ImageContainer;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.modules.output.table.html.ref.EmptyContentReference;
import org.jfree.report.modules.output.table.html.ref.ExternalStyleSheetReference;
import org.jfree.report.modules.output.table.html.ref.HtmlImageReference;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.html.util.CounterReference;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.util.ImageComparator;
import org.jfree.report.util.NoCloseOutputStream;
import org.jfree.report.util.StringUtil;
import org.jfree.util.WaitingImageObserver;

/**
 * Similiar to the DirectoryHtmlFilesystem, the generated Html-File and the supplementary
 * data files (images and external Stylesheet definition) into a directory in a ZIP-File.
 * The data files can be written into a separated data directory within the ZIP-File.
 * <p/>
 * External referenced content can either be copied into the data directory or could be
 * included as linked content. This behaviour is controled by the
 * <code>copyExternalImages</code> flag.
 *
 * @author Thomas Morgner
 */
public class ZIPHtmlFilesystem implements HtmlFilesystem
{

  /**
   * The name of the data directory within the ZIP-file.
   */
  private String dataDirectory;

  /**
   * the target zip output stream.
   */
  private ZipOutputStream zipOut;

  /**
   * the buffer for caching the root stream, until all external data is generated.
   */
  private ByteArrayOutputStream rootBase;

  /**
   * the root stream for writing the main html file.
   */
  private OutputStream rootStream;

  /**
   * A collection of all used file names for generating external content.
   */
  private HashMap usedNames;

  /**
   * A collection of all referenced external content.
   */
  private HashMap usedImages;

  /**
   * A collection of all previously encoded images.
   */
  private HashMap encodedImages;

  /**
   * the image comparator used to compare generated images.
   */
  private ImageComparator comparator;

  /**
   * A flag indicating whether to copy external references into the data directory.
   */
  private boolean copyExternalImages;

  /**
   * A flag defining whether to use the digest image compare method.
   */
  private boolean digestImageCompare;

  /**
   * Creates a new ZIPHtmlFilesystem. The given output stream is used to write a generated
   * Zip-File. The given data directory must denote a relative directory within the ZIP
   * file.
   *
   * @param out           the target output stream.
   * @param dataDirectory the data directory, relative within the ZIP file.
   * @throws IOException if an IO error occurs.
   */
  public ZIPHtmlFilesystem (final OutputStream out, String dataDirectory)
          throws IOException
  {
    if (out == null)
    {
      throw new NullPointerException();
    }
    if (dataDirectory == null)
    {
      throw new NullPointerException();
    }

    this.zipOut = new ZipOutputStream(new NoCloseOutputStream(out));
    //this.zipOut.setComment("Created with JFReeReport.");

    this.rootBase = new ByteArrayOutputStream();
    this.rootStream = new DeflaterOutputStream(rootBase, new Deflater(Deflater.BEST_COMPRESSION));

    // dataDirectory creation ...
    final File dataDir = new File(dataDirectory);
    final File baseDir = new File("");
    if (dataDir.isAbsolute() || IOUtils.getInstance().isSubDirectory(baseDir, dataDir) == false)
    {
      throw new IllegalArgumentException("The data directory is no relative directory in the "
              + "zip file");
    }

    dataDirectory = IOUtils.getInstance().createRelativeURL(dataDir.toURL(), baseDir.toURL());
    if (dataDirectory.length() != 0)
    {
      if (dataDirectory.endsWith("/") == false)
      {
        this.dataDirectory = dataDirectory + "/";
      }
      else
      {
        this.dataDirectory = dataDirectory;
      }
    }
    this.usedNames = new HashMap();
    this.usedImages = new HashMap();
    this.comparator = new ImageComparator();
    this.encodedImages = new HashMap();
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
    return rootStream;
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
   * <p/>
   * If the image was generated during the report processing or is not in a commonly
   * supported format, then the image is recoded as PNG and the recoded image is included
   * in the data directory.
   * <p/>
   * If external referenced data should be copied into the data directory, then the Image
   * content is read and copied into the data directory.
   *
   * @param reference the image reference containing the data.
   * @return the generated HtmlReference, never null.
   *
   * @throws IOException if IO errors occured while creating the reference.
   * @see ZIPHtmlFilesystem#isSupportedImageFormat
   */
  public HtmlReference createImageReference (final ImageContainer reference)
          throws IOException
  {

    // The image has an assigned URL ...
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) reference;

      // first we check for cached instances ...
      final URL url = urlImage.getSourceURL();
      if (url != null)
      {
        final String name = (String) usedImages.get(url);
        if (name != null)
        {
          return new HtmlImageReference(name);
        }

        // sadly this one seems to be new, not yet cached ...
        // so we have to create something new ...

        // it is one of the supported image formats ...
        // we we can embedd it directly ...
        if (isSupportedImageFormat(urlImage.getSourceURL()))
        {
          // check, whether we should copy the contents into the local
          // data directory ...
          if (isCopyExternalImages() && urlImage.isLoadable())
          {
            final IOUtils iou = IOUtils.getInstance();
            final String entryName =
                    dataDirectory + createName(iou.getFileName(url));
            final ZipEntry ze = new ZipEntry(entryName);
            zipOut.putNextEntry(ze);

            final InputStream urlIn = new BufferedInputStream(urlImage.getSourceURL()
                    .openStream());
            IOUtils.getInstance().copyStreams(urlIn, zipOut);
            urlIn.close();
            usedImages.put(url, entryName);
            return new HtmlImageReference(entryName);
          }
          else
          {
            final String baseName = urlImage.getSourceURL().toExternalForm();
            usedImages.put(url, baseName);
            return new HtmlImageReference(baseName);
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

        // Check, whether the imagereference contains an AWT image.
        Image image = null;

        // The image is not directly embeddable, so we have to convert it
        // into a supported format (PNG in this case)
        if (reference instanceof LocalImageContainer)
        {
          // if so, then we can use that image instance for the recoding
          final LocalImageContainer li = (LocalImageContainer) reference;
          image = li.getImage();
        }
        if (image == null)
        {
          image = ImageFactory.getInstance().createImage(url);
        }
        if (image != null)
        {
          // now encode the image. We don't need to create digest data
          // for the image contents, as the image is perfectly identifyable
          // by its URL
          final String entryName = encodeImage(image, false);
          usedImages.put(url, entryName);
          return new HtmlImageReference(entryName);
        }
      }
      else if (urlImage.getSourceURLString() != null)
      {
        return new HtmlImageReference(urlImage.getSourceURLString());
      }
    }

    // check, whether the image is a locally created image
    // such an image has no assigned URL, so we have to find an
    // artificial name
    if (reference instanceof LocalImageContainer)
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
      final Image image = li.getImage();
      if (image != null)
      {
        if (li.isIdentifiable())
        {
          final Object identity = li.getIdentity();
          String name = (String) usedImages.get(identity);
          if (name == null)
          {
            name = encodeImage(image, false);
            usedImages.put(identity, name);
          }
          return new HtmlImageReference(name);
        }
        return new HtmlImageReference(encodeImage(image, true));
      }
    }

    // it is neither a local nor a URL image container, we don't handle
    // that..
    return new EmptyContentReference();
  }

  /**
   * Encodes the given image as PNG, stores the image in the generated file and returns
   * the name of the new image file.
   *
   * @param image            the image to be encoded
   * @param createComparator true, if the image creation should be cached to avoid
   *                         duplicate images
   * @return the name of the image, never null.
   *
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
    final String entryName =
            dataDirectory + createName("picture") + ".png";

    final ZipEntry ze = new ZipEntry(entryName);
    zipOut.putNextEntry(ze);
    zipOut.write(data);

    if (createComparator)
    {
      encodedImages.put(object, entryName);
    }
    return entryName;
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
    final String entryName = dataDirectory + createName("style") + ".css";
    final ZipEntry ze = new ZipEntry(entryName);
    zipOut.putNextEntry(ze);

    //File dataFile = new File (dataDirectory, createName("picture") + ".png");
    // a png encoder is included in JCommon ...

    zipOut.write(styleSheet.getBytes());

    final String baseName = entryName;
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
    final String entryName = createName("report") + ".html";
    final ZipEntry ze = new ZipEntry(entryName);
    zipOut.putNextEntry(ze);

    rootStream.flush();
    rootStream.close();
    final byte[] data = rootBase.toByteArray();
    rootBase = null;

    final InflaterInputStream infIn
            = new InflaterInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
    IOUtils.getInstance().copyStreams(infIn, zipOut);
    zipOut.closeEntry();
    zipOut.flush();
    zipOut.close();
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
