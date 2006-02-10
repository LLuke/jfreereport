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
 * AbstractExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractExportPlugin.java,v 1.15 2005/12/07 22:20:35 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18-Jun-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jfree.report.util.WorkerPool;

/**
 * The AbstractExportPlugin provides a basic implementation of the ExportPlugin
 * interface.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExportPlugin implements ExportPlugin
{
  /**
   * The DefaultExportTaskListener is used to monitor the progress of the export and to
   * set the export result.
   *
   * @author Thomas Morgner
   */
  protected class DefaultExportTaskListener implements ExportTaskListener
  {
    /**
     * Creates a new export task listener, which will wait for the end of the report
     * processing..
     */
    public DefaultExportTaskListener ()
    {
    }

    /**
     * Informs the listener, that the export was completed without errors. The pagination
     * listener is removed from the
     *
     * @param task the export task which was completed.
     */
    public void taskAborted (final ExportTask task)
    {
      AbstractExportPlugin.this.handleExportResult(task);
    }

    /**
     * Informs the listener, that the export was aborted by the user.
     *
     * @param task the export task which was aborted.
     */
    public void taskDone (final ExportTask task)
    {
      AbstractExportPlugin.this.handleExportResult(task);
    }

    /**
     * Informs the listener, that the export failed due to errors.
     *
     * @param task the export task which failed.
     */
    public void taskFailed (final ExportTask task)
    {
      AbstractExportPlugin.this.handleExportResult(task);
    }

    /**
     * This method is empty as it does not indicate an finish event.
     *
     * @param task the export task.
     * @see org.jfree.report.modules.gui.base.ExportTaskListener#taskWaiting
     *      (org.jfree.report.modules.gui.base.ExportTask)
     */
    public void taskWaiting (final ExportTask task)
    {
    }
  }

  /**
   * The backend to perform post or preprocessing.
   */
  private PreviewProxyBase base;

  /**
   * The preview proxy used to display the preview component.
   */
  private PreviewProxy proxy;

  /**
   * The worker instance from the main dialog.
   */
  private WorkerPool worker;

  private PropertyChangeSupport propertyChangeSupport;

  private boolean enabled;
  private Skin skin;

  /**
   * DefaultConstructor.
   */
  public AbstractExportPlugin ()
  {
    propertyChangeSupport = new PropertyChangeSupport(this);
    skin = SkinLoader.loadSkin();
  }

  protected PropertyChangeSupport getPropertyChangeSupport ()
  {
    return propertyChangeSupport;
  }

  /**
   * Returns true if the action is separated, and false otherwise. A separated action
   * starts a new action group and will be spearated from previous actions on the menu and
   * toolbar.
   *
   * @return true, if the action should be separated from previous actions, false
   *         otherwise.
   */
  public boolean isSeparated ()
  {
    return false;
  }

  /**
   * Returns an error description for the last operation. This implementation provides a
   * basic default failure description text and should be overriden to give a more
   * detailed explaination.
   *
   * @return returns a error description.
   */
  public String getFailureDescription ()
  {
    return getDisplayName() + " failed on export.";
  }

  /**
   * Returns true, when this export plugin is used to configure the report or an other
   * plugin.
   *
   * @return true if this is a control plugin, false otherwise.
   */
  public boolean isControlPlugin ()
  {
    return false;
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy the preview proxy that created this plugin.
   * @throws java.lang.NullPointerException if the proxy or the proxy's basecomponent is
   *                                        null.
   */
  public void init (final PreviewProxy proxy)
  {
    if (proxy == null)
    {
      throw new NullPointerException("Proxy must not be null.");
    }
    if (proxy.getBase() == null)
    {
      throw new NullPointerException("Proxy must be initialized.");
    }
    this.base = proxy.getBase();
    this.proxy = proxy;
  }

  /**
   * Returns the preview proxy base. This is the same as calling
   * <code>getProxy().getBase()</code>.
   *
   * @return the preview proxy base.
   */
  public PreviewProxyBase getBase ()
  {
    return base;
  }

  /**
   * Returns the preview proxy used to create the export plugin.
   *
   * @return the preview proxy.
   */
  public PreviewProxy getProxy ()
  {
    return proxy;
  }

  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar ()
  {
    return false;
  }

  /**
   * Updates the status text of the base component.
   *
   * @param text the new status line text.
   */
  protected void updateStatusText (final String text)
  {
    getBase().setStatusText(text);
  }

  /**
   * Provides a default implementation to handle export errors. This implementation
   * updates the status line of the preview component.
   *
   * @param result the result of the export operation.
   * @return the value of result unmodified.
   */
  protected boolean handleExportResult (final boolean result)
  {
    if (isControlPlugin() == false && result == false)
    {
      updateStatusText("Export failed: " + getFailureDescription());
    }
    return result;
  }

  /**
   * Provides a default implementation to handle export errors. This implementation
   * updates the status line of the preview component.
   *
   * @param task the result of the export operation.
   * @return true, if the export was successfull, false otherwise.
   */
  protected boolean handleExportResult (final ExportTask task)
  {
    if (task.isTaskDone() == false)
    {
      updateStatusText("Exporting ...");
      return true;
    }
    else if (task.getReturnValue() == ExportTask.RETURN_SUCCESS)
    {
      updateStatusText("Export was successful.");
      return true;
    }
    else if (task.getReturnValue() == ExportTask.RETURN_ABORT)
    {
      updateStatusText("Export was aborted.");
      return false;
    }
    else
    {
      if (task.getException() != null)
      {
        updateStatusText("Export failed: " +
                task.getException().getLocalizedMessage());
      }
      else
      {
        updateStatusText("Export failed: " + getFailureDescription());
      }
      return false;
    }
  }

  /**
   * Defines the worker instance for that export plugin. Workers can be used to delegate
   * tasks to an other thread. The workers are shared among all export plugins of an
   * dialog instance.
   *
   * @param worker the worker.
   */
  public void defineWorkerPool (final WorkerPool worker)
  {
    this.worker = worker;
  }

  /**
   * Delegates the task to a worker. If no worker is defined, the runnable is executed
   * directly.
   *
   * @param runnable the task that should be executed.
   */
  public void delegateTask (final Runnable runnable)
  {
    if (worker != null)
    {
      synchronized (worker)
      {
        // check, whether a worker is available. 
        if (worker.isWorkerAvailable() == false)
        {
          // maybe we want to warn the user, so that he can abort the export?
        }
        // this may block, until a worker becomes available...
        worker.getWorkerForWorkload(runnable);
      }
    }
    else
    {
      runnable.run();
    }
  }

  /**
   * Creates a progress dialog, and tries to assign a parent based on the given preview
   * proxy.
   *
   * @return the progress dialog.
   */
  protected ReportProgressDialog createProgressDialog ()
  {

    if (proxy instanceof Frame)
    {
      return new ReportProgressDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      return new ReportProgressDialog((Dialog) proxy);
    }
    else
    {
      // look where we have been added ...
      if (proxy != null)
      {
        final PreviewProxyBase base = proxy.getBase();
        final Window w = PreviewProxyBase.getWindowAncestor(base);
        if (w instanceof Frame)
        {
          return new ReportProgressDialog((Frame) w);
        }
        else if (w instanceof Dialog)
        {
          return new ReportProgressDialog((Dialog) w);
        }
      }
      return new ReportProgressDialog();
    }
  }

  public void addPropertyChangeListener (final PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(l);
  }

  public void addPropertyChangeListener (final String property,
                                         final PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(property, l);
  }

  public void removePropertyChangeListener (final PropertyChangeListener l)
  {
    propertyChangeSupport.removePropertyChangeListener(l);
  }

  public void setEnabled (final boolean enabled)
  {
    final boolean oldEnabled = this.enabled;
    this.enabled = enabled;
    propertyChangeSupport.firePropertyChange("enabled", oldEnabled, enabled);
  }

  public boolean isEnabled ()
  {
    return enabled;
  }

  protected Skin getSkin()
  {
    return skin;
  }
}
