/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: PackageManager.java,v 1.18 2003/11/25 17:26:23 taqua Exp $
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
import java.io.PrintStream;

import org.jfree.report.util.Log;
import org.jfree.report.util.PackageConfiguration;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.Boot;

/**
 * The PackageManager is used to load and configure the modules of JFreeReport.
 * Modules are used to extend the basic capabilities of JFreeReport by providing
 * a simple plugin-interface.
 * <p>
 * Modules provide a simple capability to remove unneeded functionality from the
 * JFreeReport system and to reduce the overall code size. The modularisation provides
 * a very strict way of removing unnecessary dependencies beween the various packages.
 * <p>
 * The package manager can be used to add new modules to the system or to check
 * the existence and state of installed modules.
 *
 * @author Thomas Morgner
 */
public final class PackageManager
{
  /** An internal constant declaring that the specified module was already loaded. */
  private static final int RETURN_MODULE_LOADED = 0;
  /** An internal constant declaring that the specified module is not known. */
  private static final int RETURN_MODULE_UNKNOWN = 1;
  /** An internal constant declaring that the specified module produced an error while loading. */
  private static final int RETURN_MODULE_ERROR = 2;

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

  /**
   * The module configuration instance that should be used to store module
   * properties. This separates the user defined properties from the implementation
   * defined properties.
   */
  private final PackageConfiguration packageConfiguration;

  /** A list of all defined modules. */
  private final ArrayList modules;
  /** A list of module name definitions. */
  private final ArrayList initSections;

  /**
   * DefaultConstructor.
   */
  private PackageManager()
  {
    packageConfiguration = new PackageConfiguration();
    modules = new ArrayList();
    initSections = new ArrayList();
  }

  /**
   * Checks, whether a certain module is available.
   *
   * @param moduleDescription the module description of the desired module.
   * @return true, if the module is available and the version of the module
   * is compatible, false otherwise.
   */
  public boolean isModuleAvailable(final ModuleInfo moduleDescription)
  {
    final PackageState[] packageStates =
        (PackageState[]) modules.toArray(new PackageState[modules.size()]);
    for (int i = 0; i < packageStates.length; i++)
    {
      final PackageState state = packageStates[i];
      if (state.getModule().getModuleClass().equals(moduleDescription.getModuleClass()))
      {
        return (state.getState() == PackageState.STATE_INITIALIZED);
      }
    }
    return false;
  }

  /**
   * Loads all modules mentioned in the report configuration starting with
   * the given prefix. This method is used during the boot process of
   * JFreeReport. You should never need to call this method directly.
   *
   * @param modulePrefix the module prefix.
   */
  public void load (final String modulePrefix)
  {
    if (initSections.contains(modulePrefix))
    {
      return;
    }
    initSections.add(modulePrefix);

    final ReportConfiguration config = ReportConfiguration.getGlobalConfig();
    final Iterator it = config.findPropertyKeys(modulePrefix);
    while (it.hasNext())
    {
      final String key = (String) it.next();
      if (key.endsWith(".Module"))
      {
        addModule(config.getConfigProperty(key));
      }
    }
    Log.debug("Loaded a total of " + modules.size() + " modules under prefix: " + modulePrefix);
  }

  /**
   * Initializes all previously uninitialized modules. Once a module is initialized,
   * it is not re-initialized a second time.
   */
  public synchronized void initializeModules()
  {
    // sort by subsystems and dependency
    PackageSorter.sort(modules);

    for (int i = 0; i < modules.size(); i++)
    {
      final PackageState mod = (PackageState) modules.get(i);
      if (mod.configure())
      {
        Log.debug(new Log.SimpleMessage("Conf: ",
            new Log.PadMessage(mod.getModule().getModuleClass(), 70),
            " [" , mod.getModule().getSubSystem() , "]"));
      }
    }

    for (int i = 0; i < modules.size(); i++)
    {
      final PackageState mod = (PackageState) modules.get(i);
      if (mod.initialize())
      {
        Log.debug(new Log.SimpleMessage("Init: ",
            new Log.PadMessage(mod.getModule().getModuleClass(), 70),
            " [" , mod.getModule().getSubSystem() , "]"));
      }
    }
  }

