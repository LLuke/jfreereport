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
 * PackageSorter.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PackageSorter.java,v 1.5 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.07.2003 : Initial version
 *
 */

package org.jfree.report.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.util.Log;

/**
 * Compares two modules for order. A module is considered less than an other
 * module if the module is a required module of the compared module. Modules
 * are considered equal if they have no relation.
 *
 * @author Thomas Morgner
 */
public final class PackageSorter
{
  private static class SortModule implements Comparable
  {
    private int position;
    private PackageState state;
    private ArrayList dependSubsystems;
    // direct dependencies, indirect ones are handled by the
    // dependent classes ...

    public SortModule(PackageState state)
    {
      this.position = -1;
      this.state = state;
    }

    public ArrayList getDependSubsystems()
    {
      return dependSubsystems;
    }

    public void setDependSubsystems(ArrayList dependSubsystems)
    {
      this.dependSubsystems = dependSubsystems;
    }

    public int getPosition()
    {
      return position;
    }

    public void setPosition(int position)
    {
      this.position = position;
    }

    public PackageState getState()
    {
      return state;
    }

    public String toString ()
    {
      return (position + " " + state.getModule().getName() + " " + state.getModule().getModuleClass());
    }

    public int compareTo(Object o)
    {
      SortModule otherModule = (SortModule) o;
      if (position > otherModule.position)
      {
        return +1;
      }
      if (position < otherModule.position)
      {
        return -1;
      }
      return 0;
    }
  }

  /**
   * DefaultConstructor.
   */
  private PackageSorter()
  {
  }

  public static void sort (List modules)
  {
    HashMap moduleMap = new HashMap();
    ArrayList errorModules = new ArrayList();
    ArrayList weightModules = new ArrayList();

    for (int i = 0; i < modules.size(); i++)
    {
      PackageState state = (PackageState) modules.get(i);
      if (state.getState() == PackageState.STATE_ERROR)
      {
        errorModules.add (state);
      }
      else
      {
        SortModule mod = new SortModule(state);
        weightModules.add (mod);
        moduleMap.put(state.getModule().getModuleClass(), mod);
      }
    }

    SortModule[] weigths = (SortModule[])
        weightModules.toArray(new SortModule[weightModules.size()]);

    for (int i = 0; i < weigths.length; i++)
    {
      SortModule sortMod = weigths[i];
      sortMod.setDependSubsystems
          (collectSubsystemModules(sortMod.getState().getModule(),
              moduleMap));
    }


    // repeat the computation until all modules have a matching
    // position. This is not the best algorithm, but it works
    // and is relativly simple. It will need some optimizations
    // in the future, but as this is only executed once, we don't
    // have to care much about it.
    boolean doneWork = true;
    while (doneWork)
    {
      doneWork = false;
      for (int i = 0; i < weigths.length; i++)
      {
        SortModule mod = weigths[i];
        int position = searchModulePosition(mod, moduleMap);
        if (position != mod.getPosition())
        {
          mod.setPosition(position);
          doneWork = true;
        }
      }
    }

    Arrays.sort(weigths);
    modules.clear();
    for (int i = 0; i < weigths.length; i++)
    {
      modules.add (weigths[i].getState());
    }
    for (int i = 0; i < errorModules.size(); i++)
    {
      modules.add (errorModules.get(i));
    }
  }

