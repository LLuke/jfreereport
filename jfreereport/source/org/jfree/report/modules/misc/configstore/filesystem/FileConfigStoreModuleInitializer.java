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
 * ------------------------------
 * FileConfigStoreModuleInitializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FileConfigStoreModuleInitializer.java,v 1.1 2003/07/14 17:37:07 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.misc.configstore.filesystem;

import java.io.File;

import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.ModuleInitializer;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.util.ReportConfiguration;

public class FileConfigStoreModuleInitializer implements ModuleInitializer
{
  public static final String USER_BASEDIR_CONFIG_KEY =
      "org.jfree.report.modules.misc.configstore.filesystem.UserTargetDir";

  public static final String SYSTEM_BASEDIR_CONFIG_KEY =
      "org.jfree.report.modules.misc.configstore.filesystem.SystemTargetDir";

  public FileConfigStoreModuleInitializer()
  {
  }

  public void performInit() throws ModuleInitializeException
  {
    String userBaseDirectory =
        ReportConfiguration.getGlobalConfig().getConfigProperty
        (USER_BASEDIR_CONFIG_KEY, "~/.jfreereport/user");

    String systemBaseDirectory =
        ReportConfiguration.getGlobalConfig().getConfigProperty
        (SYSTEM_BASEDIR_CONFIG_KEY, "~/.jfreereport/system");

    ConfigFactory factory = ConfigFactory.getInstance();
    factory.defineUserStorage(new FileConfigStorage(getStoragePath(userBaseDirectory)));
    factory.defineUserStorage(new FileConfigStorage(getStoragePath(systemBaseDirectory)));
  }

  private File getStoragePath (String baseDirectory) throws ModuleInitializeException
  {
    File baseDirectoryFile = null;

    if (baseDirectory.startsWith("~/") == false)
    {
      baseDirectoryFile = new File(baseDirectory);
    }
    else
    {
      try
      {
        String homeDirectory = System.getProperty("user.home");
        if (baseDirectory.equals("~/"))
        {
          baseDirectoryFile = new File(homeDirectory);
        }
        else
        {
          baseDirectory = "." + baseDirectory.substring(1);
          baseDirectoryFile = new File(homeDirectory, baseDirectory);
        }
      }
      catch (Exception e)
      {
        throw new ModuleInitializeException
            ("Failed to create the file config storage.", e);
      }
    }

    if (baseDirectoryFile.exists() == false)
    {
      if (baseDirectoryFile.mkdirs() == false)
      {
        throw new ModuleInitializeException
            ("Unable to create the specified directory.");
      }
    }
    else
    {
      if ((baseDirectoryFile.canRead() && baseDirectoryFile.canWrite()) == false)
      {
        throw new ModuleInitializeException
            ("Unable to access the specified directory.");
      }
    }
    return baseDirectoryFile;
  }
}
