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
 * $Id: ExportPluginFactory.java,v 1.10 2003/06/29 16:59:27 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.base;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

/**
 * An export plug-in factory.
 *
 * @author Thomas Morgner.
 */
public class ExportPluginFactory
{
  /** The plug-in enable prefix. */
  public static final String PLUGIN_ENABLE_PREFIX = "org.jfree.report.preview.plugin.";


  /**
   * Loads and instatiaties an export plug-in.
   *
   * @param proxy  the preview proxy.
   * @param className the class name of the export plugin.
   * @return The plug-in.
   */
  protected ExportPlugin createPlugIn(final PreviewProxy proxy, final String className)
  {
    if (proxy == null)
    {
      throw new NullPointerException("PreviewProxy must not be null.");
    }
    try
    {
      final Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
      final ExportPlugin ep = (ExportPlugin) c.newInstance();
      ep.init(proxy);
      return ep;
    }
    catch (Exception e)
    {
      Log.warn("Unable to create the export plugin: " + className, e);
      return null;
    }
  }

  /**
   * Returns true if the plug-in is enabled for a given report configuration, and false otherwise.
   *
   * @param config  the report configuration.
   * @param plugin  the plug-in key.
   *
   * @return A boolean.
   */
  protected boolean isPluginEnabled(final ReportConfiguration config, final String plugin)
  {
    return config.getConfigProperty(PLUGIN_ENABLE_PREFIX + plugin, "false").equals("true");
  }

  /**
   * Creates a list containing all available export plugins.
   * todo 0.8.4 move it into the report configuration ...
   *
   * @param proxy  the preview proxy.
   * @param config  the report configuration.
   *
   * @return  The list of export plugins.
   */
  public ArrayList createExportPlugIns(final PreviewProxy proxy, final ReportConfiguration config)
  {
    final InputStream in = getClass().getResourceAsStream
        ("/org/jfree/report/preview/previewplugins.properties");

    final Properties prop = new Properties();

    try
    {
      prop.load(in);
    }
    catch (Exception e)
    {
      Log.warn("Unable to load export plugin configuration.");
    }

    final String availablePlugins = prop.getProperty("available.plugins", "");
    final StringTokenizer strtok = new StringTokenizer(availablePlugins, ",");
    final ArrayList retval = new ArrayList();

    while (strtok.hasMoreElements())
    {
      final String plugin = strtok.nextToken().trim();
      final String pluginClass = prop.getProperty(plugin);
      if (pluginClass == null)
      {
        Log.warn(new org.jfree.util.Log.SimpleMessage("Plugin ", plugin, " is not defined."));
        continue;
      }
      if (isPluginEnabled(config, plugin))
      {
        final ExportPlugin ep = createPlugIn(proxy, pluginClass);
        if (ep != null)
        {
          retval.add(ep);
        }
      }
      else
      {
        Log.warn(new org.jfree.util.Log.SimpleMessage("Plugin ", plugin, " is not enabled."));
      }
    }
    return retval;
  }
}
