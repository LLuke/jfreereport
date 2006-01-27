/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * InternalFrameDemoFrame.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: InternalFrameDemoFrame.java,v 1.2 2005/12/22 01:25:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.report.demo.layouts.internalframe;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.functions.PaintComponentTableModel;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.demo.layouts.ComponentDrawingDemoHandler;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.12.2005, 12:49:55
 *
 * @author Thomas Morgner
 */
public class InternalFrameDemoFrame extends AbstractDemoFrame
{
  private class NewFrameAction extends AbstractActionDowngrade
  {
    public NewFrameAction()
    {
      putValue(ActionDowngrade.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
      putValue(ActionDowngrade.NAME, "New");
      putValue(ActionDowngrade.ACCELERATOR_KEY,
              KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
    }

    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e)
    {
      JInternalFrame frame = new DocumentInternalFrame();
      frame.setSize(400, 300);
      frame.setVisible(true); //necessary as of 1.3
      desktop.add(frame);
      try
      {
        frame.setSelected(true);
      }
      catch (PropertyVetoException ex)
      {
        // ignore exception ..
      }
    }
  }

  private JDesktopPane desktop;

  public InternalFrameDemoFrame()
  {
    setTitle("InternalFrameDemo");

    setJMenuBar(createMenuBar());

    desktop = new JDesktopPane();
    setContentPane(desktop);
  }

  public void updateFrameSize (int inset)
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset, screenSize.width - inset * 2,
            screenSize.height - inset * 2);
  }

  protected JMenuBar createMenuBar()
  {
    JMenuBar menuBar = new JMenuBar();

//Set up the lone menu.
    JMenu menu = new JMenu("Document");
    menu.setMnemonic(KeyEvent.VK_D);
    menuBar.add(menu);

    menu.add(new ActionMenuItem(new NewFrameAction()));
    menu.add(new ActionMenuItem(getPreviewAction()));
    menu.addSeparator();
    menu.add(new ActionMenuItem(getCloseAction()));

    JMenu helpmenu = new JMenu("Help");
    helpmenu.setMnemonic(KeyEvent.VK_H);
    helpmenu.add(new ActionMenuItem(getAboutAction()));
    return menuBar;
  }


  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview()
  {
    JInternalFrame frame = findSelectedFrame();
    if (frame == null)
    {
      return;
    }
    Rectangle bounds = frame.getBounds();
    Container parent = frame.getParent();
    boolean visible = frame.isVisible();
    int layer = frame.getLayer();

    // now print ..
    previewReport(frame);

    if (parent != null)
    {
      if (frame.getParent() != parent)
      {
        frame.getParent().remove(frame);
        parent.add(frame);
      }
    }
    frame.setBounds(bounds);
    frame.setVisible(visible);
    frame.setLayer(new Integer(layer));
  }


  protected void previewReport(JInternalFrame frame)
  {
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      final URL in = ObjectUtilities.getResourceRelative
              ("component-drawing.xml", ComponentDrawingDemoHandler.class);
      if (in == null)
      {
        return;
      }
      JFreeReport report = generator.parseReport(in);
      report.getReportConfiguration().setConfigProperty
              ("org.jfree.report.AllowOwnPeerForComponentDrawable", "true");
      PaintComponentTableModel tableModel = new PaintComponentTableModel();
      tableModel.addComponent(frame);
      report.setData(tableModel);

      // Important: The dialog must be modal, so that we know, when the report
      // processing is finished.
      final PreviewDialog previewDialog = new PreviewDialog(report, this, true);
      previewDialog.getBase().setToolbarFloatable(true);
      previewDialog.pack();
      RefineryUtilities.positionFrameRandomly(previewDialog);
      previewDialog.setVisible(true);
    }
    catch (Exception e)
    {
      Log.error("Failed to parse the report definition", e);
    }
  }


  private JInternalFrame findSelectedFrame()
  {
    JInternalFrame[] frames = desktop.getAllFrames();
    for (int i = 0; i < frames.length; i++)
    {
      JInternalFrame frame = frames[i];
      if (frame.isSelected())
      {
        return frame;
      }
    }
    return null;
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    InternalFrameDemoFrame frame = new InternalFrameDemoFrame();
    frame.updateFrameSize(50);
    frame.setVisible(true);

  }
}
