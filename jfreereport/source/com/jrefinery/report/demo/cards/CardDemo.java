/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * CardDemo.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package com.jrefinery.report.demo.cards;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.TableModel;
import javax.swing.UIManager;

import com.jrefinery.report.demo.JFreeReportDemo;
import com.jrefinery.ui.RefineryUtilities;

public class CardDemo extends JFreeReportDemo
{
  public CardDemo()
  {
  }

  private TableModel createSimpleDemoModel ()
  {
    CardTableModel model = new CardTableModel();
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    return new WrappingTableModel(model, "C1_", "C2_");
  }

  private TableModel createEmptyStartDemoModel ()
  {
    CardTableModel model = new CardTableModel();
    model.addCard(new NoPrintCard());
    model.addCard(new NoPrintCard());
    model.addCard(new NoPrintCard());
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    return new WrappingTableModel(model, "C1_", "C2_");
  }

  protected List createAvailableDemos()
  {
    ArrayList demos = new ArrayList();
    demos.add(new DemoDefinition("Simple Card printing", createSimpleDemoModel(), new URLDemoHandler("/com/jrefinery/report/demo/cards/usercards.xml")));
    demos.add(new DemoDefinition("First 3 cards empty", createEmptyStartDemoModel(), new URLDemoHandler("/com/jrefinery/report/demo/cards/usercards.xml")));
    return demos;
  }

  public static void main (String [] args)
  {
    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
        System.out.println("Look and feel problem.");
      }

      CardDemo frame = new CardDemo();
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
