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
 * PackageManager.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PackageManager.java,v 1.2 2003/07/11 18:33:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26-Jun-2003 : Initial version
 *
 */

package org.jfree.report.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;

import org.jfree.report.util.Log;
import org.jfree.report.util.PackageConfiguration;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.JFreeReport;

/**
 * This class will help to manage the extension classes of JFreeReport 0.8.4 and
 * later...
 *
 * @author Thomas Morgner
 */
public class PackageManager
{
  /** A singleton instance of the package manager. */
  private static PackageManager singleton;

  /**
   * Returns the singleton instance of the package manager.
   *
   * @return the package manager.
   */
  public static PackageManager getInstance()
  {
    if (singleton == null)
    {
      singleton = new PackageManager();
    }
    return singleton;
  }

  private PackageConfiguration packageConfiguration;
  private ArrayList modules;
  private ArrayList initSections;

  /**
   * DefaultConstructor.
   */
  private PackageManager()
  {
    packageConfiguration = new PackageConfiguration();
    modules = new ArrayList();
    initSections = new ArrayList();
  }

  public synchronized void init ()
  {
    init("org.jfree.report.modules.");
    init("org.jfree.report.ext.modules.");
  }

  public synchronized void init (String modulePrefix)
  {
    if (initSections.contains(modulePrefix))
    {
      return;
    }
    initSections.add(modulePrefix);

    ReportConfiguration config = ReportConfiguration.getGlobalConfig();
    Iterator it = config.findPropertyKeys(modulePrefix);
    while (it.hasNext())
    {
      String key = (String) it.next();
      if (key.endsWith(".Module"))
      {
        addModule(config.getConfigProperty(key));
      }
      else
      {
        Log.debug ("Other Key: " + key);
      }
    }
    Log.debug ("Loaded a total of " + modules.size() + " modules.");
    initializeModules();
  }

  public synchronized void initializeModules ()
  {
    Collections.sort(modules);

    for (int i = 0; i < modules.size(); i++)
    {
      PackageState mod = (PackageState) modules.get(i);
      if (mod.configure())
      {
        Log.debug ("Conf: " + mod.getModule().getModuleClass());
      }
    }
    for (int i = 0; i < modules.size(); i++)
    {
      PackageState mod = (PackageState) modules.get(i);
      if (mod.initialize())
      {
        Log.debug ("Init: " + mod.getModule().getModuleClass());
      }
    }
  }

  public synchronized void addModule (String modClass)
  {
    ArrayList loadModules = new ArrayList();
    ModuleInfo modInfo = new DefaultModuleInfo
        (modClass, null, null, null);
    if (loadModule(modInfo, loadModules))
    {
      for (int i = 0; i < loadModules.size(); i++)
      {
        Module mod = (Module) loadModules.get(i);
        modules.add (new PackageState(mod));
        Log.debug (mod.getModuleClass());
      }
    }
  }

  private boolean containsModule (ArrayList tempModules, ModuleInfo module)
  {
    ModuleInfo[] mods = (ModuleInfo[])
        tempModules.toArray(new ModuleInfo[tempModules.size()]);
    for (int i = 0; i < mods.length; i++)
    {
      if (mods[i].getModuleClass().equals(module.getModuleClass()))
      {
        return true;
      }
    }

    PackageState[] packageStates =
        (PackageState[]) modules.toArray(new PackageState[modules.size()]);
    for (int i = 0; i < packageStates.length; i++)
    {
      if (packageStates[i].getModule().getModuleClass().equals(module.getModuleClass()))
      {
        return true;
      }
    }
    return false;
  }
  private boolean loadModule (ModuleInfo moduleInfo, ArrayList modules)
  {
    try
    {
      Class c = this.getClass().getClassLoader().loadClass(moduleInfo.getModuleClass());
      Module module = (Module) c.newInstance();

      if (acceptVersion(moduleInfo, module) == false)
      {
        // module conflict!
        Log.debug ("Module " + module.getName() + ": required version: " + moduleInfo +
            ", but found Version: " + module);
        return false;
      }

      if (containsModule(modules, module) == false)
      {
        ModuleInfo[] required = module.getRequiredModules();
        for (int i = 0; i < required.length; i++)
        {
          if (loadModule(required[i], modules) == false)
          {
            return false;
          }
        }

        ModuleInfo[] optional = module.getOptionalModules();
        for (int i = 0; i < optional.length; i++)
        {
          if (loadModule(optional[i], modules) == false)
          {
            Log.debug (new Log.SimpleMessage("Optional module: ",
                optional[i].getModuleClass(), " was not loaded."));
          }
        }
        // maybe a dependent module defined the same base module ...
        if (containsModule(modules, module) == false)
        {
          modules.add (module);
        }
      }
      return true;
    }
    catch (ClassNotFoundException cnfe)
    {
      Log.warn (new Log.SimpleMessage("Unresolved dependency for package: ", moduleInfo.getModuleClass()));
      return false;
    }
    catch (Exception e)
    {
      Log.debug (new Log.SimpleMessage("Exception while loading module: ", moduleInfo), e);
      return false;
    }
  }

