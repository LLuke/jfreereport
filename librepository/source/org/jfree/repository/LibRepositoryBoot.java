/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * LibRepositoryBoot.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.repository;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 13.11.2006, 11:27:20
 *
 * @author Thomas Morgner
 */
public class LibRepositoryBoot extends AbstractBoot
{
  public static final String REPOSITORY_DOMAIN = "org.jfree.repository";
  public static final String SIZE_ATTRIBUTE = "size";
  public static final String VERSION_ATTRIBUTE = "version";
  public static final String CONTENT_TYPE = "content-type";

  private static LibRepositoryBoot instance;

  public static synchronized LibRepositoryBoot getInstance()
  {
    if (instance == null)
    {
      instance = new LibRepositoryBoot();
    }
    return instance;
  }

  private LibRepositoryBoot()
  {
  }

  protected Configuration loadConfiguration()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/repository/librepository.properties",
             "/librepository.properties", true);

  }

  protected void performBoot()
  {
  }

  protected BootableProjectInfo getProjectInfo()
  {
    return LibRepositoryInfo.getInstance();
  }
}
