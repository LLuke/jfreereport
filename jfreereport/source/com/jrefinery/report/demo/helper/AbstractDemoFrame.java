/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * AbstractDemoFrame.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 19.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.demo.helper;

import java.util.ResourceBundle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Action;
import javax.swing.JMenu;

import com.jrefinery.report.demo.PreviewAction;
import com.jrefinery.report.util.ExceptionDialog;

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
    public void actionPerformed(ActionEvent event)
    {
      attemptExit();
    }
  }

  /**
   * Window close handler.
   */
  protected class CloseHandler extends WindowAdapter
  {
    /**
     * Handles the window closing event.
     *
     * @param event  the window event.
     */
    public void windowClosing(WindowEvent event)
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
    public void actionPerformed(ActionEvent event)
    {
      attemptPreview();
    }
  }

  /** The base resource class. */
  public static final String RESOURCE_BASE = "com.jrefinery.report.demo.resources.DemoResources";

  /** Localised resources. */
  private ResourceBundle resources;

  private Action closeAction;

  private Action previewAction;

  /**
   * Constructs a new frame that is initially invisible.
   * <p>
   * This constructor sets the component's locale property to the value
   * returned by <code>JComponent.getDefaultLocale</code>.
   */
  public AbstractDemoFrame()
  {
    resources = ResourceBundle.getBundle(RESOURCE_BASE);
    previewAction = new DemoPreviewAction();
    closeAction = new DemoCloseAction();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new CloseHandler());
  }

  public ResourceBundle getResources()
  {
    return resources;
  }

  public Action getCloseAction()
  {
    return closeAction;
  }

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
    boolean close =
        JOptionPane.showConfirmDialog(
            this,
            getResources().getString("exitdialog.message"),
            getResources().getString("exitdialog.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE)
        == JOptionPane.YES_OPTION;
    if (close)
    {
      dispose();
      System.exit(0);
    }

    return close;
  }

  protected abstract void attemptPreview ();

  /**
   * Creates a JMenu which gets initialized from the current resource bundle.
   *
   * @param base the resource prefix.
   *
   * @return the menu.
   */
  protected JMenu createJMenuItem(String base)
  {
    String label = getResources().getString(base + ".name");
    Character mnemonic = (Character) getResources().getObject(base + ".mnemonic");

    JMenu menu = new JMenu(label);
    if (mnemonic != null)
    {
      menu.setMnemonic(mnemonic.charValue());
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
  protected void showExceptionDialog(String localisationBase, Exception e)
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
