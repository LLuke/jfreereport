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
 * ModuleNodeFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ModuleNodeFactory.java,v 1.1 2003/08/31 19:31:22 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 30.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;

import org.jfree.report.Boot;
import org.jfree.report.JFreeReportCoreModule;
import org.jfree.report.modules.Module;
import org.jfree.report.modules.PackageManager;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.xml.sax.SAXException;

public class ModuleNodeFactory
{
  private static class ModuleSorter implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second.
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      String name1;
      String name2;

      if (o1.getClass().getPackage() == null || o2.getClass().getPackage() == null)
      {
        name1 = getPackage(o1.getClass());
        name2 = getPackage(o2.getClass());
      }
      else
      {
        name1 = o1.getClass().getPackage().getName();
        name2 = o2.getClass().getPackage().getName();
      }
      if (name1.length() < name2.length())
      {
        return -1;
      }
      if (name1.length() > name2.length())
      {
        return 1;
      }
      return 0;
    }
  }

  private Module[] activeModules;
  private ArrayList globalNodes;
  private ArrayList localNodes;
  private Hashtable configEntryLookup;

  private ModuleNodeFactory ()
  {
    Boot.start();
    PackageManager pm = PackageManager.getInstance();
    activeModules = pm.getAllModules();
    Arrays.sort(activeModules, new ModuleSorter());
    globalNodes = new ArrayList();
    localNodes = new ArrayList();
    configEntryLookup = new Hashtable();

  }

  public ModuleNodeFactory (InputStream in) throws IOException
  {
    this();
    ConfigDescriptionModel model = new ConfigDescriptionModel();
    try
    {
      model.load(in);
    }
    catch (SAXException saxException)
    {
      Log.error ("Failed to parse the model description.", saxException);
      throw new IOException("Failed to parse the model description:" + saxException.getMessage());
    }
    catch (ParserConfigurationException pE)
    {
      Log.error ("Failed to configure the xml parser.", pE);
      throw new IOException("Failed to configure the xml parser:" + pE.getMessage());
    }

    ConfigDescriptionEntry[] entries = model.toArray();
    for (int i = 0; i < entries.length; i++)
    {
      //Log.debug ("Entry: " + entries[i].getKeyName() + " registered");
      configEntryLookup.put(entries[i].getKeyName(), entries[i]);
    }
  }

  public void init (ReportConfiguration config) throws ConfigTreeModelException
  {
    //Iterator enum = config.findPropertyKeys("");
    Enumeration enum = configEntryLookup.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      processKey(key, config);
    }
  }

  private void processKey (String key, ReportConfiguration config) throws ConfigTreeModelException
  {
    ConfigDescriptionEntry cde = (ConfigDescriptionEntry) configEntryLookup.get(key);

    Module mod = lookupModule(key);
    //Log.debug ("ActiveModule: " + mod.getClass() + " for key " + key);
    if (cde == null)
    {
      // create an default entry on the fly ...
      // cde = new TextConfigDescriptionEntry(key);

      // if no definition was found, we have to assume that the config
      // property is not editable by this editor.
      // (this filters the System-properties out of the way)
      Log.debug ("Ignored key: " + key);
      return;
    }
    if (cde.isGlobal() == false)
    {
      ConfigTreeModuleNode node = lookupNode(mod, localNodes);
      if (node == null)
      {
        node = new ConfigTreeModuleNode(mod, config);
        localNodes.add(node);
      }
      node.addAssignedKey(cde);
    }

    // The global configuration provides defaults for the local
    // settings...
    ConfigTreeModuleNode node = lookupNode(mod, globalNodes);
    if (node == null)
    {
      node = new ConfigTreeModuleNode(mod, config);
      globalNodes.add(node);
    }
    node.addAssignedKey(cde);
  }

  private ConfigTreeModuleNode lookupNode (Module key, ArrayList nodeList)
  {
    for (int i = 0; i < nodeList.size(); i++)
    {
      ConfigTreeModuleNode node = (ConfigTreeModuleNode) nodeList.get(i);
      if (key == node.getModule())
      {
        return node;
      }
    }
    return null;
  }

  public static String getPackage (Class c)
  {
    String className = c.getName();
    int idx = className.lastIndexOf('.');
    if (idx <= 0)
    {
      return "";
    }
    else
    {
      return className.substring(0, idx);
    }
  }

  private Module lookupModule (String key) throws ConfigTreeModelException
  {
    Module fallback = null;
    for (int i = 0; i < activeModules.length; i++)
    {
      if (activeModules[i].getClass().equals(JFreeReportCoreModule.class))
      {
        fallback = activeModules[i];
      }
      else
      {
        String modPackage = getPackage(activeModules[i].getClass());
        // Log.debug ("Module package: " + modPackage + " for " + activeModules[i].getClass());
        if (key.startsWith(modPackage))
        {
          return activeModules[i];
        }
      }
    }
    if (fallback == null)
    {
      throw new ConfigTreeModelException("Core module is not registered.");
    }
    return fallback;
  }

  public ArrayList getGlobalNodes()
  {
    return globalNodes;
  }

  public ArrayList getLocalNodes()
  {
    return localNodes;
  }

  public static void main (String[] args) throws Exception
  {
    ModuleNodeFactory factory = new ModuleNodeFactory();
    factory.init(ReportConfiguration.getGlobalConfig());
  }
}
