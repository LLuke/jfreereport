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
 * -------------------------
 * ParserEntityResolver.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ParserEntityResolver.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 * 23-Feb-2003 : Documentation
 */
package org.jfree.report.modules.parser.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import org.jfree.report.util.Log;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Resolves the JFreeReport DTD specification and routes the parser 
 * to a local copy.
 *
 * @author Thomas Morgner
 */
public final class ParserEntityResolver implements EntityResolver
{
  /** The hashtable for the known entities. */
  private Hashtable dtds;
  /** The singleton instance of this entity resolver. */ 
  private static ParserEntityResolver singleton;

  /**
   * Creates a new, uninitialized ParserEntityResolver.
   */
  private ParserEntityResolver()
  {
    dtds = new Hashtable();
  }

  /**
   * Defines a DTD used to validate the report definition. Your XMLParser
   * must be a validating parser for this feature to work.
   *
   * @param publicID  the public ID.
   * @param location  the URL.
   *
   * @return A boolean.
   */
  public boolean setDTDLocation(final String publicID, final URL location)
  {
    if (isValid(location))
    {
      this.dtds.put(publicID, location);
      return true;
    }
    else
    {
      Log.warn("Validate location failed for location: " + location);
      return false;
    }
  }

  /**
   * Sets the location of the DTD. This is used for validating XML parsers to
   * validate the structure of the report definition.
   *
   * @param publicID  the id.
   *
   * @return the URL for the DTD.
   */
  public URL getDTDLocation(final String publicID)
  {
    return (URL) dtds.get(publicID);
  }

  /**
   * Checks whether the speficied URL is readable.
   *
   * @param reportDtd the url pointing to the local DTD copy.
   *
   * @return true, if the URL can be read, false otherwise.
   */
  private boolean isValid(final URL reportDtd)
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
   * <p>
   * Resolves the DTD definition to point to a local copy, if the specified
   * public ID is known to this resolver.
   *
   * @param publicId  the public ID.
   * @param systemId  the system ID.
   *
   * @return The input source.
   *
   * @throws org.xml.sax.SAXException if there is a parsing problem.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public InputSource resolveEntity(final String publicId, final String systemId)
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

      final URL location = getDTDLocation(publicId);
      if (location == null)
      {
        Log.info("A public ID was given for the document, but it was unknown or invalid.");
        return null;
      }
      return new InputSource(location.openStream());
    }
    catch (IOException ioe)
    {
      Log.warn("Unable to open specified DTD", ioe);
    }
    return null;
  }

  /**
   * Returns a default resolver, which is initialized to redirect the parser to a
   * local copy of the JFreeReport DTDs.
   *
   * @return the default entity resolver.
   */
  public static ParserEntityResolver getDefaultResolver()
  {
    if (singleton == null)
    {
      singleton = new ParserEntityResolver();
    }
    return singleton;
  }
}
