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
 * ConfigDescriptionEditor.java
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
 * 26.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionRadioButton;
import org.jfree.report.modules.gui.config.resources.ConfigResources;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.ExtensionFileFilter;

public class ConfigDescriptionEditor extends JFrame
{
  private class CloseAction extends AbstractActionDowngrade
  {
    public CloseAction()
    {
      putValue(NAME, "Exit");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      System.exit(0);
    }
  }

  private class SaveAction extends AbstractActionDowngrade
  {
    public SaveAction()
    {
      putValue(NAME, resources.getString("action.save.name"));
      putValue(SMALL_ICON, resources.getObject("action.save.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      save();
    }
  }

  private class ImportAction extends AbstractActionDowngrade
  {
    public ImportAction()
    {
      putValue(NAME, resources.getString("action.import.name"));
      putValue(SMALL_ICON, resources.getObject("action.import.small-icon"));

    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      model.importFromConfig(ReportConfiguration.getGlobalConfig());
    }
  }

  private class AddEntryAction extends AbstractActionDowngrade
  {
    public AddEntryAction()
    {
      putValue(NAME, resources.getString("action.add-entry.name"));
      putValue(SMALL_ICON, resources.getObject("action.add-entry.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      model.add(new TextConfigDescriptionEntry("<unnamed entry>"));
    }
  }

  private class RemoveEntryAction extends AbstractActionDowngrade
  {
    public RemoveEntryAction()
    {
      putValue(NAME, resources.getString("action.remove-entry.name"));
      putValue(SMALL_ICON, resources.getObject("action.remove-entry.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      int[] selectedEntries = entryList.getSelectedIndices();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        model.remove(model.get(i));
      }
      entryList.clearSelection();
    }
  }

  private class LoadAction extends AbstractActionDowngrade
  {
    public LoadAction()
    {
      putValue(NAME, resources.getString("action.load.name"));
      putValue(SMALL_ICON, resources.getObject("action.load.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      load();
    }
  }

  private class UpdateAction extends AbstractActionDowngrade
  {
    public UpdateAction()
    {
      putValue(NAME, "Update");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      updateSelectedEntry();
    }
  }

  private class CancelAction extends AbstractActionDowngrade
  {
    public CancelAction()
    {
      putValue(NAME, "Cancel");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      ConfigDescriptionEntry ce = getSelectedEntry();
      setSelectedEntry(null);
      setSelectedEntry(ce);
    }
  }

  private class SelectTypeAction extends AbstractActionDowngrade
  {
    private int type;

    public SelectTypeAction(String name, int type)
    {
      putValue(NAME, name);
      this.type = type;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      setEntryType(type);
    }
  }

  private class ConfigListSelectionListener implements ListSelectionListener
  {
    public ConfigListSelectionListener()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e)
    {
      if (entryList.getSelectedIndex() == -1)
      {
        setSelectedEntry(null);
      }
      else
      {
        setSelectedEntry(model.get(entryList.getSelectedIndex()));
      }
    }
  }

  private class EnumerationListSelectionHandler implements ListSelectionListener
  {
    public EnumerationListSelectionHandler()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e)
    {
      if (enumEntryList.getSelectedIndex() == -1)
      {
        enumEntryEditField.setText("");
      }
      else
      {
        enumEntryEditField.setText((String) enumEntryListModel.get
            (enumEntryList.getSelectedIndex()));
      }
    }
  }

  private class AddEnumEntryAction extends AbstractActionDowngrade
  {
    public AddEnumEntryAction()
    {
      putValue(NAME, "Add");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      enumEntryListModel.addElement(enumEntryEditField.getText());
    }
  }

  private class RemoveEnumEntryAction extends AbstractActionDowngrade
  {
    public RemoveEnumEntryAction()
    {
      putValue(NAME, "Remove");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      int[] selectedEntries = enumEntryList.getSelectedIndices();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        enumEntryListModel.remove(i);
      }
      enumEntryList.clearSelection();
    }
  }

  private class UpdateEnumEntryAction extends AbstractActionDowngrade
  {
    public UpdateEnumEntryAction()
    {
      putValue(NAME, "Update");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      int idx = enumEntryList.getSelectedIndex();
      if (idx == -1)
      {
        enumEntryListModel.addElement(enumEntryEditField.getText());
      }
      else
      {
        enumEntryListModel.setElementAt(enumEntryEditField.getText(), idx);
      }
    }
  }


  private static final int TYPE_TEXT = 0;
  private static final int TYPE_CLASS = 1;
  private static final int TYPE_ENUM = 2;
  private static final String RESOURCE_BUNDLE =
      ConfigResources.class.getName();

  private ActionRadioButton rbText;
  private ActionRadioButton rbClass;
  private ActionRadioButton rbEnum;

  private AddEntryAction addEntryAction;
  private RemoveEntryAction removeEntryAction;
  private ImportAction importAction;
  private LoadAction loadAction;
  private CloseAction closeAction;
  private SaveAction saveAction;
  private UpdateAction updateAction;
  private CancelAction cancelAction;
  private ConfigDescriptionModel model;

  private JTextField keyNameField;
  private JTextArea descriptionField;
  private JCheckBox globalField;

  private JTextField baseClassField;
  private JLabel baseClassValidateMessage;
  private JTextField enumEntryEditField;
  private DefaultListModel enumEntryListModel;

  private ResourceBundle resources;
  private JPanel enumerationEditor;
  private JPanel classEditor;
  private JPanel textEditor;

  private JPanel detailEditorPane;
  private JList entryList;

  private JList enumEntryList;

  private ConfigDescriptionEntry selectedEntry;
  private JFileChooser fileChooser;


  /**
   * Constructs a new frame that is initially invisible.
   */
  public ConfigDescriptionEditor()
  {
    this.resources = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    setTitle("Configuration Definition Editor");
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());

    detailEditorPane = createEditPane();
    JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, createEntryList(), detailEditorPane);

    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);

    setContentPane(contentPane);
    setEntryType(TYPE_TEXT);
    setSelectedEntry(null);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(
        new ExtensionFileFilter
            ("XML files", ".xml"));
    fileChooser.setMultiSelectionEnabled(false);
  }

