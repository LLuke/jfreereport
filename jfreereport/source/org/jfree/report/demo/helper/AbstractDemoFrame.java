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
 * AbstractDemoFrame.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractDemoFrame.java,v 1.3.4.2 2004/10/13 18:42:15 taqua Exp $
 *
 * Changes
 * -------------------------
 * 19.06.2003 : Initial version
 *
 */

package org.jfree.report.demo.helper;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import org.jfree.report.demo.PreviewAction;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.ResourceBundleSupport;

/**
 * The AbstractDemoFrame provides some basic functionality shared among all demos.
 * It provides default handlers for preview and the window-closing event as well
 * as helper function to display error messages.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractDemoFrame extends JFrame
{
  /**
   * Close action.
   */
  protected class DemoCloseAction extends CloseAction
  {
    /**
     * Default constructor.
     */
    public DemoCloseAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptExit();
    }
  }

  /**
   * Window close handler.
   */
  protected class CloseHandler extends WindowAdapter
  {
    public CloseHandler ()
    {
    }

    /**
     * Handles the window closing event.
     *
     * @param event  the window event.
     */
    public void windowClosing(final WindowEvent event)
    {
      attemptExit();
    }
  }

  /**
   * Preview action.
   */
  protected class DemoPreviewAction extends PreviewAction
  {
    /**
     * Default constructor.
     */
    public DemoPreviewAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptPreview();
    }
  }

  /** The base resource class. */
  public static final String RESOURCE_BASE =
          "org.jfree.report.demo.resources.demo-resources";

  /** Localised resources. */
  private final ResourceBundleSupport resources;

  /** The close action is called when closing the frame. */
  private final Action closeAction;

  /** The preview action is called when the user chooses to preview the report. */
  private final Action previewAction;

  /**
   * Constructs a new frame that is initially invisible.
   * <p>
   * This constructor sets the component's locale property to the value
   * returned by <code>JComponent.getDefaultLocale</code>.
   */
  public AbstractDemoFrame()
  {
    resources = new ResourceBundleSupport(RESOURCE_BASE);
    previewAction = new DemoPreviewAction();
    closeAction = new DemoCloseAction();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new CloseHandler());
  }

  /**
   * Returns the resource bundle for this demo frame.
   *
   * @return the resource bundle for the localization.
   */
  public ResourceBundleSupport getResources()
  {
    return resources;
  }

  /**
   * Returns the close action implementation to handle the closing of the frame.
   *
   * @return the close action.
   */
  public Action getCloseAction()
  {
    return closeAction;
  }

  /**
   * Returns the preview action implementation to handle the preview action event.
   *
   * @return the preview action.
   */
  public Action getPreviewAction()
  {
    return previewAction;
  }

  /**
   * Exits the application, but only if the user agrees.
   *
   * @return false if the user decides not to exit the application.
   */
  protected boolean attemptExit()
  {
    final boolean close =
        JOptionPane.showConfirmDialog(
            this,
            getResources().getString("exitdialog.message"),
            getResources().getString("exitdialog.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    if (close)
    {
      if (ReportConfiguration.getGlobalConfig().getConfigProperty
              ("org.jfree.report.demo.Embedded", "false").equals("false"))
      {
        System.exit(0);
      }
      else
      {
        setVisible(false);
        dispose();
      }
    }

    return close;
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected abstract void attemptPreview();

  /**
   * Creates a JMenu which gets initialized from the current resource bundle.
   *
   * @param base the resource prefix.
   *
   * @return the menu.
   */
  protected JMenu createJMenu(final String base)
  {
    final String label = getResources().getString(base + ".name");
    final Integer mnemonic = getResources().getMnemonic(base + ".mnemonic");

    final JMenu menu = new JMenu(label);
    if (mnemonic != null)
    {
      menu.setMnemonic(mnemonic.intValue());
    }
    return menu;
  }

  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
   *
   * @param localisationBase  the resource prefix.
   * @param e  the exception.
   */
  protected void showExceptionDialog(final String localisationBase, final Exception e)
  {
    ExceptionDialog.showExceptionDialog(
        getResources().getString(localisationBase + ".title"),
        MessageFormat.format(
            getResources().getString(localisationBase + ".message"),
            new Object[]{e.getLocalizedMessage()}
        ),
        e);
  }

}
