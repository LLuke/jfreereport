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
 * ------------------------------
 * PreviewBase.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewBaseModule.java,v 1.3 2003/07/12 16:31:13 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base;

import javax.swing.UIManager;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.util.ReportConfiguration;

public class PreviewBaseModule extends AbstractModule
{
  public static final String SWING_TRANSLATE_KEY =
      "org.jfree.report.modules.gui.base.SwingDialogTranslation";

  public PreviewBaseModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize() throws ModuleInitializeException
  {
    if (isTranslateSwingDialogs())
    {
      UIManager.getDefaults().addResourceBundle
          (JFreeReportResources.class.getName());
    }
  }

  private boolean isTranslateSwingDialogs()
  {
    return ReportConfiguration.getGlobalConfig().getConfigProperty
        (SWING_TRANSLATE_KEY, "false").equals("true");
  }
}
