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
 * ------------------------------
 * BarcodeTester.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: BarcodeTester.java,v 1.2 2005/05/25 19:52:51 mimil Exp $
 *
 * Changes (from 2005-05-23)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode.tester;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.dev.barcode.Barcode1D;
import org.jfree.report.dev.barcode.base.Barcode39;
import org.jfree.report.dev.barcode.base.Barcode39Ext;
import org.jfree.report.dev.barcode.base.Barcode93;

//todo: center the barcode
//todo: editors for insets and fontdefinition

public class BarcodeTester extends JFrame
{
  private Class barcodeClass;

  public BarcodeTester ()
  {
    initFrame();

    JScrollPane propertyPane = new JScrollPane();

    getContentPane().add(createInputPanel(), BorderLayout.NORTH);

    getContentPane().add(propertyPane, BorderLayout.WEST);
  }

  private JPanel createInputPanel ()
  {
    final JPanel panel = new JPanel();

    panel.add(createBarcodeBox());

    final JTextField textField = new JTextField(15);
    panel.add(textField);


    final AbstractAction action = new AbstractAction("Apply")
    {
      public void actionPerformed (ActionEvent e)
      {
        final String str = textField.getText();

        if (str != null)
        {
          try
          {
            final Constructor constructor = barcodeClass.getConstructor(new Class[]{String.class});

            Barcode1D barcode1D = (Barcode1D) constructor.newInstance(new Object[]{str});
            /*barcode1D.setBarHeight(200);
            barcode1D.setBarWidth(5);
            barcode1D.setShowCode(false);
            barcode1D.setBarToCodeGap(20);
            barcode1D.setMargins(new Insets(0,0,0,0));
            barcode1D.setStrokeColor(Color.RED);
            barcode1D.setStroke(new BasicStroke(5));
            barcode1D.setFont(new FontDefinition("SansSerif", 20));*/

            final BarcodePanel barcodePanel = new BarcodePanel(barcode1D);
            getContentPane().add(barcodePanel, BorderLayout.CENTER);
            final PropertySheetInspector comp = new PropertySheetInspector(barcode1D);
            getContentPane().add(comp, BorderLayout.WEST);
            comp.addPropertyChangeListener(new PropertyChangeListener()
            {
              public void propertyChange (PropertyChangeEvent evt)
              {
                barcodePanel.repaint();
              }
            });
            BarcodeTester.this.validateTree();
          }
          catch (Exception e1)
          {
            JOptionPane.showMessageDialog(BarcodeTester.this, e1.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    };
    final JButton button = new JButton(action);

    panel.add(button);

    textField.addKeyListener(new KeyListener()
    {
      public void keyTyped (KeyEvent e)
      {
        click(e, action);
      }

      public void keyPressed (KeyEvent e)
      {
        click(e, action);
      }

      public void keyReleased (KeyEvent e)
      {
        click(e, action);
      }

      private void click (KeyEvent e, final AbstractAction action)
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
          action.actionPerformed(null);
        }
      }
    });
    return panel;
  }


  private JComboBox createBarcodeBox ()
  {
    final JComboBox combo = new JComboBox();
    ;

    combo.addItem(Barcode39.class);
    combo.addItem(Barcode39Ext.class);
    combo.addItem(Barcode93.class);

    combo.addActionListener(new ActionListener()
    {
      public void actionPerformed (ActionEvent e)
      {
        barcodeClass = (Class) combo.getSelectedItem();
      }
    });

    combo.setSelectedIndex(0);

    return combo;
  }

  private void initFrame ()
  {
    setTitle("Barcode Tester");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main (String[] args)
  {
    JFreeReportBoot.getInstance().start();

    new BarcodeTester().setVisible(true);
  }
}
