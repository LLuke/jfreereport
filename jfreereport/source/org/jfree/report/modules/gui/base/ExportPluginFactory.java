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
 * ------------------------
 * ExportPluginFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExportPluginFactory.java,v 1.7 2003/08/22 20:27:20 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.base;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Worker;

/**
 * An export plug-in factory. This factory is used to collect all available
 * export plugins and to make them avaiable to the preview components.
 *
 * @author Thomas Morgner.
 */
public class ExportPluginFactory
{
  /**
   * A class to manage the plugin module definitions.
   * 
   * @author Thomas Morgner
   */
  private static class PluginDefinition implements Comparable
  {
    /** The class of the export plugin implementation. */
    private Class pluginClass;
    /** The preference string (used to sort the modules in the menu). */
    private String preference;
    /** The configuration key that controls whether a module is visible. */
    private String enableKey;

    /**
     * Creates a new plugin definition.
     * 
     * @param pluginClass the plugin class that should be defined.
     * @param preference the preference of the class in the menu.
     * @param enableKey the report configuration key that triggers the visiblity 
     * of the plugin.
     */
    public PluginDefinition(Class pluginClass, String preference, String enableKey)
    {
      if (pluginClass == null)
      {
        throw new NullPointerException("PluginClass is null.");
      }
      if (enableKey == null)
      {
        throw new NullPointerException("EnableKey is null.");
      }
      if (preference == null)
      {
        throw new NullPointerException("Preference is null.");
      }
      this.pluginClass = pluginClass;
      this.enableKey = enableKey;
      this.preference = preference;
    }

    /**
     * Checks whether this plugin definition is equal to the given object.
     * The object will be considered equal if it is a plugin definition pointing
     * to the same export plugin.
     *  
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     * @param o the object to compare
     * @return true, if the object is equal, false otherwise.
     */
    public boolean equals(Object o)
    {
      if (this == o)
      { 
        return true;
      }
      if (!(o instanceof PluginDefinition))
      { 
        return false;
      }

      final PluginDefinition pluginDefinition = (PluginDefinition) o;

      if (!pluginClass.equals(pluginDefinition.pluginClass))
      {
        return false;
      }

      return true;
    }

    /**
     * Computes an hashcode for this export plugin. 
     * @see java.lang.Object#hashCode()
     * 
     * @return the computed hashcode.
     */
    public int hashCode()
    {
      return pluginClass.hashCode();
    }

    /**
     * Returns the export plugin class defined for this plugin definition.
     * 
     * @return the export plugin class.
     */
    public Class getPluginClass()
    {
      return pluginClass;
    }

    /**
     * Returns the preference of the plugin in the menu. The preference is used
     * to order the export plugins.
     * 
     * @return the preference of the plugin in the menu
     */
    public String getPreference()
    {
      return preference;
    }

    /**
     * Returns the enable key of the report configuration which defines whether this
     * plugin will be visible.
     * 
     * @return the enable key.
     */
    public String getEnableKey()
    {
      return enableKey;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     *
     * @param   o the Object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     */
    public int compareTo(Object o)
    {
      if (this == o)
      { 
        return 0;
      }
      PluginDefinition def = (PluginDefinition) o;
      return getPreference().compareTo(def.getPreference());
    }
  }

  /** The singleton instance of this factory. */
  private static ExportPluginFactory factory;

  /**
   * Returns the singleton instance of the export plugin factory.
   * 
   * @return the factory instance
   */
  public static ExportPluginFactory getInstance()
  {
    if (factory == null)
    {
      factory = new ExportPluginFactory();
    }
    return factory;
  }

  /** The list of all known export plugins. */ 
  private ArrayList exportPlugins;

  /**
   * DefaultConstructor. Defines a new export plugin factory.
   *
   */
  protected ExportPluginFactory()
  {
    exportPlugins = new ArrayList();
  }

  /**
   * Registers the given plugin in this factory. 
   * 
   * @param plugin the implementing class of the export plugin
   * @param preference the preference in the menu
   * @param enableKey the enable key of the export plugin to trigger the visiblity
   */
  public void registerPlugin (Class plugin, String preference, String enableKey)
  {
    if (ExportPlugin.class.isAssignableFrom(plugin))
    {
      PluginDefinition def = new PluginDefinition(plugin, preference, enableKey);
      if (exportPlugins.contains(def) == false)
      {
        exportPlugins.add(def);
      }
    }
  }

  /**
   * Loads and instatiaties an export plug-in.
   *
   * @param proxy  the preview proxy.
   * @param plugin the class of the export plugin.
   * @return The plug-in.
   */
  protected ExportPlugin createPlugIn(final PreviewProxy proxy, final Class plugin)
  {
    if (proxy == null)
    {
      throw new NullPointerException("PreviewProxy must not be null.");
    }
    try
    {
      final ExportPlugin ep = (ExportPlugin) plugin.newInstance();
      ep.init(proxy);
      return ep;
    }
    catch (Exception e)
    {
      Log.warn("Unable to create the export plugin: " + plugin.getName(), e);
      return null;
    }
  }

  /**
   * Returns true if the plug-in is enabled for a given report configuration, and false otherwise.
   *
   * @param config  the report configuration.
   * @param pluginKey  the plug-in enable key.
   *
   * @return A boolean.
   */
  protected boolean isPluginEnabled(final ReportConfiguration config, final String pluginKey)
  {
    return config.getConfigProperty(pluginKey, "false").equals("true");
  }

  /**
   * Creates a list containing all available export plugins.
   *
   * @param proxy  the preview proxy.
   * @param config  the report configuration.
   *
   * @return  The list of export plugins.
   */
  public ArrayList createExportPlugIns
      (final PreviewProxy proxy, final ReportConfiguration config, final Worker worker)
  {
    PluginDefinition[] def = (PluginDefinition[])
        exportPlugins.toArray(new PluginDefinition[exportPlugins.size()]);

    Arrays.sort(def);
    ArrayList retval = new ArrayList();

    for (int i = 0; i < def.length; i++)
    {
      PluginDefinition definition = def[i];
      if (isPluginEnabled(config, definition.getEnableKey()))
      {
        final ExportPlugin ep = createPlugIn(proxy, definition.getPluginClass());
        if (ep != null)
        {
          ep.defineWorker(worker);
          retval.add(ep);
        }
      }
      else
      {
        Log.debug(new Log.SimpleMessage("Plugin ", definition.getPluginClass(), 
                  " is not enabled."));
      }
    }
    return retval;
  }
}
