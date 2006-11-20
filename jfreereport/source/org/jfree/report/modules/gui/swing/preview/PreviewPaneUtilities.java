/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.preview;

import java.util.Arrays;
import javax.swing.JMenu;

import org.jfree.report.modules.gui.swing.common.ActionPlugin;
import org.jfree.report.modules.gui.swing.common.ActionPluginMenuComparator;
import org.jfree.report.modules.gui.swing.common.ExportActionPlugin;
import org.jfree.report.modules.gui.swing.common.ActionFactory;
import org.jfree.report.modules.gui.swing.common.DefaultActionFactory;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.common.DefaultIconTheme;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 17.11.2006, 15:06:51
 *
 * @author Thomas Morgner
 */
public class PreviewPaneUtilities
{
  private static final String ICON_THEME_CONFIG_KEY = "org.jfree.report.modules.gui.common.IconTheme";
  private static final String ACTION_FACTORY_CONFIG_KEY = "org.jfree.report.modules.gui.swing.preview.ActionFactory";

  private PreviewPaneUtilities()
  {
  }

  public static int buildMenu(JMenu menu,
                              ActionPlugin[] actions,
                              PreviewPane pane)
  {
    if (actions.length == 0)
    {
      return 0;
    }

    Arrays.sort(actions, new ActionPluginMenuComparator());
    boolean separatorPending = false;
    int count = 0;
    for (int i = 0; i < actions.length; i++)
    {
      ActionPlugin actionPlugin = actions[i];
      if (actionPlugin.isAddToMenu() == false)
      {
        continue;
      }

      if (count > 0 && separatorPending)
      {
        menu.addSeparator();
        separatorPending = false;
      }

      if (actionPlugin instanceof ExportActionPlugin)
      {
        final ExportActionPlugin exportPlugin = (ExportActionPlugin) actionPlugin;
        final ExportAction action = new ExportAction(exportPlugin, pane);
        menu.add(new ActionMenuItem(action));
        count += 1;
      }
      else if (actionPlugin instanceof ControlActionPlugin)
      {
        final ControlActionPlugin controlPlugin = (ControlActionPlugin) actionPlugin;
        final ControlAction action = new ControlAction(controlPlugin, pane);
        menu.add(new ActionMenuItem(action));
        count += 1;
      }

      if (actionPlugin.isSeparated())
      {
        separatorPending = true;
      }

    }
    return count;
  }


  public static double getNextZoomIn(final double zoom, final double[] zoomFactors )
  {
    if (zoom <= zoomFactors[0])
    {
      return (zoom * 2.0) / 3.0;
    }

    final double largestZoom = zoomFactors[zoomFactors.length - 1];
    if (zoom > largestZoom)
    {
      double linear = (zoom * 2.0) / 3.0;
      if (linear < largestZoom)
      {
        return largestZoom;
      }
      return linear;
    }

    for (int i = zoomFactors.length - 1; i >= 0; i--)
    {
      double factor = zoomFactors[i];
      if (factor < zoom)
      {
        return factor;
      }
    }

    return (zoom * 2.0) / 3.0;
  }

  public static double getNextZoomOut(final double zoom, final double[] zoomFactors )
  {
    final double largestZoom = zoomFactors[zoomFactors.length - 1];
    if (zoom >= largestZoom)
    {
      return (zoom * 1.5);
    }

    final double smallestZoom = zoomFactors[0];
    if (zoom < smallestZoom)
    {
      double linear = (zoom * 1.5);
      if (linear > smallestZoom)
      {
        return smallestZoom;
      }
      return linear;
    }

    for (int i = 0; i < zoomFactors.length; i++)
    {
      double factor = zoomFactors[i];
      if (factor > zoom)
      {
        return factor;
      }
    }
    return (zoom * 1.5);
  }


  public static IconTheme createIconTheme(Configuration config)
  {
    String themeClass = config.getConfigProperty(ICON_THEME_CONFIG_KEY);
    Object maybeTheme = ObjectUtilities.loadAndInstantiate(themeClass, PreviewPane.class);
    IconTheme iconTheme;
    if (maybeTheme instanceof IconTheme)
    {
      iconTheme = (IconTheme) maybeTheme;
    }
    else
    {
      iconTheme = new DefaultIconTheme();
    }
    iconTheme.initialize(config);
    return iconTheme;
  }

  public static ActionFactory createActionFactory(Configuration config)
  {
    final String factoryClass = config.getConfigProperty(ACTION_FACTORY_CONFIG_KEY);
    Object maybeFactory = ObjectUtilities.loadAndInstantiate(factoryClass, PreviewPane.class);
    ActionFactory actionFactory;
    if (maybeFactory instanceof ActionFactory)
    {
      actionFactory = (ActionFactory) maybeFactory;
    }
    else
    {
      actionFactory = new DefaultActionFactory();
    }
    return actionFactory;
  }

}
