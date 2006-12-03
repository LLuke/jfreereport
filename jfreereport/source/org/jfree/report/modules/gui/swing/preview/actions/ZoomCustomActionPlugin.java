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
 * $Id: ZoomCustomAction.java,v 1.1 2006/11/20 21:17:03 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.preview.actions;

import java.awt.Window;
import java.util.Locale;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.jfree.report.modules.gui.swing.common.AbstractActionPlugin;
import org.jfree.report.modules.gui.swing.common.SwingGuiContext;
import org.jfree.report.modules.gui.swing.preview.PreviewPane;
import org.jfree.report.modules.gui.swing.preview.SwingPreviewModule;
import org.jfree.util.Log;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 16.11.2006, 18:52:30
 *
 * @author Thomas Morgner
 */
public class ZoomCustomActionPlugin  extends AbstractActionPlugin
    implements ControlActionPlugin
{
  private ResourceBundleSupport resources;

  public ZoomCustomActionPlugin()
  {
  }

  public void initialize(SwingGuiContext context)
  {
    super.initialize(context);
    resources = new ResourceBundleSupport(context.getLocale(),
        SwingPreviewModule.BUNDLE_NAME);
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.swing.preview.zoom-custom.";
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.zoomCustom.name");
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.zoomCustom.description");
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.zoomCustom.small-icon");
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.zoomCustom.icon");
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return resources.getOptionalKeyStroke("action.zoomCustom.accelerator");
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getOptionalMnemonic("action.zoomCustom.mnemonic");
  }

  public boolean configure(PreviewPane reportPane)
  {
    Window window = SwingUtilities.windowForComponent(reportPane);
    final String result = JOptionPane.showInputDialog(window,
            resources.getString("dialog.zoom.title"),
            resources.getString("dialog.zoom.message"),
            JOptionPane.OK_CANCEL_OPTION);
    if (result == null)
    {
      return false;
    }
    try
    {
      final double zoom = Double.parseDouble(result);

      if (zoom > 0 && zoom <= 4000)
      {
        reportPane.setZoom(zoom / 100.0);
        return true;
      }

    }
    catch (Exception ex)
    {
      Log.info("ZoomAction: swallowed an exception");
    }
    return false;
  }


}
