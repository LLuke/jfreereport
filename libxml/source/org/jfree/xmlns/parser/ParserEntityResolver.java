/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id: ParserEntityResolver.java,v 1.3 2006/12/19 17:46:36 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.xmlns.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import org.jfree.util.Log;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Resolves the JFreeReport DTD specification and routes the parser to a local copy.
 *
 * @author Thomas Morgner
 */
public final class ParserEntityResolver implements EntityResolver
{
  /**
   * The hashtable for the known entities (deprecated DTDs).
   */
  private final Hashtable deprecatedDTDs;
  /**
   * The hashtable for the known entities.
   */
  private final Hashtable dtds;
  /**
   * The singleton instance of this entity resolver.
   */
  private static ParserEntityResolver singleton;

  /**
   * Creates a new, uninitialized ParserEntityResolver.
   */
  private ParserEntityResolver ()
  {
    dtds = new Hashtable();
    deprecatedDTDs = new Hashtable();
  }

  /**
   * Defines a DTD used to validate the report definition. Your XMLParser must be a
   * validating parser for this feature to work.
   *
   * @param publicID the public ID.
   * @param location the URL.
   * @return A boolean.
   */
  public boolean setDTDLocation (final String publicID, final URL location)
  {
    if (isValid(location))
    {
      this.dtds.put(publicID, location);
      return true;
    }
    else
    {
      Log.warn(new Log.SimpleMessage("Validate location failed for ",
              publicID, " location: ", location));
      return false;
    }
  }

  /**
   * Defines a DTD used to validate the report definition. Your XMLParser must be a
   * validating parser for this feature to work.
   *
   * @param publicID the public ID.
   * @param location the URL.
   * @return A boolean.
   */
  public boolean setDTDLocation (final String publicID,
                                 final String systemId,
                                 final URL location)
  {
    if (isValid(location))
    {
      this.dtds.put(publicID, location);
      this.dtds.put(systemId, location);
      return true;
    }
    else
    {
      Log.warn(new Log.SimpleMessage("Validate location failed for ",
              publicID, " location: ", location));
      return false;
    }
  }

  /**
   * Sets the location of the DTD. This is used for validating XML parsers to validate the
   * structure of the report definition.
   *
   * @param publicID the id.
   * @return the URL for the DTD.
   */
  public URL getDTDLocation (final String publicID)
  {
    return (URL) dtds.get(publicID);
  }

  /**
   * Checks whether the speficied URL is readable.
   *
   * @param reportDtd the url pointing to the local DTD copy.
   * @return true, if the URL can be read, false otherwise.
   */
  private boolean isValid (final URL reportDtd)
  {
    if (reportDtd == null)
    {
      return false;
    }
    try
    {
      final InputStream uc = reportDtd.openStream();
      uc.close();
      return true;
    }
    catch (IOException ioe)
    {
      return false;
    }
  }

  /**
   * Allow the application to resolve external entities.
   * <p/>
   * Resolves the DTD definition to point to a local copy, if the specified public ID is
   * known to this resolver.
   *
   * @param publicId the public ID.
   * @param systemId the system ID.
   * @return The input source.
   */
  public InputSource resolveEntity (final String publicId,
                                    final String systemId)
  {
    try
    {
      // cannot validate without public id ...
      if (publicId == null)
      {
        //Log.debug ("No PUBLIC ID, cannot continue");
        if (systemId != null)
        {
          final URL location = getDTDLocation(systemId);
          if (location != null)
          {
            return new InputSource(location.openStream());
          }
        }
        return null;
      }

      final URL location = getDTDLocation(publicId);
      if (location != null)
      {
        return new InputSource(location.openStream());
      }
      final String message = getDeprecatedDTDMessage(publicId);
      if (message != null)
      {
        Log.info(message);
      }
      else
      {
        Log.info("A public ID was given for the document, " +
                "but it was unknown or invalid.");
      }
      return null;
    }
    catch (IOException ioe)
    {
      Log.warn("Unable to open specified DTD", ioe);
    }
    return null;
  }

  /**
   * Returns a default resolver, which is initialized to redirect the parser to a local
   * copy of the JFreeReport DTDs.
   *
   * @return the default entity resolver.
   */
  public static synchronized ParserEntityResolver getDefaultResolver ()
  {
    if (singleton == null)
    {
      singleton = new ParserEntityResolver();
    }
    return singleton;
  }

  public void setDeprecatedDTDMessage (final String publicID, final String message)
  {
    deprecatedDTDs.put (publicID, message);
  }


  public String getDeprecatedDTDMessage (final String publicID)
  {
    return (String) deprecatedDTDs.get(publicID);
  }
}
