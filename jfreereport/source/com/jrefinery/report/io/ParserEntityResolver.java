/**
 * Date: Feb 1, 2003
 * Time: 5:56:29 PM
 *
 * $Id: ParserEntityResolver.java,v 1.3 2003/02/12 17:36:06 taqua Exp $
 */
package com.jrefinery.report.io;

import com.jrefinery.report.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

public class ParserEntityResolver implements EntityResolver
{
  public static final String PUBLIC_ID_SIMPLE =   "-//JFreeReport//DTD report definition//EN//simple";
  public static final String PUBLIC_ID_EXTENDED = "-//JFreeReport//DTD report definition//EN//extended";

  private Hashtable dtds;

  public ParserEntityResolver()
  {
    dtds = new Hashtable();
  }

  /**
   * Defines a DTD used to validate the report definition. Your XMLParser
   * must be a validating parser for this feature to work.
   *
   * @param publicID the URL for the DTD.
   */
  public boolean setDTDLocation (String publicID, URL location)
  {
    if (isValid(location))
    {
      this.dtds.put(publicID, location);
      return true;
    }
    else
    {
      Log.warn ("Validate location failed for location: " + location);
      return false;
    }
  }

  /**
   * Sets the location of the DTD. This is used for validating XML parsers to
   * validate the structure of the report definition.
   *
   * @return the URL for the DTD.
   */
  public URL getDTDLocation (String publicID)
  {
    return (URL) dtds.get(publicID);
  }

  private boolean isValid (URL reportDtd)
  {
    if (reportDtd == null) return false;
    try
    {
      InputStream uc = reportDtd.openStream();
      uc.close();
      return true;
    }
    catch (IOException ioe)
    {
      return false;
    }
  }

  public InputSource getExternalSubset(String name, String baseURI)
      throws SAXException, IOException
  {
    return null;
  }

  public InputSource resolveEntity(String name, String publicID, String baseURI, String systemId)
      throws SAXException, IOException
  {
    if (publicID == null)
      return null;

    // only resolve the DTD, any other content is not affected ...
    if (name.equals("[dtd]"))
    {
      try
      {
        URL location = getDTDLocation(publicID);
        if (location == null)
        {
          //Log.debug ("PublicID " + publicID + " is not registered");
          return null;
        }
        return new InputSource(location.openStream());
      }
      catch (IOException ioe)
      {
        Log.warn("Unable to open specified DTD", ioe);
      }
    }
    return null;
  }

  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException
  {
    try
    {
      // cannot validate without public id ...
      if (publicId == null)
      {
        //Log.debug ("No PUBLIC ID, cannot continue");
        return null;
      }

      URL location = getDTDLocation(publicId);
      return new InputSource(location.openStream());
    }
    catch (IOException ioe)
    {
      Log.warn("Unable to open specified DTD", ioe);
    }
    return null;
  }

  public static ParserEntityResolver getDefaultResolver ()
  {
    ParserEntityResolver res = new ParserEntityResolver();
    URL urlReportDTD = res.getClass().getResource("/com/jrefinery/report/resources/report.dtd");
    res.setDTDLocation(PUBLIC_ID_SIMPLE, urlReportDTD);
    URL urlExtReportDTD = res.getClass().getResource("/com/jrefinery/report/resources/extreport.dtd");
    res.setDTDLocation(PUBLIC_ID_EXTENDED, urlExtReportDTD);
    return res;
  }
}
