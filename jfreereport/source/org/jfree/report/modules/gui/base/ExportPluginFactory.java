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
 * ------------------------
 * ExportPluginFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExportPluginFactory.java,v 1.4 2003/07/23 16:02:19 taqua Exp $
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

/**
 * An export plug-in factory.
 *
 * @author Thomas Morgner.
 */
public class ExportPluginFactory
{
  private static class PluginDefinition implements Comparable
  {
    private Class pluginClass;
    private String preference;
    private String enableKey;

    public PluginDefinition(Class pluginClass, String preference, String enableKey)
    {
      if (pluginClass == null)
      {
        throw new NullPointerException("PluginClass is null.");
      }
      if (enableKey == null)
      {
        throw new NullPointerException("PluginClass is null.");
      }
      if (preference == null)
      {
        throw new NullPointerException("PluginClass is null.");
      }
      this.pluginClass = pluginClass;
      this.enableKey = enableKey;
      this.preference = preference;
    }

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

    public int hashCode()
    {
      return pluginClass.hashCode();
    }

    public Class getPluginClass()
    {
      return pluginClass;
    }

    public String getPreference()
    {
      return preference;
    }

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

  private static ExportPluginFactory factory;

  public static ExportPluginFactory getInstance()
  {
    if (factory == null)
    {
      factory = new ExportPluginFactory();
    }
    return factory;
  }

  private ArrayList exportPlugins;

  protected ExportPluginFactory()
  {
    exportPlugins = new ArrayList();
  }

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
  public ArrayList createExportPlugIns(final PreviewProxy proxy, final ReportConfiguration config)
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
