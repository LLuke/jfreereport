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
 * $Id: AbstractDemoFrame.java,v 1.9 2005/07/20 18:47:29 taqua Exp $
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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.actions.AboutAction;
import org.jfree.report.demo.helper.actions.CloseAction;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.NumberCellRenderer;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.about.AboutFrame;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.ResourceBundleSupport;

/**
 * The AbstractDemoFrame provides some basic functionality shared among all
 * demos. It provides default handlers for preview and the window-closing event
 * as well as helper function to display error messages.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractDemoFrame extends JFrame implements DemoControler
{
  protected static final String EMBEDDED_KEY = "org.jfree.report.demo.Embedded";

  /** Close action. */
  protected class DemoCloseAction extends CloseAction
  {
    /** Default constructor. */
    public DemoCloseAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptExit();
    }
  }

  /** Window close handler. */
  protected class CloseHandler extends WindowAdapter
  {
    public CloseHandler()
    {
    }

    /**
     * Handles the window closing event.
     *
     * @param event the window event.
     */
    public void windowClosing(final WindowEvent event)
    {
      attemptExit();
    }
  }

  /** Preview action. */
  protected class DemoPreviewAction extends PreviewAction
  {
    /** Default constructor. */
    public DemoPreviewAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptPreview();
    }
  }


  /** About action. */
  private class DemoAboutAction extends AboutAction
  {
    /** Default constructor. */
    public DemoAboutAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      displayAbout();
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

  /** About action. */
  private AboutAction aboutAction;

  /** A frame for displaying information about the demo application. */
  private AboutFrame aboutFrame;
  private boolean ignoreEmbeddedConfig;
  private JStatusBar statusBar;

  /**
   * Constructs a new frame that is initially invisible.
   * <p/>
   * This constructor sets the component's locale property to the value returned
   * by <code>JComponent.getDefaultLocale</code>.
   */
  public AbstractDemoFrame()
  {
    resources = new ResourceBundleSupport(RESOURCE_BASE);
    previewAction = new DemoPreviewAction();
    closeAction = new DemoCloseAction();
    aboutAction = new DemoAboutAction();
    statusBar = new JStatusBar();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new CloseHandler());
  }

  public boolean isIgnoreEmbeddedConfig()
  {
    return ignoreEmbeddedConfig;
  }

  public void setIgnoreEmbeddedConfig(final boolean ignoreEmbeddedConfig)
  {
    this.ignoreEmbeddedConfig = ignoreEmbeddedConfig;
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
   * Returns the close action implementation to handle the closing of the
   * frame.
   *
   * @return the close action.
   */
  public Action getCloseAction()
  {
    return closeAction;
  }

  /**
   * Returns the preview action implementation to handle the preview action
   * event.
   *
   * @return the preview action.
   */
  public Action getPreviewAction()
  {
    return previewAction;
  }

  public AboutAction getAboutAction()
  {
    return aboutAction;
  }

  /**
   * Exits the application, but only if the user agrees.
   *
   * @return false if the user decides not to exit the application.
   */
  protected boolean attemptExit()
  {
    final boolean close =
            JOptionPane.showConfirmDialog(this,
                    getResources().getString("exitdialog.message"),
                    getResources().getString("exitdialog.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    if (close)
    {
      if (ignoreEmbeddedConfig ||
              ReportConfiguration.getGlobalConfig().getConfigProperty
                      (EMBEDDED_KEY, "false").equals("false"))
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
   * used to construct the localisation key by appending ".title" and ".message"
   * to the base name.
   *
   * @param localisationBase the resource prefix.
   * @param e                the exception.
   */
  public static void showExceptionDialog
          (final String localisationBase, final Exception e)
  {
    final ResourceBundleSupport resources = new ResourceBundleSupport(
            RESOURCE_BASE);
    final String title = resources.getString(localisationBase + ".title");
    final String format = resources.getString(localisationBase + ".message");
    final String message = MessageFormat.format
            (format, new Object[]{e.getLocalizedMessage()});
    ExceptionDialog.showExceptionDialog(title, message, e);
  }

  protected JComponent createDefaultTable(final TableModel data)
  {
    final JTable table = new JTable(data);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    for (int columnIndex = 0; columnIndex < data
            .getColumnCount(); columnIndex++)
    {
      final TableColumn column = table.getColumnModel().getColumn(columnIndex);
      column.setMinWidth(50);
      final Class c = data.getColumnClass(columnIndex);
      if (c.equals(Number.class))
      {
        column.setCellRenderer(new NumberCellRenderer());
      }
    }

    return new JScrollPane
            (table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }


  /** Displays information about the application. */
  public synchronized void displayAbout()
  {
    if (aboutFrame == null)
    {
      aboutFrame = new AboutFrame
              (getResources().getString("action.about.name"),
                      JFreeReport.getInfo());

      aboutFrame.pack();
      RefineryUtilities.centerFrameOnScreen(aboutFrame);
    }
    aboutFrame.setVisible(true);
    aboutFrame.requestFocus();
  }

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  protected JMenuBar createMenuBar()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem previewItem = new ActionMenuItem(getPreviewAction());
    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    fileMenu.add(previewItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);

    // then the help menu
    final JMenu helpMenu = createJMenu("menu.help");
    helpMenu.add(new ActionMenuItem(aboutAction));
    mb.add(helpMenu);
    return mb;
  }

  public Action getExportAction()
  {
    return previewAction;
  }

  public JStatusBar getStatusBar()
  {
    return statusBar;
  }
}
