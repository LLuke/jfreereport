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
 * -------------
 * CardDemo.java
 * -------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CardDemo.java,v 1.7 2003/06/29 16:59:23 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package org.jfree.report.demo.cards;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.table.TableModel;

import org.jfree.report.demo.JFreeReportDemo;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;

/**
 * A JFreeReport demo.
 *
 * @author Thomas Morgner.
 */
public class CardDemo extends JFreeReportDemo
{
  /**
   * Default constructor.
   */
  public CardDemo()
  {
  }

  /**
   * Creates a <code>TableModel</code> containing data for the demo.
   * <!-- used in JUnit tests -->
   *
   * @return A <code>TableModel</code>.
   */
  public static TableModel createSimpleDemoModel()
  {
    final CardTableModel model = new CardTableModel();
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    return new WrappingTableModel(model, "C1_", "C2_");
  }

  /**
   * Creates a <code>TableModel</code> containing data for the demo.
   *
   * @return A <code>TableModel</code>.
   */
  private TableModel createEmptyStartDemoModel()
  {
    final CardTableModel model = new CardTableModel();
    model.addCard(new NoPrintCard());
    model.addCard(new NoPrintCard());
    model.addCard(new NoPrintCard());
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    return new WrappingTableModel(model, "C1_", "C2_");
  }

  /**
   * Creates a list of the available demos.
   *
   * @return A list.
   */
  protected List createAvailableDemos()
  {
    final ArrayList demos = new ArrayList();

    demos.add(new DemoDefinition("Simple Card printing",
        createSimpleDemoModel(),
        new URLDemoHandler("/org/jfree/report/demo/cards/usercards.xml")));

    demos.add(new DemoDefinition("First 3 cards empty",
        createEmptyStartDemoModel(),
        new URLDemoHandler("/org/jfree/report/demo/cards/usercards.xml")));

    return demos;
  }

  /**
   * The starting point for the demo application.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
        Log.info("Look and feel problem.");
      }

      final CardDemo frame = new CardDemo();
      frame.pack();
      frame.setBounds(100, 100, 700, 400);
      RefineryUtilities.centerFrameOnScreen(frame);
      frame.setVisible(true);
    }
    catch (Throwable th)
    {
      th.printStackTrace();
    }
  }
}