  private boolean acceptVersion (ModuleInfo moduleRequirement, Module module)
  {
    if (moduleRequirement.getMajorVersion() == null)
    {
      return true;
    }
    if (module.getMajorVersion() == null)
    {
      Log.warn ("Module " + module.getName() + " does not define a major version.");
    }
    else
    {
      if (acceptVersion(moduleRequirement.getMajorVersion(),
          module.getMajorVersion()) == false)
      {
        return false;
      }
    }

    if (moduleRequirement.getMinorVersion() == null)
    {
      return true;
    }
    if (module.getMinorVersion() == null)
    {
      Log.warn ("Module " + module.getName() + " does not define a minor version.");
    }
    else
    {
      if (acceptVersion(moduleRequirement.getMinorVersion(),
          module.getMinorVersion()) == false)
      {
        return false;
      }
    }

    if (moduleRequirement.getPatchLevel() == null)
    {
      return true;
    }
    if (module.getPatchLevel() == null)
    {
      Log.debug ("Module " + module.getName() + " does not define a patch level.");
    }
    else
    {
      if (acceptVersion(moduleRequirement.getPatchLevel(),
          module.getPatchLevel()) == false)
      {
        return false;
      }
    }
    return true;

  }

  /**
   * Compare the version strings. If the strings have a different length,
   * the shorter string is padded with spaces to make them compareable.
   *
   * @param modVer the version string of the module
   * @param depModVer the version string of the dependent or optional module
   * @return true, if the dependent module version is greater or equal than
   * the modules required version.
   */
  private boolean acceptVersion (String modVer, String depModVer)
  {
    int mLength = Math.max (modVer.length(), depModVer.length());
    if (modVer.length() > depModVer.length())
    {
      char[] b2 = new char[mLength];
      int delta = modVer.length() - depModVer.length();
      Arrays.fill(b2, 0, delta, ' ');
      System.arraycopy(b2, delta, depModVer.toCharArray(), 0, depModVer.length());
      return (new String (b2).compareTo(modVer) <= 0);
    }
    else if (modVer.length() < depModVer.length())
    {
      char[] b1 = new char[mLength];
      int delta = depModVer.length() - modVer.length();
      Arrays.fill(b1, 0, delta, ' ');
      System.arraycopy(b1, delta, modVer.toCharArray(), 0, modVer.length());
      return (new String (b1).compareTo(depModVer) <= 0);
    }
    else
    {
      return (depModVer.compareTo(modVer) <= 0);
    }
  }

  /**
   * Returns the default package configuration. Private report configuration
   * instances may be inserted. These inserted configuration can never override
   * the settings from this package configuration.
   *
   * @return
   */
  public PackageConfiguration getPackageConfiguration ()
  {
    return packageConfiguration;
  }

  public static void main(String[] args)
  {
    new JFreeReport();
    //ReportConfiguration.getGlobalConfig();
    //PackageManager.getInstance().init();
  }
}
