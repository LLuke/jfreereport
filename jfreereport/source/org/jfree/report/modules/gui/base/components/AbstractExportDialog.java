package org.jfree.report.modules.gui.base.components;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.misc.configstore.base.ConfigStoreException;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

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
    public AbstractConfirmAction ()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
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
    public AbstractCancelAction ()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      setConfirmed(false);
      setVisible(false);
    }
  }

  private class ExportDialogValidator extends FormValidator
  {
    public ExportDialogValidator ()
    {
      super();
    }

    public boolean performValidate ()
    {
      return AbstractExportDialog.this.performValidate();
    }

    public Action getConfirmAction ()
    {
      return AbstractExportDialog.this.getConfirmAction();
    }
  }

  private class WindowCloseHandler extends WindowAdapter
  {
    public WindowCloseHandler ()
    {
    }

    /**
     * Invoked when a window is in the process of being closed. The close operation can be
     * overridden at this point.
     */
    public void windowClosing (final WindowEvent e)
    {
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
  private JStatusBar statusBar;

  /**
   * Creates a non-modal dialog without a title and without a specified <code>Frame</code>
   * owner.  A shared, hidden frame will be set as the owner of the dialog.
   */
  public AbstractExportDialog ()
          throws HeadlessException
  {
    formValidator = new ExportDialogValidator();
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowCloseHandler());
    statusBar = new JStatusBar();
  }


  /**
   * Creates a non-modal dialog without a title with the specified <code>Frame</code> as
   * its owner.  If <code>owner</code> is <code>null</code>, a shared, hidden frame will
   * be set as the owner of the dialog.
   *
   * @param owner the <code>Frame</code> from which the dialog is displayed
   */
  public AbstractExportDialog (final Frame owner)
          throws HeadlessException
  {
    super(owner);
    formValidator = new ExportDialogValidator();
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    statusBar = new JStatusBar();
  }

  /**
   * Creates a non-modal dialog without a title with the specified <code>Dialog</code> as
   * its owner.
   *
   * @param owner the non-null <code>Dialog</code> from which the dialog is displayed
   */
  public AbstractExportDialog (final Dialog owner)
          throws HeadlessException
  {
    super(owner);
    formValidator = new ExportDialogValidator();
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    statusBar = new JStatusBar();
  }

  public JStatusBar getStatusBar ()
  {
    return statusBar;
  }

  protected Action getCancelAction ()
  {
    return cancelAction;
  }

  protected void setCancelAction (final Action cancelAction)
  {
    this.cancelAction = cancelAction;
  }

  protected Action getConfirmAction ()
  {
    return confirmAction;
  }

  protected void setConfirmAction (final Action confirmAction)
  {
    this.confirmAction = confirmAction;
  }

  protected abstract boolean performValidate ();

  protected FormValidator getFormValidator ()
  {
    return formValidator;
  }

  /**
   * Opens the dialog to query all necessary input from the user. This will not start the
   * processing, as this is done elsewhere.
   *
   * @param report the report that should be processed.
   * @return true, if the processing should continue, false otherwise.
   */
  public boolean performQueryForExport (final JFreeReport report)
  {
    getFormValidator().setEnabled(false);
    initFromConfiguration(report.getReportConfiguration());
    final ConfigStorage storage = ConfigFactory.getInstance().getUserStorage();
    try
    {
      setDialogContents(storage.loadProperties
              (ConfigFactory.encodePath(report.getName() + getConfigurationSuffix()),
                      new Properties()));
    }
    catch (Exception cse)
    {
      Log.debug("Unable to load the defaults in Export export dialog. [" + getClass() + "]");
    }

    getFormValidator().setEnabled(true);
    getFormValidator().handleValidate();
    setModal(true);
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }

    getFormValidator().setEnabled(false);
    storeToConfiguration(report.getReportConfiguration());
    try
    {
      storage.storeProperties
              (ConfigFactory.encodePath(report.getName() + getConfigurationSuffix()),
                      getDialogContents());
    }
    catch (ConfigStoreException cse)
    {
      Log.debug("Unable to store the defaults in Export export dialog. [" + getClass() + "]");
    }
    getFormValidator().setEnabled(true);
    return true;
  }

  protected abstract Properties getDialogContents ();
  protected abstract void storeToConfiguration (ReportConfiguration reportConfiguration);
  protected abstract void setDialogContents (Properties properties);
  protected abstract void initFromConfiguration (ReportConfiguration reportConfiguration);
  protected abstract String getConfigurationSuffix();

  /**
   * Retrieves the resources for this dialog. If the resources are not initialized, they
   * get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  protected ResourceBundle getResources ()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(getResourceBaseName());
    }
    return resources;
  }


  /**
   * Returns <code>true</code> if the user confirmed the selection, and <code>false</code>
   * otherwise.  The file should only be saved if the result is <code>true</code>.
   *
   * @return A boolean.
   */
  public boolean isConfirmed ()
  {
    return confirmed;
  }

  /**
   * Defines whether this dialog has been finished using the 'OK' or the 'Cancel' option.
   *
   * @param confirmed set to <code>true</code>, if OK was pressed, <code>false</code>
   *                  otherwise
   */
  protected void setConfirmed (final boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  protected boolean performConfirm ()
  {
    return true;
  }

  public abstract void clear ();

  protected abstract String getResourceBaseName ();

}