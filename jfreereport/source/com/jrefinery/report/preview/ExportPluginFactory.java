/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * $Id: ExportPluginFactory.java,v 1.6 2003/02/25 14:45:31 mungady Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

/**
 * An export plug-in factory.
 * 
 * @author Thomas Morgner.
 */
public class ExportPluginFactory
{
  /** The plug-in enable prefix. */
  public static final String PLUGIN_ENABLE_PREFIX = "com.jrefinery.report.preview.plugin.";


  /**
   * Creates an Excel plug-in.
   * 
   * @param proxy  the preview proxy.
   * 
   * @return The plug-in.
   */
  protected ExportPlugin createPlugIn (PreviewProxy proxy, String className)
  {
    try
    {
      Class c = Class.forName(className);
      ExportPlugin ep = (ExportPlugin) c.newInstance();
      ep.init(proxy);
      return ep;
    }
    catch (Exception e)
    {
      Log.warn ("Unable to create the export plugin: " + className, e);
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
  protected boolean isPluginEnabled (ReportConfiguration config, String plugin)
  {
    return config.getConfigProperty(PLUGIN_ENABLE_PREFIX + plugin, "false").equals("true");
  }

  /**
   * Creates a list containing all available export plugins.
   * 
   * @param proxy  the preview proxy.
   * @param config  the report configuration.
   * 
   * @return  The list of export plugins.
   */
  public ArrayList createExportPlugIns (PreviewProxy proxy, ReportConfiguration config)
  {
    InputStream in = getClass().getResourceAsStream
        ("/com/jrefinery/report/preview/previewplugins.properties");

    Properties prop = new Properties ();

    try
    {
      prop.load(in);
    }
    catch (Exception e)
    {
      Log.warn ("Unable to load export plugin configuration.");
    }

    String availablePlugins = prop.getProperty("available.plugins", "");
    StringTokenizer strtok = new StringTokenizer(availablePlugins, ",");
    ArrayList retval = new ArrayList();

    while (strtok.hasMoreElements())
    {
      String plugin = strtok.nextToken().trim();
      String pluginClass = prop.getProperty(plugin);
      if (pluginClass == null)
      {
        Log.warn (new Log.SimpleMessage("Plugin ", plugin, " is not defined."));
        continue;
      }
      if (isPluginEnabled(config, plugin))
      {
        ExportPlugin ep = createPlugIn(proxy, pluginClass);
        if (ep != null)
        {
          retval.add (ep);
        }
      }
      else
      {
        Log.warn (new Log.SimpleMessage("Plugin ", plugin, " is not enabled."));
      }
    }
    return retval;
  }
}
