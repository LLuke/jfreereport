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
 * ResourceLoader.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ResourceLoader.java,v 1.1.1.1 2006/04/17 16:48:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader;

import java.util.Map;

/**
 * A resource loader knows how to get binary rawdata from a location specified
 * by an resource key.
 *
 * @author Thomas Morgner
 */
public interface ResourceLoader
{
  public String getSchema();
  public boolean isSupportedKeyValue (Map values);

  public ResourceKey createKey (Map values) throws ResourceKeyCreationException;
  public ResourceKey deriveKey (ResourceKey parent, Map data) throws ResourceKeyCreationException;

  public ResourceData load (ResourceKey key) throws ResourceLoadingException;

  public void setResourceManager (ResourceManager manager);
}
