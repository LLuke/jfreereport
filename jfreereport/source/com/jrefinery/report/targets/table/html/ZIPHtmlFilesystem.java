/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------
 * ZIPHtmlFilesystem.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ZIPHtmlFilesystem.java,v 1.18 2003/05/09 17:12:13 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

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

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.ImageComparator;
import com.jrefinery.report.util.NoCloseOutputStream;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.util.WaitingImageObserver;
import com.keypoint.PngEncoder;
import org.jfree.io.IOUtils;

/**
 * Similiar to the DirectoryHtmlFilesystem, the generated Html-File and the supplementary
 * data files (images and external Stylesheet definition) into a directory in a ZIP-File.
 * The data files can be written into a separated data directory within the ZIP-File.
 * <p>
 * External referenced content can either be copied into the data directory or could
 * be included as linked content. This behaviour is controled by the <code>copyExternalImages</code>
 * flag.
 * 
 * @author Thomas Morgner
 */
public class ZIPHtmlFilesystem implements HtmlFilesystem
{
  /** a simple counter carrier. */
  private class CounterRef
  {
    /** a counter. */
    public int count;
  }

  /** The name of the data directory within the ZIP-file. */
  private String dataDirectory;

  /** the target zip output stream. */
  private ZipOutputStream zipOut;
  
  /** the buffer for caching the root stream, until all external data is generated. */
  private ByteArrayOutputStream rootBase;
  
  /** the root stream for writing the main html file. */
  private OutputStream rootStream;
  
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
   * Creates a new ZIPHtmlFilesystem. The given output stream is used to write
   * a generated Zip-File. The given data directory must denote a relative directory
   * within the ZIP file.
   *
   * @param out the target output stream.
   * @param dataDirectory the data directory, relative within the ZIP file.
   * 
   * @throws IOException if an IO error occurs.
   */
  public ZIPHtmlFilesystem(OutputStream out, String dataDirectory)
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

    this.zipOut = new ZipOutputStream(new NoCloseOutputStream (out));
    //this.zipOut.setComment("Created with JFReeReport.");

    this.rootBase = new ByteArrayOutputStream();
    this.rootStream = new DeflaterOutputStream(rootBase, new Deflater(Deflater.BEST_COMPRESSION));

    // dataDirectory creation ...
    File dataDir = new File (dataDirectory);
    File baseDir = new File ("");
    if (dataDir.isAbsolute() || IOUtils.getInstance().isSubDirectory(baseDir, dataDir) == false)
    {
      throw new IllegalArgumentException("The data directory is no relative directory in the "
                                         + "zip file");
    }