  /**
   * Adds a module to the package manager.
   * Once all modules are added, you have to call initializeModules()
   * to configure and initialize the new modules.
   *
   * @param modClass the module class
   */
  public synchronized void addModule(final String modClass)
  {
    final ArrayList loadModules = new ArrayList();
    final ModuleInfo modInfo = new DefaultModuleInfo
        (modClass, null, null, null);
    if (loadModule(modInfo, new ArrayList(), loadModules, false))
    {
      for (int i = 0; i < loadModules.size(); i++)
      {
        final Module mod = (Module) loadModules.get(i);
        modules.add(new PackageState(mod));
      }
    }
  }

  /**
   * Checks, whether the given module is already loaded in either the given
   * tempModules list or the global package registry. If tmpModules is null,
   * only the previously installed modules are checked.
   *
   * @param tempModules a list of previously loaded modules.
   * @param module the module specification that is checked.
   * @return true, if the module is already loaded, false otherwise.
   */
  private int containsModule(final ArrayList tempModules, final ModuleInfo module)
  {
    if (tempModules != null)
    {
      final ModuleInfo[] mods = (ModuleInfo[])
          tempModules.toArray(new ModuleInfo[tempModules.size()]);
      for (int i = 0; i < mods.length; i++)
      {
        if (mods[i].getModuleClass().equals(module.getModuleClass()))
        {
          return RETURN_MODULE_LOADED;
        }
      }
    }

    final PackageState[] packageStates =
        (PackageState[]) modules.toArray(new PackageState[modules.size()]);
    for (int i = 0; i < packageStates.length; i++)
    {
      if (packageStates[i].getModule().getModuleClass().equals(module.getModuleClass()))
      {
        if (packageStates[i].getState() == PackageState.STATE_ERROR)
        {
          return RETURN_MODULE_ERROR;
        }
        else
        {
          return RETURN_MODULE_LOADED;
        }
      }
    }
    return RETURN_MODULE_UNKNOWN;
  }

  /**
   * A utility method that collects all failed modules. Such an module caused 
   * an error while being loaded, and is now cached in case it is referenced
   * elsewhere.
   *  
   * @param state the failed module.
   */
  private void dropFailedModule (final PackageState state)
  {
    if (modules.contains(state) == false)
    {
      modules.add(state);
    }
  }

