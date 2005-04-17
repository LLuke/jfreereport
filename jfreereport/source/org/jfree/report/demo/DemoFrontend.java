/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * DemoFrontend.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DemoFrontend.java,v 1.4 2005/04/15 19:52:26 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;

public class DemoFrontend extends AbstractDemoFrame
{
  private static class RunDemoAction
          extends AbstractAction
  {
    private Class target;
    private String description;

    public RunDemoAction (final String name, final Class target,
                          final String description)
    {
      putValue(NAME, name);
      putValue(SHORT_DESCRIPTION, description);
      this.target = target;
      this.description = description;
    }

    public String getDescription ()
    {
      return description;
    }

    public void actionPerformed (final ActionEvent event)
    {
      try
      {
        final String[] args = new String[0];
        final Method m = target.getMethod("main", new Class[]{args.getClass()});
        m.invoke(null, new Object[]{args});
      }
      catch (Exception e)
      {
        // failed to execute ...
        Log.error ("There was an error while trying to start the requested demo", e);
      }
    }
  }

  private RunDemoAction[] demos;

  public DemoFrontend ()
  {
    demos = createDemoActions();
    setTitle("Demo Selector");
    setJMenuBar(createMenuBar());
    setContentPane(createContentPane());
    ReportConfiguration.getGlobalConfig().setConfigProperty
            ("org.jfree.report.demo.Embedded", "true");
  }

  private JPanel createContentPane ()
  {
    final int cols = 2;
    final int rows = (int) Math.ceil(demos.length / (double) cols);

    final JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(rows, cols));

    for (int i = 0; i < demos.length; i++)
    {
//      final GridBagConstraints gbc = new GridBagConstraints();
//      gbc.gridx = 0;
//      gbc.gridy = i;
//      gbc.fill = GridBagConstraints.HORIZONTAL;
//      gbc.insets = new Insets (2, 2, 2, 2);
      panel.add(new ActionButton(demos[i]));

//      gbc = new GridBagConstraints();
//      gbc.gridx = 1;
//      gbc.gridy = i;
//      gbc.fill = GridBagConstraints.HORIZONTAL;
//      gbc.insets = new Insets (2, 2, 2, 2);
//      panel.add (new JLabel (demos[i].getDescription()), gbc);
    }
    return panel;
  }

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  public JMenuBar createMenuBar ()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    for (int i = 0; i < demos.length; i++)
    {
      fileMenu.add(new ActionMenuItem(demos[i]));
    }
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);
    return mb;
  }


  private RunDemoAction[] createDemoActions ()
  {
    final InputStream in = getClass().getResourceAsStream("demos.properties");
    if (in == null)
    {
      Log.warn("Missing resource: demos.properties");
      return null;
    }
    try
    {
      final Properties p = new Properties();
      p.load(in);
      in.close();

      final ArrayList list = new ArrayList();

      final int size = Integer.parseInt(p.getProperty("size"));
      for (int i = 0; i < size; i++)
      {
        final String name = p.getProperty("demo." + i + ".name");
        final String className = p.getProperty("demo." + i + ".class");
        final String description = p.getProperty("demo." + i + ".description", "-");
        if (name == null || className == null)
        {
          continue;
        }
        final Class c = Class.forName(className);
        list.add(new RunDemoAction(name, c, description));
      }

      final RunDemoAction[] retval = (RunDemoAction[])
              list.toArray(new RunDemoAction[list.size()]);
      return retval;
    }
    catch (Exception e)
    {
      Log.warn("Unable to parse demo list: demos.properties", e);
      return null;
    }
  }

  /**
   * Exits the application, but only if the user agrees.
   *
   * @return false if the user decides not to exit the application.
   */
  protected boolean attemptExit ()
  {
    final boolean close =
            JOptionPane.showConfirmDialog(this,
                    getResources().getString("exitdialog.message"),
                    getResources().getString("exitdialog.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    if (close)
    {
      System.exit(0);
    }

    return close;
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview ()
  {
  }

  public static void main (final String[] args)
  {
    final DemoFrontend frontend = new DemoFrontend();
    frontend.pack();
    RefineryUtilities.centerFrameOnScreen(frontend);
    frontend.setVisible(true);
  }
}