  /**
   * Computes the new module position. This position is computed
   * according to the dependent modules and subsystems. The returned
   * position will be higher than the highest dependent module position.
   *
   * @param smodule the sort module for that we compute the new positon.
   * @param moduleMap the map with all modules.
   * @return the new positon.
   */
  private static int searchModulePosition
      (SortModule smodule, HashMap moduleMap)
  {
    Module module = smodule.getState().getModule();
    int position = 0;

    // check the required modules. Increase our level to at least
    // one point over the highest dependent module
    // ignore missing modules.
    ModuleInfo[] modInfo = module.getOptionalModules();
    for (int modPos = 0; modPos < modInfo.length; modPos++)
    {
      String moduleName = modInfo[modPos].getModuleClass();
      SortModule reqMod = (SortModule) moduleMap.get(moduleName);
      if (reqMod == null)
      {
        continue;
      }
      if (reqMod.getPosition() >= position)
      {
        position = reqMod.getPosition() + 1;
      }
    }

    // check the required modules. Increase our level to at least
    // one point over the highest dependent module
    // there are no missing modules here (or the package manager
    // is invalid)
    modInfo = module.getRequiredModules();
    for (int modPos = 0; modPos < modInfo.length; modPos++)
    {
      String moduleName = modInfo[modPos].getModuleClass();
      SortModule reqMod = (SortModule) moduleMap.get(moduleName);
      if (reqMod.getPosition() >= position)
      {
        position = reqMod.getPosition() + 1;
      }
    }

    // check the subsystem dependencies. This way we make sure
    // that subsystems are fully initialized before we try to use
    // them.
    String subSystem = module.getSubSystem();
    Iterator it = moduleMap.values().iterator();
    while (it.hasNext())
    {
      SortModule mod = (SortModule) it.next();
      // it is evil to compute values on ourself...
      if (mod.getState().getModule() == module)
      {
        // same module ...
        continue;
      }
      Module subSysMod = mod.getState().getModule();
      // if the module we check is part of the same subsystem as
      // we are, then we dont do anything. Within the same subsystem
      // the dependencies are computed solely by the direct references.
      if (subSystem.equals(subSysMod.getSubSystem()))
      {
        // same subsystem ... ignore
        continue;
      }

      // does the module from the global list <mod> depend on the
      // subsystem we are part of?
      //
      // if yes, we have a relation and may need to adjust the level...
      if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))
      {
        // check whether the module is a base module of the given
        // subsystem. We will not adjust our position in that case,
        // as this would lead to an infinite loop
        if (isBaseModule(subSysMod, module) == false)
        {
          if (mod.getPosition() >= position)
          {
            position = mod.getPosition() + 1;
          }
        }
      }
    }
    return position;
  }

  /**
   * Checks, whether a module is a base module of an given module.
   *
   * @param mod the module which to check
   * @param mi the module info of the suspected base module.
   * @return true, if the given module info describes a base module of the
   * given module, false otherwise.
   */
  private static boolean isBaseModule(final Module mod, final ModuleInfo mi)
  {
    ModuleInfo[] info = mod.getRequiredModules();
    for (int i = 0; i < info.length; i++)
    {
      if (info[i].getModuleClass().equals(mi.getModuleClass()))
      {
        return true;
      }
    }
    info = mod.getOptionalModules();
    for (int i = 0; i < info.length; i++)
    {
      if (info[i].getModuleClass().equals(mi.getModuleClass()))
      {
        return true;
      }
    }
    return false;
  }


  /**
   * Checks, whether a module is a base module of an given module.
   *
   * @param childMod the module which to check
   */
  private static ArrayList collectSubsystemModules
      (final Module childMod, final HashMap moduleMap)
  {
    ArrayList collector = new ArrayList();
    ModuleInfo[] info = childMod.getRequiredModules();
    for (int i = 0; i < info.length; i++)
    {
      SortModule dependentModule = (SortModule)
          moduleMap.get(info[i].getModuleClass());
      if (dependentModule == null)
      {
        Log.warn ("A dependent module was not found in the list of known modules." + info[i].getModuleClass());
        continue;
      }

      collector.add (dependentModule.getState().getModule().getSubSystem());
    }

    info = childMod.getOptionalModules();
    for (int i = 0; i < info.length; i++)
    {
      Module dependentModule = (Module)
          moduleMap.get(info[i].getModuleClass());
      if (dependentModule == null)
      {
        Log.warn ("A dependent module was not found in the list of known modules.");
        continue;
      }
      collector.add (dependentModule.getSubSystem());
    }
    return collector;
  }
}
