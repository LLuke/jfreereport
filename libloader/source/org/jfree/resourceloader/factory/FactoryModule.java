/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * ------------
 * FactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.factory;

import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKey;

/**
 * Creation-Date: 05.04.2006, 17:00:33
 *
 * @author Thomas Morgner
 */
public interface FactoryModule
{
  public static final int RECOGNIZED_FINGERPRINT = 4000;
  public static final int RECOGNIZED_CONTENTTYPE = 2000;
  public static final int RECOGNIZED_FILE = 1000;
  /** A default handler does not reject the content. */
  public static final int FEELING_LUCKY = 0;
  public static final int REJECTED = -1;

  public int canHandleResource (ResourceData data) throws
          ResourceCreationException, ResourceLoadingException;
  public int getHeaderFingerprintSize ();
  public Resource create (ResourceData data, ResourceKey context) throws ResourceCreationException,
          ResourceLoadingException;
}