  private JPanel createEntryList ()
  {
    addEntryAction = new AddEntryAction();
    removeEntryAction = new RemoveEntryAction();

    model = new ConfigDescriptionModel();
    entryList = new JList(model);
    entryList.addListSelectionListener(new ConfigListSelectionListener());

    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(addEntryAction);
    toolbar.add(removeEntryAction);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(toolbar, BorderLayout.NORTH);
    panel.add(new JScrollPane(entryList), BorderLayout.CENTER);
    return panel;
  }

  private JPanel createButtonPane ()
  {
    closeAction = new CloseAction();
    saveAction = new SaveAction();
    loadAction = new LoadAction();
    importAction = new ImportAction();

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder (5,5,5,5));

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1,4));
    buttonHolder.add (new JButton(importAction));
    buttonHolder.add (new JButton(loadAction));
    buttonHolder.add (new JButton(saveAction));
    buttonHolder.add (new JButton (closeAction));

    panel.add(buttonHolder);
    return panel;
  }

  private JPanel createEditPane()
  {
    updateAction = new UpdateAction();
    cancelAction = new CancelAction();

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1,4));
    buttonHolder.add (new JButton(cancelAction));
    buttonHolder.add (new JButton(updateAction));

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(new EmptyBorder (5,5,5,5));
    buttonPanel.add(buttonHolder);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(createDetailEditorPanel(), BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
  }

  private JPanel createEnumerationEditor ()
  {
    enumEntryEditField = new JTextField();
    enumEntryListModel = new DefaultListModel();

    enumEntryList = new JList(enumEntryListModel);
    enumEntryList.addListSelectionListener(new EnumerationListSelectionHandler());

    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BorderLayout());
    listPanel.add(enumEntryEditField, BorderLayout.NORTH);
    listPanel.add(new JScrollPane(enumEntryList), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(3,1));
    buttonPanel.add(new ActionButton(new AddEnumEntryAction()));
    buttonPanel.add(new ActionButton(new RemoveEnumEntryAction()));
    buttonPanel.add(new ActionButton(new UpdateEnumEntryAction()));

    JPanel buttonCarrier = new JPanel();
    buttonCarrier.setLayout(new FlowLayout());
    buttonCarrier.add (buttonPanel);

    JPanel editorPanel = new JPanel();
    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(listPanel, BorderLayout.CENTER);
    editorPanel.add(buttonCarrier, BorderLayout.EAST);
    return editorPanel;
  }

  private JPanel createClassEditor ()
  {
    baseClassField = new JTextField();
    baseClassValidateMessage = new JLabel(" ");

    JLabel textLabel = new JLabel
        ("BaseClass:");
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(textLabel, BorderLayout.WEST);
    panel.add(baseClassField, BorderLayout.CENTER);
    panel.add(baseClassValidateMessage, BorderLayout.SOUTH);

    JPanel carrier = new JPanel();
    carrier.setLayout(new BorderLayout());
    carrier.add (panel, BorderLayout.NORTH);
    return carrier;
  }

  private JPanel createTextEditor ()
  {
    JLabel textLabel = new JLabel
        ("The Text configuration entry does not require any settings.");
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add (textLabel);
    return panel;
  }

  private int parseInt (String value, int defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  private JPanel createDetailEditorPanel ()
  {
    JLabel keyNameLabel = new JLabel("KeyName:");
    JLabel descriptionLabel = new JLabel("Description");
    JLabel typeLabel = new JLabel("Type:");
    JLabel globalLabel = new JLabel("Global:");

    globalField = new JCheckBox();
    String font = ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.modules.gui.config.EditorFont", "Monospaced");
    int fontSize = parseInt
        (ReportConfiguration.getGlobalConfig().getConfigProperty
          ("org.jfree.report.modules.gui.config.EditorFontSize"), 12);
    descriptionField = new JTextArea();
    descriptionField.setFont(new Font (font, Font.PLAIN, fontSize));
    descriptionField.setLineWrap(true);
    descriptionField.setWrapStyleWord(true);
    keyNameField = new JTextField();

    enumerationEditor = createEnumerationEditor();
    textEditor = createTextEditor();
    classEditor = createClassEditor();

    JPanel detailEditorPanel = new JPanel();
    detailEditorPanel.setBorder(BorderFactory.createLineBorder(Color.green));
    detailEditorPanel.setLayout(new CardLayout ());
    detailEditorPanel.add (classEditor, "Class");
    detailEditorPanel.add (textEditor, "Text");
    detailEditorPanel.add (enumerationEditor, "Enumeration");

    JPanel commonEntryEditorPanel = new JPanel();
    commonEntryEditorPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    commonEntryEditorPanel.add(keyNameLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(keyNameField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    commonEntryEditorPanel.add(descriptionLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    gbc.ipady = 120;
    commonEntryEditorPanel.add(new JScrollPane
        (descriptionField,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    commonEntryEditorPanel.add(globalLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(globalField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    commonEntryEditorPanel.add(typeLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(createTypeSelectionPane(), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(detailEditorPanel, gbc);

    commonEntryEditorPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
    return commonEntryEditorPanel;
  }

  private JPanel createTypeSelectionPane ()
  {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createLineBorder(Color.yellow));
    panel.setLayout(new GridLayout(3,1));

    rbText = new ActionRadioButton (new SelectTypeAction("Text", TYPE_TEXT));
    rbClass = new ActionRadioButton (new SelectTypeAction("Class", TYPE_CLASS));
    rbEnum = new ActionRadioButton (new SelectTypeAction("Enum", TYPE_ENUM));

    ButtonGroup bg = new ButtonGroup();
    bg.add (rbText);
    bg.add (rbClass);
    bg.add (rbEnum);

    panel.add (rbText);
    panel.add (rbClass);
    panel.add (rbEnum);

    return panel;
  }

  public void setEntryType (int type)
  {
    classEditor.setVisible(type == TYPE_CLASS);
    enumerationEditor.setVisible(type == TYPE_ENUM);
    textEditor.setVisible(type == TYPE_TEXT);
    if (type == TYPE_CLASS)
    {
      rbClass.setSelected(true);
    }
    else if (type == TYPE_ENUM)
    {
      rbEnum.setSelected(true);
    }
    else
    {
      rbText.setSelected(true);
    }
    invalidate();
  }

  public int getEntryType ()
  {
    if (classEditor.isVisible())
    {
      return TYPE_CLASS;
    }
    if (enumerationEditor.isVisible())
    {
      return TYPE_ENUM;
    }
    return TYPE_TEXT;
  }

  public ConfigDescriptionEntry getSelectedEntry()
  {
    return selectedEntry;
  }

  public void setSelectedEntry(ConfigDescriptionEntry selectedEntry)
  {
    this.selectedEntry = selectedEntry;

    enumEntryEditField.setText("");
    enumEntryListModel.clear();
    baseClassField.setText("");

    if (this.selectedEntry == null)
    {
      deepEnable(detailEditorPane, false);
    }
    else
    {
      deepEnable(detailEditorPane, true);
      keyNameField.setText(selectedEntry.getKeyName());
      globalField.setSelected(selectedEntry.isGlobal());
      descriptionField.setText(selectedEntry.getDescription());
      if (selectedEntry instanceof ClassConfigDescriptionEntry)
      {
        ClassConfigDescriptionEntry ce = (ClassConfigDescriptionEntry) selectedEntry;
        setEntryType(TYPE_CLASS);
        if (ce.getBaseClass() != null)
        {
          baseClassField.setText(ce.getBaseClass().getName());
        }
      }
      else if (selectedEntry instanceof EnumConfigDescriptionEntry)
      {
        EnumConfigDescriptionEntry enum = (EnumConfigDescriptionEntry) selectedEntry;
        String[] enums = enum.getOptions();
        for (int i= 0; i < enums.length; i++)
        {
          enumEntryListModel.addElement(enums[i]);
        }
        setEntryType(TYPE_ENUM);
      }
      else
      {
        setEntryType(TYPE_TEXT);
      }
    }
  }



  public void deepEnable (Component comp, boolean state)
  {
    comp.setEnabled(state);
    if (comp instanceof Container)
    {
      Container cont = (Container) comp;
      Component[] childs = cont.getComponents();
      for (int i = 0; i < childs.length; i++)
      {
        deepEnable(childs[i], state);
      }
    }
  }

  public void save ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(fileChooser.getSelectedFile()));
        model.save(out, "ISO-8859-1");
        out.close();
      }
      catch (Exception ioe)
      {
        Log.debug ("Failed", ioe);
      }
    }
  }

  public void load ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        InputStream in = new BufferedInputStream(new FileInputStream(fileChooser.getSelectedFile()));
        model.load(in);
        in.close();
      }
      catch (Exception ioe)
      {
        Log.debug ("Failed", ioe);
      }
    }
  }

  public void updateSelectedEntry ()
  {
    ConfigDescriptionEntry entry;
    switch (getEntryType())
    {
      case TYPE_CLASS:
        {
          ClassConfigDescriptionEntry ce =
              new ClassConfigDescriptionEntry(keyNameField.getText());
          ce.setDescription(descriptionField.getText());
          ce.setGlobal(globalField.isSelected());
          try
          {
            Class c = this.getClass().getClassLoader().loadClass(baseClassField.getText());
            ce.setBaseClass(c);
          }
          catch (Exception e)
          {
            // invalid
            Log.debug ("Class is invalid.");
          }
          entry = ce;
          break;
        }
      case TYPE_ENUM:
        {
          EnumConfigDescriptionEntry ece =
              new EnumConfigDescriptionEntry(keyNameField.getText());
          ece.setDescription(descriptionField.getText());
          ece.setGlobal(globalField.isSelected());
          String[] enumEntries = new String[enumEntryListModel.getSize()];
          for (int i = 0; i < enumEntryListModel.getSize(); i++)
          {
            enumEntries[i] = String.valueOf(enumEntryListModel.get(i));
          }
          ece.setOptions(enumEntries);
          entry = ece;
          break;
        }
      default:
        {
          TextConfigDescriptionEntry te =
              new TextConfigDescriptionEntry(keyNameField.getText());
          te.setDescription(descriptionField.getText());
          te.setGlobal(globalField.isSelected());
          entry = te;
          break;
        }
    }
    if (model.contains(entry))
    {
      model.remove(entry);
    }
    model.remove(this.selectedEntry);
    model.add(entry);
    model.sort();
    entryList.setSelectedIndex(model.indexOf(entry));
  }

  public static void main(String[] args)
  {
    ConfigDescriptionEditor ed = new ConfigDescriptionEditor();
    ed.pack();
    ed.setVisible(true);
  }
}
