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
 * ------------------------------
 * PreviewBaseModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewBaseModule.java,v 1.6 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import javax.swing.UIManager;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.util.ReportConfiguration;

/**
 * The preview base module provides the basic preview components. This package
 * contains Applet, JDialog, JFrame and JInternalFrame implementations of the
 * preview component. External export modules may plug into these components
 * to provide additional functionality.
 *
 * @author Thomas Morgner
 */
public class PreviewBaseModule extends AbstractModule
{
  /** A configuration key defining whether to translate the swing components. */
  public static final String SWING_TRANSLATE_KEY =
      "org.jfree.report.modules.gui.base.SwingDialogTranslation";

  /**
   * Default Constructor. Loads the module definition.
   *
   * @throws ModuleInitializeException if loading the module definition failed.
   */
  public PreviewBaseModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. If the swing components should be translated, the
   * resources are plugged into the Swing-ResourceManager.
   * @see org.jfree.report.modules.Module#initialize()
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
    if (isTranslateSwingDialogs())
    {
      UIManager.getDefaults().addResourceBundle
          (JFreeReportResources.class.getName());
    }
  }

  /**
   * Checks, whethe to translate swing dialogs. This is a global setting and must
   * be configured outside.
   *
   * @return true, if translating is enabled, false otherwise.
   */
  private boolean isTranslateSwingDialogs()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (SWING_TRANSLATE_KEY, "false").equals("true");
  }
}
