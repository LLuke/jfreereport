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
 * $Id: PreviewBaseModule.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.base;

import java.util.ResourceBundle;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

public class PreviewBaseModule extends AbstractModule
{
  private ResourceBundle resources;

  public PreviewBaseModule() throws ModuleInitializeException
  {
    loadModuleInfo();
    resources = ResourceBundle.getBundle(JFreeReportResources.class.getName());
  }

  public void initialize() throws ModuleInitializeException
  {
    if (isTranslateSwingDialogs())
    {
      putTranslation("FileChooser.acceptAllFileFilterText");
      putTranslation("FileChooser.cancelButtonMnemonic");
      putTranslation("FileChooser.cancelButtonText");
      putTranslation("FileChooser.cancelButtonToolTipText");
      putTranslation("FileChooser.detailsViewButtonAccessibleName");
      putTranslation("FileChooser.detailsViewButtonToolTipText");
      putTranslation("FileChooser.directoryDescriptionText");
      putTranslation("FileChooser.fileDescriptionText");
      putTranslation("FileChooser.fileNameLabelMnemonic");
      putTranslation("FileChooser.fileNameLabelText");
      putTranslation("FileChooser.filesOfTypeLabelMnemonic");
      putTranslation("FileChooser.filesOfTypeLabelText");
      putTranslation("FileChooser.helpButtonMnemonic");
      putTranslation("FileChooser.helpButtonText");
      putTranslation("FileChooser.helpButtonToolTipText");
      putTranslation("FileChooser.homeFolderAccessibleName");
      putTranslation("FileChooser.homeFolderToolTipText");
      putTranslation("FileChooser.listViewButtonAccessibleName");
      putTranslation("FileChooser.listViewButtonToolTipText");
      putTranslation("FileChooser.lookInLabelMnemonic");
      putTranslation("FileChooser.lookInLabelText");
      putTranslation("FileChooser.newFolderAccessibleNam");
      putTranslation("FileChooser.newFolderErrorSeparator");
      putTranslation("FileChooser.newFolderErrorText");
      putTranslation("FileChooser.newFolderToolTipText");
      putTranslation("FileChooser.openButtonMnemonic");
      putTranslation("FileChooser.openButtonText");
      putTranslation("FileChooser.openButtonToolTipText");
      putTranslation("FileChooser.saveButtonMnemonic");
      putTranslation("FileChooser.saveButtonText");
      putTranslation("FileChooser.saveButtonToolTipText");
      putTranslation("FileChooser.updateButtonMnemonic");
      putTranslation("FileChooser.updateButtonText");
      putTranslation("FileChooser.updateButtonToolTipText");
      putTranslation("FileChooser.upFolderAccessibleName");
      putTranslation("FileChooser.upFolderToolTipText");
    }
  }

  private void putTranslation (String key)
  {
    Object value = resources.getObject(key);
    UIDefaults defaults = UIManager.getDefaults();
    defaults.put(key, value);
  }

  private boolean isTranslateSwingDialogs()
  {
    return false;
  }
}
