/**
 * Date: Jan 26, 2003
 * Time: 7:06:56 PM
 *
 * $Id: ZIPHtmlFilesystem.java,v 1.5 2003/02/02 23:43:52 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.IOUtils;
import com.jrefinery.report.util.ImageComparator;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NoCloseOutputStream;
import com.jrefinery.report.util.StringUtil;
import com.keypoint.PngEncoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIPHtmlFilesystem implements HtmlFilesystem
{
  private class CounterRef
  {
    public int count;
  }

  private String dataDirectory;
  private ZipOutputStream zipOut;
  private ByteArrayOutputStream rootBase;
  private OutputStream rootStream;
  private Hashtable usedNames;
  private Hashtable usedURLs;
  private Hashtable encodedImages;
  private ImageComparator comparator;
  private boolean copyExternalImages;

  public ZIPHtmlFilesystem(OutputStream out, String dataDirectory)
    throws IOException
  {
    if (out == null) throw new NullPointerException();
    if (dataDirectory == null) throw new NullPointerException();

    this.zipOut = new ZipOutputStream(new NoCloseOutputStream (out));
    //this.zipOut.setComment("Created with JFReeReport.");

    this.rootBase = new ByteArrayOutputStream();
    this.rootStream = new DeflaterOutputStream(rootBase, new Deflater(Deflater.BEST_COMPRESSION));

    // dataDirectory creation ...
    File dataDir = new File (dataDirectory);
    File baseDir = new File ("");
    if (dataDir.isAbsolute() || IOUtils.getInstance().isSubDirectory(baseDir, dataDir))
    {
      throw new IllegalArgumentException("The data directory is no relative directory in the zip file");
    }

    dataDirectory = IOUtils.getInstance().createRelativeURL(dataDir.toURL(), baseDir.toURL());
    if (dataDirectory.endsWith("/") == false)
    {
      this.dataDirectory = dataDirectory + "/";
    }
    else
    {
      this.dataDirectory = dataDirectory;
    }
    this.usedNames = new Hashtable();
    this.usedURLs = new Hashtable();
    this.comparator = new ImageComparator();
    this.encodedImages = new Hashtable();
  }

  // contains the HTML file

  public OutputStream getRootStream() throws IOException
  {
    return rootStream;
  }

  public boolean isCopyExternalImages()
  {
    return copyExternalImages;
  }

  public void setCopyExternalImages(boolean copyExternalImages)
  {
    this.copyExternalImages = copyExternalImages;
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
        String entryName = dataDirectory + createName("picture") + ".png";
        ZipEntry ze = new ZipEntry(entryName);
        Log.debug ("Added: " + entryName);
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
        PngEncoder encoder = new PngEncoder (reference.getImage(), true, 0, 9);
        byte[] data = encoder.pngEncode();

        IOUtils iou = IOUtils.getInstance();
        String entryName = dataDirectory +
            createName(iou.stripFileExtension(iou.getFileName(url))) +
            ".png";
        ZipEntry ze = new ZipEntry(entryName);
        Log.debug ("Added: " + entryName);
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
        String entryName = dataDirectory +
            createName(iou.getFileName(url));
        ZipEntry ze = new ZipEntry(entryName);
        Log.debug ("Added: " + entryName);
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
    String entryName = dataDirectory + createName("style") + ".css";
    ZipEntry ze = new ZipEntry(entryName);
    Log.debug ("Added: " + entryName);
    zipOut.putNextEntry(ze);

    //File dataFile = new File (dataDirectory, createName("picture") + ".png");
    // a png encoder is included in JCommon ...

    zipOut.write(styleSheet.getBytes());

    String baseName = entryName;
    return new HRefReferenceData(baseName);
  }

  public void close() throws IOException
  {
    String entryName = createName("report") + ".html";
    ZipEntry ze = new ZipEntry(entryName);
    Log.debug ("Added: " + entryName);
    zipOut.putNextEntry(ze);

    rootStream.flush();
    rootStream.close();
    byte[] data = rootBase.toByteArray();
    rootBase = null;

    InflaterInputStream infIn = new InflaterInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
    IOUtils.getInstance().copyStreams(infIn, zipOut);
    zipOut.closeEntry();
    zipOut.flush();
    zipOut.close();
  }

}
