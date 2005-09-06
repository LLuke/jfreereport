/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExportPluginFactory.java,v 1.18 2005/08/08 15:36:30 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.WorkerPool;
import org.jfree.util.Log;

/**
 * An export plug-in factory. This factory is used to collect all available export plugins
 * and to make them avaiable to the preview components.
 *
 * @author Thomas Morgner.
 */
public final class ExportPluginFactory
{
  private static class ExportPluginSelectorComparator implements Comparator
  {
    public ExportPluginSelectorComparator()
    {
    }

    public int compare(Object o1, Object o2)
    {
      final ExportPluginSelector s1 = (ExportPluginSelector) o1;
      final ExportPluginSelector s2 = (ExportPluginSelector) o2;

      return s1.getPreference().compareTo(s2.getPreference());
    }
  }

  /**
   * The singleton instance of this factory.
   */
  private static ExportPluginFactory factory;

  /**
   * Returns the singleton instance of the export plugin factory.
   *
   * @return the factory instance
   */
  public static ExportPluginFactory getInstance ()
  {
    if (factory == null)
    {
      factory = new ExportPluginFactory();
    }
    return factory;
  }

  /**
   * The list of all known export plugins.
   */
  private final ArrayList exportPlugins;

  /**
   * DefaultConstructor. Defines a new export plugin factory.
   */
  private ExportPluginFactory ()
  {
    exportPlugins = new ArrayList();
  }

  /**
   * Registers the given plugin in this factory.
   *
   * @param plugin     the implementing class of the export plugin
   * @param preference the sort order in the menu
   * @param enableKey  the enable key of the export plugin to trigger the visiblity
   */
  public void registerPlugin (final ExportPluginSelector pluginSelector)
  {
    if (ExportPlugin.class.isAssignableFrom(pluginSelector.getPluginClass()) == false)
    {
      throw new IllegalArgumentException
              ("Only ExportPlugin implementations are allowed");
    }
    if (exportPlugins.contains(pluginSelector) == false)
    {
      exportPlugins.add(pluginSelector);
    }
  }

  /**
   * Loads and instatiaties an export plug-in.
   *
   * @param proxy  the preview proxy.
   * @param plugin the class of the export plugin.
   * @return The plug-in.
   */
  protected ExportPlugin createPlugIn (final PreviewProxy proxy, final Class plugin)
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
   * Creates a list containing all available export plugins.
   *
   * @param proxy  the preview proxy.
   * @param config the report configuration.
   * @param worker the woker that should be used to execute the exports, or null, if all
   *               tasks should be executed synchronous.
   * @return The list of export plugins.
   */
  public ArrayList createExportPlugIns
          (final PreviewProxy proxy, final ReportConfiguration config,
           final WorkerPool worker)
  {
    final ExportPluginSelector[] def = (ExportPluginSelector[])
            exportPlugins.toArray(new ExportPluginSelector[exportPlugins.size()]);

    Arrays.sort(def, new ExportPluginSelectorComparator());
    final ArrayList retval = new ArrayList();

    for (int i = 0; i < def.length; i++)
    {
      final ExportPluginSelector definition = def[i];
      if (definition.isPluginValid(proxy, config))
      {
        final ExportPlugin ep = createPlugIn(proxy, definition.getPluginClass());
        if (ep != null)
        {
          ep.defineWorkerPool(worker);
          retval.add(ep);
        }
        else
        {
          Log.warn("Cannot create plugin: " + definition.getPluginClass());
        }
      }
      else
      {
        Log.info(new Log.SimpleMessage
                ("Plugin ", definition.getPluginClass()," is not enabled."));
      }
    }
    return retval;
  }
}
