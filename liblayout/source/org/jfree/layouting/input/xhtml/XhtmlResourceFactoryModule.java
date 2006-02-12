package org.jfree.layouting.input.xhtml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jfree.loader.resourceloader.ResourceFactoryModule;
import org.jfree.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XhtmlResourceFactoryModule implements ResourceFactoryModule
{
  private static final String[] MIMETYPES =
          {
             "application/xhtml+xml",
             "application/vnd.pwg-xhtml-print+xml",
             "text/html", "text/xml", "application/xml"
          };

  private static final String[] EXTENSIONS =
          {
                  "xht", "xhtml",
                  "htm", "html", "shtml"
          };

  public XhtmlResourceFactoryModule ()
  {
  }

  public boolean canHandleResourceByContent (byte[] content)
  {
    // there are thousand ways to encode html, we dont go that way
    return false;
  }

  public boolean canHandleResourceByMimeType (String name)
  {
    for (int i = 0; i < MIMETYPES.length; i++)
    {
      if (name.equals(MIMETYPES[i]))
      {
        return true;
      }
    }
    return false;
  }

  public boolean canHandleResourceByName (String name)
  {
    for (int i = 0; i < EXTENSIONS.length; i++)
    {
      if (StringUtils.endsWithIgnoreCase(name, EXTENSIONS[i]))
      {
        return true;
      }
    }
    return false;
  }

  public int getHeaderFingerprintSize ()
  {
    return 0;
  }

  public XhtmlDocument createDocument (final byte[] data,
                                  final URL source,
                                  final String fileName,
                                  final String mimeType)
          throws IOException
  {
    try
    {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      InputSource is = new InputSource(new ByteArrayInputStream(data));
      Document doc = db.parse(is);
      return new XhtmlDocument(source, data, doc);
    }
    catch(IOException ioe)
    {
      throw ioe;
    }
    catch (ParserConfigurationException e)
    {
      throw new IOException(e.getMessage());
    }
    catch (SAXException e)
    {
      throw new IOException(e.getMessage());
    }
  }


  public XhtmlDocument createDocument (final InputStream data,
                                  final URL source,
                                  final String fileName,
                                  final String mimeType)
          throws IOException
  {
    try
    {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      InputSource is = new InputSource(data);
      Document doc = db.parse(is);
      return new XhtmlDocument(source, null, doc);
    }
    catch(IOException ioe)
    {
      throw ioe;
    }
    catch (ParserConfigurationException e)
    {
      throw new IOException(e.getMessage());
    }
    catch (SAXException e)
    {
      throw new IOException(e.getMessage());
    }
  }
}
