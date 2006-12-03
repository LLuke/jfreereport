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
 * $Id: AboutActionPlugin.java,v 1.2 2006/11/24 17:12:13 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.preview.actions;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Locale;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReportInfo;
import org.jfree.report.modules.gui.swing.common.AbstractActionPlugin;
import org.jfree.report.modules.gui.swing.common.SwingGuiContext;
import org.jfree.report.modules.gui.swing.common.SwingUtil;
import org.jfree.report.modules.gui.swing.preview.SwingPreviewModule;
import org.jfree.report.modules.gui.swing.preview.PreviewPane;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.about.AboutDialog;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 16.11.2006, 16:34:55
 *
 * @author Thomas Morgner
 */
public class AboutActionPlugin extends AbstractActionPlugin
    implements ControlActionPlugin
{
  private ResourceBundleSupport resources;
  private AboutDialog aboutFrame;

  public AboutActionPlugin()
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
    return "org.jfree.report.modules.gui.swing.preview.about.";
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.about.name");
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.about.description");
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.about.small-icon");
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.about.icon");
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return null;
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getMnemonic("action.about.mnemonic");
  }

  public boolean configure(PreviewPane reportPane)
  {
    if (aboutFrame == null)
    {
      final String title = getDisplayName();
      // look where we have been added ...
      Window w = SwingUtil.getWindowAncestor(reportPane);
      if (w instanceof Frame)
      {
        aboutFrame = new AboutDialog
                ((Frame) w, title, JFreeReportInfo.getInstance());
      }
      else if (w instanceof Dialog)
      {
        aboutFrame = new AboutDialog
                ((Dialog) w, title, JFreeReportInfo.getInstance());
      }
      else
      {
        aboutFrame = new AboutDialog
                (title, JFreeReportInfo.getInstance());
      }
      aboutFrame.pack();
      RefineryUtilities.centerFrameOnScreen(aboutFrame);
    }

    aboutFrame.setVisible(true);
    aboutFrame.requestFocus();
    return true;
  }

}