  /**
   * Tries to load a given module and all dependent modules. If the dependency check
   * fails for that module (or for one of the dependent modules), the loaded modules
   * are discarded and no action is taken.
   *
   * @param moduleInfo the module info of the module that should be loaded.
   * @param incompleteModules a list of incompletly loaded modules. This are module
   * specifications which depend on the current module and wait for the module to
   * be completly loaded.
   * @param modules the list of previously loaded modules for this module.
   * @param fatal a flag that states, whether the failure of loading a module should
   * be considered an error. Root-modules load errors are never fatal, as we try
   * to load all known modules, regardless whether they are active or not. 
   * @return true, if the module was loaded successfully, false otherwise.
   */
  private boolean loadModule(final ModuleInfo moduleInfo, final ArrayList incompleteModules,
                             final ArrayList modules, final boolean fatal)
  {
    try
    {
      final Class c = this.getClass().getClassLoader().loadClass(moduleInfo.getModuleClass());
      final Module module = (Module) c.newInstance();

      if (acceptVersion(moduleInfo, module) == false)
      {
        // module conflict!
        Log.warn("Module " + module.getName() + ": required version: " + moduleInfo +
            ", but found Version: \n" + module);
        final PackageState state = new PackageState(module, PackageState.STATE_ERROR);
        dropFailedModule(state);
        return false;
      }

      final int moduleContained = containsModule(modules, module);
      if (moduleContained == RETURN_MODULE_ERROR)
      {
        // the module caused harm before ...
        Log.debug ("Indicated failure for module: " + module.getModuleClass());
        final PackageState state = new PackageState(module, PackageState.STATE_ERROR);
        dropFailedModule(state);
        return false;
      }
      else if (moduleContained == RETURN_MODULE_UNKNOWN)
      {
        if (incompleteModules.contains(module))
        {
          // we assume that loading will continue ...
          Log.error(new Log.SimpleMessage
            ("Circular module reference: This module definition is invalid: ", 
              module.getClass()));
          final PackageState state = new PackageState(module, PackageState.STATE_ERROR);
          dropFailedModule(state);
          return false;
        }
        incompleteModules.add(module);
        final ModuleInfo[] required = module.getRequiredModules();
        for (int i = 0; i < required.length; i++)
        {
          if (loadModule(required[i], incompleteModules, modules, true) == false)
          {
            Log.debug ("Indicated failure for module: " + module.getModuleClass());
            final PackageState state = new PackageState(module, PackageState.STATE_ERROR);
            dropFailedModule(state);
            return false;
          }
        }

        final ModuleInfo[] optional = module.getOptionalModules();
        for (int i = 0; i < optional.length; i++)
        {
          if (loadModule(optional[i], incompleteModules, modules, true) == false)
          {
            Log.debug(new Log.SimpleMessage("Optional module: ",
                optional[i].getModuleClass(), " was not loaded."));
          }
        }
        // maybe a dependent module defined the same base module ...
        if (containsModule(modules, module) == RETURN_MODULE_UNKNOWN)
        {
          modules.add(module);
        }
        incompleteModules.remove(module);
      }
      return true;
    }
    catch (ClassNotFoundException cnfe)
    {
      if (fatal)
      {
        Log.warn(new Log.SimpleMessage
            ("Unresolved dependency for package: ", moduleInfo.getModuleClass()));
      }
      Log.debug(new Log.SimpleMessage ("ClassNotFound: ", cnfe.getMessage()));
      return false;
    }
    catch (Exception e)
    {
      Log.warn(new Log.SimpleMessage("Exception while loading module: ", moduleInfo), e);
      return false;
    }
  }

