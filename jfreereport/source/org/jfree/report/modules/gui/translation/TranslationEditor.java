/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * TranslationEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TranslationEditor.java,v 1.3 2003/11/07 18:33:54 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.09.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.translation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TranslationEditor extends JFrame
{
  /**
   * Handles the list selection in the list of available config keys.
   */
  private class ConfigListSelectionListener implements ListSelectionListener
  {
    /**
     * Defaultconstructor.
     */
    public ConfigListSelectionListener()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(final ListSelectionEvent e)
    {
      if (entryList.getSelectedIndex() == -1)
      {
//        setSelectedEntry(null);
      }
      else
      {
//        setSelectedEntry(model.get(entryList.getSelectedIndex()));
      }
    }
  }

  private JLabel resourceKey;
  private JTextField translatedValue;
  private JTextField defaultValue;
  private JTextField commentValue;
  private ListModel model;
  private ListModel translationsModel;
  private JList translationList;
  private JList entryList;

  /**
   * Constructs a new Frame that is initially invisible.
   *
   * @see Component#setSize
   * @see Component#setVisible
   */
  public TranslationEditor()
  {
    initialize();
  }

  private void initialize ()
  {
    translatedValue = new JTextField();
    defaultValue = new JTextField();
    defaultValue.setEditable(false);
    commentValue = new JTextField();
    commentValue.setEditable(false);
    resourceKey = new JLabel("Just-another-key.value");

    translationsModel = new DefaultListModel();
    translationList = new JList(translationsModel);
    //translationList.addListSelectionListener();
    final JScrollPane translationListContainer = new JScrollPane
        (translationList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    model = new DefaultListModel();
    entryList = new JList(model);
    entryList.addListSelectionListener(new ConfigListSelectionListener());

    final JScrollPane entryListContainer = new JScrollPane
        (entryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    final JSplitPane listContainer = new JSplitPane
        (JSplitPane.VERTICAL_SPLIT, translationListContainer, entryListContainer);


    final JPanel detailEditorPane = new JPanel();
    detailEditorPane.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    detailEditorPane.add(resourceKey, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    detailEditorPane.add (new JLabel ("Comment: "), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    detailEditorPane.add(commentValue, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    detailEditorPane.add (new JLabel ("Default: "), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    detailEditorPane.add(defaultValue, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    detailEditorPane.add (new JLabel ("Translation: "), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    detailEditorPane.add(translatedValue, gbc);

    final JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, listContainer, detailEditorPane);

    setContentPane(splitPane);
  }

  public static void main(final String[] args)
  {
    final TranslationEditor ed = new TranslationEditor();
    ed.pack();
    ed.setVisible(true);
  }
}
