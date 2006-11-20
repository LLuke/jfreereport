/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * AbstractExportDialog.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractExportDialog.java,v 1.1 2006/11/13 19:27:45 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.gui.swing.common;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.preferences.base.ConfigFactory;
import org.jfree.report.modules.preferences.base.ConfigStorage;
import org.jfree.report.modules.preferences.base.ConfigStoreException;
import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;

public abstract class AbstractExportDialog extends JDialog
{

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  protected abstract class AbstractConfirmAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public AbstractConfirmAction()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      if (performValidate() && performConfirm())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  /**
   * Internal action class to cancel the report processing.
   */
  protected abstract class AbstractCancelAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public AbstractCancelAction()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setConfirmed(false);
      setVisible(false);
    }
  }

  private class ExportDialogValidator extends FormValidator
  {
    public ExportDialogValidator()
    {
      super();
    }

    public boolean performValidate()
    {
      return AbstractExportDialog.this.performValidate();
    }

    public Action getConfirmAction()
    {
      return AbstractExportDialog.this.getConfirmAction();
    }
  }

  private class WindowCloseHandler extends WindowAdapter
  {
    public WindowCloseHandler()
    {
    }

    /**
     * Invoked when a window is in the process of being closed. The close
     * operation can be overridden at this point.
     */
    public void windowClosing(final WindowEvent e)
    {
      final Action cancelAction = getCancelAction();
      if (cancelAction != null)
      {
        cancelAction.actionPerformed(null);
      }
      else
      {
        setConfirmed(false);
        setVisible(false);
      }
    }
  }

  private Action cancelAction;
  private Action confirmAction;
  private FormValidator formValidator;
  private ResourceBundle resources;
  private boolean confirmed;

  /**
   * Creates a non-modal dialog without a title and without a specified
   * <code>Frame</code> owner.  A shared, hidden frame will be set as the owner
   * of the dialog.
   */
  public AbstractExportDialog()
  {
    initialize();
  }


  /**
   * Creates a non-modal dialog without a title with the specified
   * <code>Frame</code> as its owner.  If <code>owner</code> is
   * <code>null</code>, a shared, hidden frame will be set as the owner of the
   * dialog.
   *
   * @param owner the <code>Frame</code> from which the dialog is displayed
   */
  public AbstractExportDialog(final Frame owner)
  {
    super(owner);
    initialize();
  }


  /**
   * Creates a non-modal dialog without a title with the specified
   * <code>Dialog</code> as its owner.
   *
   * @param owner the non-null <code>Dialog</code> from which the dialog is
   *              displayed
   */
  public AbstractExportDialog(final Dialog owner)
  {
    super(owner);
    initialize();
  }

  protected void initialize()
  {
    formValidator = new ExportDialogValidator();
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowCloseHandler());
  }


  public abstract JStatusBar getStatusBar();

  protected Action getCancelAction()
  {
    return cancelAction;
  }

  protected void setCancelAction(final Action cancelAction)
  {
    this.cancelAction = cancelAction;
  }

  protected Action getConfirmAction()
  {
    return confirmAction;
  }

  protected void setConfirmAction(final Action confirmAction)
  {
    this.confirmAction = confirmAction;
  }

  protected abstract boolean performValidate();

  protected FormValidator getFormValidator()
  {
    return formValidator;
  }

  /**
   * Opens the dialog to query all necessary input from the user. This will not
   * start the processing, as this is done elsewhere.
   *
   * @param report the report that should be processed.
   * @return true, if the processing should continue, false otherwise.
   */
  public boolean performQueryForExport(final JFreeReport report)
  {
    final FormValidator formValidator = getFormValidator();
    formValidator.setEnabled(false);
    final ModifiableConfiguration repConf = report.getConfiguration();
    initFromConfiguration(repConf);
    final String configPath = ConfigFactory.encodePath(
            report.getName() + getConfigurationSuffix());
    if (isInputStorageEnabled(repConf))
    {
      final ConfigStorage storage = ConfigFactory.getInstance().getUserStorage();
      try
      {
        final Configuration properties =
                storage.load(configPath, new DefaultConfiguration());
        setDialogContents(properties, isFullInputStorageEnabled(repConf));
      }
      catch (Exception cse)
      {
        Log.debug("Unable to load the defaults in Export export dialog. [" + getClass() + "]");
      }
    }

    formValidator.setEnabled(true);
    formValidator.handleValidate();
    setModal(true);
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }

    formValidator.setEnabled(false);
    storeToConfiguration(repConf);
    if (isInputStorageEnabled(repConf))
    {
      try
      {
        final ConfigStorage storage = ConfigFactory.getInstance().getUserStorage();
        final Configuration dialogContents = getDialogContents(isFullInputStorageEnabled(repConf));
        storage.store(configPath, dialogContents);
      }
      catch (ConfigStoreException cse)
      {
        Log.debug("Unable to store the defaults in Export export dialog. [" + getClass() + "]");
      }
    }
    formValidator.setEnabled(true);
    return true;
  }

  protected abstract String getConfigurationPrefix();

  protected abstract Configuration getDialogContents(boolean full);

  protected abstract void storeToConfiguration(ModifiableConfiguration reportConfiguration);

  protected abstract void setDialogContents(Configuration properties,
                                            boolean full);

  protected abstract void initFromConfiguration(Configuration reportConfiguration);

  protected abstract String getConfigurationSuffix();

  /**
   * Retrieves the resources for this dialog. If the resources are not
   * initialized, they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  protected ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(getResourceBaseName());
    }
    return resources;
  }

  protected boolean isInputStorageEnabled(Configuration config)
  {
    final String confVal = config.getConfigProperty
            (getConfigurationPrefix() + "StoreDialogContents");
    return "none".equalsIgnoreCase(confVal) == false;
  }

  protected boolean isFullInputStorageEnabled(Configuration config)
  {
    final String confVal = config.getConfigProperty
            (getConfigurationPrefix() + "StoreDialogContents");
    return "all".equalsIgnoreCase(confVal);
  }

  /**
   * Returns <code>true</code> if the user confirmed the selection, and
   * <code>false</code> otherwise.  The file should only be saved if the result
   * is <code>true</code>.
   *
   * @return A boolean.
   */
  public boolean isConfirmed()
  {
    return confirmed;
  }

  /**
   * Defines whether this dialog has been finished using the 'OK' or the
   * 'Cancel' option.
   *
   * @param confirmed set to <code>true</code>, if OK was pressed,
   *                  <code>false</code> otherwise
   */
  protected void setConfirmed(final boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  protected boolean performConfirm()
  {
    return true;
  }

  public abstract void clear();

  protected abstract String getResourceBaseName();


  /**
   * Resolves file names for the exports. An occurence of "~/" at the beginning
   * of the name will be replaced with the users home directory.
   *
   * @param baseDirectory the base directory as specified in the configuration.
   * @return the file object pointing to that directory.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error occured or the directory could not be created.
   * @throws IllegalArgumentException if the base directory is null.
   */
  protected File resolvePath(String baseDirectory)
  {
    final File baseDirectoryFile;
    if(baseDirectory == null)
    {
      throw new IllegalArgumentException("The base directory must not be null");
    }

    if (baseDirectory.startsWith("~/") == false)
    {
      baseDirectoryFile = new File(baseDirectory);
    }
    else
    {
      final String homeDirectory = System.getProperty("user.home");
      if ("~/".equals(baseDirectory))
      {
        baseDirectoryFile = new File(homeDirectory);
      }
      else
      {
        baseDirectory = baseDirectory.substring(2);
        baseDirectoryFile = new File(homeDirectory, baseDirectory);
      }
    }
    return baseDirectoryFile;
  }
}