  /**
   * Checks, whether the given module meets the requirements defined in the module
   * information.
   *
   * @param moduleRequirement the required module specification.
   * @param module the module that should be checked against the specification.
   * @return true, if the module meets the given specifications, false otherwise.
   */
  private boolean acceptVersion(final ModuleInfo moduleRequirement, final Module module)
  {
    if (moduleRequirement.getMajorVersion() == null)
    {
      return true;
    }
    if (module.getMajorVersion() == null)
    {
      Log.warn("Module " + module.getName() + " does not define a major version.");
    }
    else
    {
      int compare = acceptVersion(moduleRequirement.getMajorVersion(),
          module.getMajorVersion());
      if (compare > 0)
      {
        return false;
      }
      else if (compare < 0)
      {
        return true;
      }
    }

    if (moduleRequirement.getMinorVersion() == null)
    {
      return true;
    }
    if (module.getMinorVersion() == null)
    {
      Log.warn("Module " + module.getName() + " does not define a minor version.");
    }
    else
    {
      int compare = acceptVersion(moduleRequirement.getMinorVersion(),
          module.getMinorVersion());
      if (compare > 0)
      {
        return false;
      }
      else if (compare < 0)
      {
        return true;
      }
    }

    if (moduleRequirement.getPatchLevel() == null)
    {
      return true;
    }
    if (module.getPatchLevel() == null)
    {
      Log.debug("Module " + module.getName() + " does not define a patch level.");
    }
    else
    {
      if (acceptVersion(moduleRequirement.getPatchLevel(),
          module.getPatchLevel()) > 0)
      {
        Log.debug ("Did not accept patchlevel: " + moduleRequirement.getPatchLevel() + " - " +
          module.getPatchLevel());
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
   * @return 0, if the dependent module version is equal tothe module's required
   * version, a negative number if the dependent module is newer or a positive
   * number if the dependent module is older and does not fit.
   */
  private int acceptVersion(final String modVer, final String depModVer)
  {
    final int mLength = Math.max(modVer.length(), depModVer.length());
    char[] modVerArray;
    char[] depVerArray;
    if (modVer.length() > depModVer.length())
    {
      modVerArray = modVer.toCharArray();
      depVerArray = new char[mLength];
      final int delta = modVer.length() - depModVer.length();
      Arrays.fill(depVerArray, 0, delta, ' ');
      System.arraycopy(depVerArray, delta, depModVer.toCharArray(), 0, depModVer.length());
    }
    else if (modVer.length() < depModVer.length())
    {
      depVerArray = depModVer.toCharArray();
      modVerArray = new char[mLength];
      final char[] b1 = new char[mLength];
      final int delta = depModVer.length() - modVer.length();
      Arrays.fill(b1, 0, delta, ' ');
      System.arraycopy(b1, delta, modVer.toCharArray(), 0, modVer.length());
    }
    else
    {
      depVerArray = depModVer.toCharArray();
      modVerArray = modVer.toCharArray();
    }
    return new String(modVerArray).compareTo(new String (depVerArray));
  }

  /**
   * Returns the default package configuration. Private report configuration
   * instances may be inserted here. These inserted configuration can never override
   * the settings from this package configuration.
   *
   * @return the package configuration.
   */
  public PackageConfiguration getPackageConfiguration()
  {
    return packageConfiguration;
  }

  /**
   * Returns an array of the currently active modules. The module definition
   * returned contain all known modules, including buggy and unconfigured
   * instances.
   *
   * @return the modules.
   */
  public Module[] getAllModules ()
  {
    final Module[] mods = new Module[modules.size()];
    for (int i = 0; i < modules.size(); i++)
    {
      final PackageState state = (PackageState) modules.get(i);
      mods[i] = state.getModule();
    }
    return mods;
  }

  /**
   * Returns all active modules. This array does only contain modules
   * which were successfully configured and initialized.
   *
   * @return the list of all active modules.
   */
  public Module[] getActiveModules ()
  {
    final ArrayList mods = new ArrayList();
    for (int i = 0; i < modules.size(); i++)
    {
      final PackageState state = (PackageState) modules.get(i);
      if (state.getState() == PackageState.STATE_INITIALIZED)
      {
        mods.add(state.getModule());
      }
    }
    return (Module[]) mods.toArray(new Module[mods.size()]);
  }

  public void printUsedModules (PrintStream p)
  {
    Module[] allMods = PackageManager.getInstance().getAllModules();
    ArrayList activeModules = new ArrayList();
    ArrayList failedModules = new ArrayList();

    for (int i = 0; i < allMods.length; i++)
    {
      if (isModuleAvailable(allMods[i]))
      {
        activeModules.add (allMods[i]);
      }
      else
      {
        failedModules.add (allMods[i]);
      }
    }

    p.print("Active modules: ");
    p.println(activeModules.size());
    p.println("----------------------------------------------------------");
    for (int i = 0; i < activeModules.size(); i++)
    {
      Module mod = (Module) activeModules.get(i);
      p.print(new Log.PadMessage(mod.getModuleClass(), 70));
      p.print(" [");
      p.print(mod.getSubSystem());
      p.println("]");
      p.print("  Version: ");
      p.print(mod.getMajorVersion());
      p.print("-");
      p.print(mod.getMinorVersion());
      p.print("-");
      p.print(mod.getPatchLevel());
      p.print (" Producer: ");
      p.println(mod.getProducer());
      p.print("  Description: ");
      p.println(mod.getDescription());
    }
  }

  public static void main (String [] args)
  {
    Boot.start();
    PackageManager.getInstance().printUsedModules(System.out);
  }
}