    dataDirectory = IOUtils.getInstance().createRelativeURL(dataDir.toURL(), baseDir.toURL());
    if (dataDirectory.length () != 0) 
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
    this.usedURLs = new HashMap();
    this.comparator = new ImageComparator();
    this.encodedImages = new HashMap();
  }

  /**
   * The root stream is used to write the main HTML-File. Any external content is
   * referenced from this file.
   *
   * @return the output stream of the main HTML file.
   * @throws IOException if an IO error occured, while providing the root stream.
   */
  public OutputStream getRootStream() throws IOException
  {
    return rootStream;
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
   * @param copyExternalImages true, if external referenced content should be copied into the ZIP 
   *                           file, false otherwise.
   */
  public void setCopyExternalImages(boolean copyExternalImages)
  {
    this.copyExternalImages = copyExternalImages;
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

  /**
   * Creates a HtmlReference for ImageData. If the target filesystem does not support
   * this reference type, return an empty content reference, but never null.
   * <p>
   * If the image was generated during the report processing or is not in a commonly
   * supported format, then the image is recoded as PNG and the recoded image is
   * included in the data directory.
   * <p>
   * If external referenced data should be copied into the data directory, then
   * the Image content is read and copied into the data directory.
   *
   * @param reference the image reference containing the data.
   * 
   * @return the generated HtmlReference, never null.
   * 
   * @throws IOException if IO errors occured while creating the reference.
   * 
   * @see ZIPHtmlFilesystem#isSupportedImageFormat
   */
  public HtmlReferenceData createImageReference(ImageReference reference)
    throws IOException
  {
    if (reference.getSourceURL() == null)
    {
      WaitingImageObserver obs = new WaitingImageObserver(reference.getImage());
      obs.waitImageLoaded();

      PngEncoder encoder = new PngEncoder (reference.getImage(),
          PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
      byte[] data = encoder.pngEncode();

      Object object = comparator.createCompareData(data);
      String name = (String) encodedImages.get(object);
      if (name == null)
      {
        // encode the picture ...
        String entryName = dataDirectory + createName("picture") + ".png";
        ZipEntry ze = new ZipEntry(entryName);
        zipOut.putNextEntry(ze);

        //File dataFile = new File (dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        zipOut.write(data);

        name = entryName;
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
        WaitingImageObserver obs = new WaitingImageObserver(reference.getImage());
        obs.waitImageLoaded();

        PngEncoder encoder = new PngEncoder (reference.getImage(),
            PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
        byte[] data = encoder.pngEncode();

        IOUtils iou = IOUtils.getInstance();
        String entryName = dataDirectory 
            + createName(iou.stripFileExtension(iou.getFileName(url))) + ".png";
        ZipEntry ze = new ZipEntry(entryName);
        zipOut.putNextEntry(ze);

        //File dataFile = new File (dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...
        zipOut.write(data);

        name = entryName;
        // encode the picture ...
        // a png encoder is included in JCommon ...
        usedURLs.put(url, name);
      }
      return new ImageReferenceData(name);
    }
    else if (isCopyExternalImages())
    {
      URL url = reference.getSourceURL();
      String name = (String) usedURLs.get(url);
      if (name == null)
      {
        IOUtils iou = IOUtils.getInstance();
        String entryName = dataDirectory + createName(iou.getFileName(url));
        ZipEntry ze = new ZipEntry(entryName);
        zipOut.putNextEntry(ze);

        //File dataFile = new File (dataDirectory, createName("picture") + ".png");
        // a png encoder is included in JCommon ...

        InputStream urlIn = new BufferedInputStream(reference.getSourceURL().openStream());
        IOUtils.getInstance().copyStreams(urlIn, zipOut);
        urlIn.close();

        name = entryName;
        usedURLs.put(url, name);
      }
      return new ImageReferenceData(name);
    }
    else
    {
      String baseName = reference.getSourceURL().toExternalForm();
      return new ImageReferenceData(baseName);
    }
  }

  /**
   * Creates an unique name for resources in the data directory.
   *
   * @param base the basename.
   * @return the unique name generated using the basename.
   */
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

  /**
   * Creates a HtmlReference for StyleSheetData. If the target filesystem does not
   * support external stylesheets, return an inline stylesheet reference.
   *
   * @param styleSheet the stylesheet data, which should be referenced.
   * @return the generated HtmlReference, never null.
   * @throws IOException if IO errors occured while creating the reference.
   */
  public HtmlReferenceData createCSSReference(String styleSheet)
    throws IOException
  {
    String entryName = dataDirectory + createName("style") + ".css";
    ZipEntry ze = new ZipEntry(entryName);
    zipOut.putNextEntry(ze);

    //File dataFile = new File (dataDirectory, createName("picture") + ".png");
    // a png encoder is included in JCommon ...

    zipOut.write(styleSheet.getBytes());

    String baseName = entryName;
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
    String entryName = createName("report") + ".html";
    ZipEntry ze = new ZipEntry(entryName);
    zipOut.putNextEntry(ze);

    rootStream.flush();
    rootStream.close();
    byte[] data = rootBase.toByteArray();
    rootBase = null;

    InflaterInputStream infIn 
        = new InflaterInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
    IOUtils.getInstance().copyStreams(infIn, zipOut);
    zipOut.closeEntry();
    zipOut.flush();
    zipOut.close();
  }

}
