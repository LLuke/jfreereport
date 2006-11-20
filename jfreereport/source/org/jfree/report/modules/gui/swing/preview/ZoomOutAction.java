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

import java.util.Locale;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.jfree.util.ResourceBundleSupport;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.report.modules.gui.common.IconTheme;

/**
 * Creation-Date: 16.11.2006, 18:52:30
 *
 * @author Thomas Morgner
 */
public class ZoomOutAction extends AbstractAction
{
  private PreviewPane pane;

  /**
   * Defines an <code>Action</code> object with a default description string and
   * default icon.
   */
  public ZoomOutAction(PreviewPane pane)
  {
    this.pane = pane;
    final Locale locale = pane.getLocale();
    ResourceBundleSupport resources = new ResourceBundleSupport
        (locale, SwingPreviewModule.BUNDLE_NAME);
    putValue(NAME, resources.getString("action.zoomOut.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.zoomOut.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
            resources.getMnemonic("action.zoomOut.mnemonic"));
    final KeyStroke keyStroke = resources.getKeyStroke("action.zoomOut.accelerator");
    putValue(ActionDowngrade.ACCELERATOR_KEY, keyStroke);

    final IconTheme iconTheme = pane.getIconTheme();
    putValue(SMALL_ICON, iconTheme.getSmallIcon(locale, "action.zoomOut.small-icon"));
    putValue("ICON24", iconTheme.getSmallIcon(locale, "action.zoomOut.icon"));
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e)
  {
    pane.setZoom(PreviewPaneUtilities.getNextZoomOut
        (pane.getZoom(), pane.getZoomFactors()));
  }
}
