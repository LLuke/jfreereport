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
 * $Id: ConfigDescriptionEditor.java,v 1.7 2003/09/14 19:24:07 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionRadioButton;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionModel;
import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.TextConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.resources.ConfigResources;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.ExtensionFileFilter;

/**
 * The config description editor is used to edit the configuration
 * metadata used in the ConfigEditor to describe the ReportConfiguration
 * keys.
 * 
 * @author Thomas Morgner
 */
public class ConfigDescriptionEditor extends JFrame
{
  /** An internal constant to activate the class detail editor. */
  private static final String CLASS_DETAIL_EDITOR_NAME = "Class";
  /** An internal constant to activate the enumeration detail editor. */
  private static final String ENUM_DETAIL_EDITOR_NAME = "Enum";
  /** An internal constant to activate the text detail editor. */
  private static final String TEXT_DETAIL_EDITOR_NAME = "Text";

  /**
   * Handles close requests in this editor. 
   */
  private class CloseAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public CloseAction()
    {
      putValue(NAME, resources.getString("action.exit.name"));
    }

    /**
     * Handles the close request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      attempExit();
    }
  }

  /**
   * Handles save requests in this editor. 
   */
  private class SaveAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public SaveAction()
    {
      putValue(NAME, resources.getString("action.save.name"));
      putValue(SMALL_ICON, resources.getObject("action.save.small-icon"));
    }

    /**
     * Handles the save request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      save();
    }
  }

  /**
   * Handles import requests in this editor. Imports try to build a 
   * new description model from a given report configuration. 
   */
  private class ImportAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public ImportAction()
    {
      putValue(NAME, resources.getString("action.import.name"));
      putValue(SMALL_ICON, resources.getObject("action.import.small-icon"));

    }

    /**
     * Handles the import request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      model.importFromConfig(ReportConfiguration.getGlobalConfig());
      model.sort();
      setStatusText(resources.getString ("config-description-editor.import-complete"));
    }
  }

  /**
   * Handles requests to add a new entry in this editor. 
   */
  private class AddEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public AddEntryAction()
    {
      putValue(NAME, resources.getString("action.add-entry.name"));
      putValue(SMALL_ICON, resources.getObject("action.add-entry.small-icon"));
    }

    /**
     * Handles the add-entry request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      TextConfigDescriptionEntry te = 
        new TextConfigDescriptionEntry
          (resources.getString ("config-description-editor.unnamed-entry"));
      model.add(te);
      entryList.setSelectedIndex(model.getSize() - 1);
    }
  }

  /**
   * Handles requests to remove an entry from this editor. 
   */
  private class RemoveEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public RemoveEntryAction()
    {
      putValue(NAME, resources.getString("action.remove-entry.name"));
      putValue(SMALL_ICON, resources.getObject("action.remove-entry.small-icon"));
    }

    /**
     * Handles the remove entry request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      int[] selectedEntries = entryList.getSelectedIndices();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        model.remove(model.get(selectedEntries[i]));
      }
      entryList.clearSelection();
    }
  }

  /**
   * Handles load requests in this editor. 
   */
  private class LoadAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public LoadAction()
    {
      putValue(NAME, resources.getString("action.load.name"));
      putValue(SMALL_ICON, resources.getObject("action.load.small-icon"));
    }

    /**
     * Handles the laod request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      load();
    }
  }

  /**
   * Handles update requests in the detail editor. 
   */
  private class UpdateAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public UpdateAction()
    {
      putValue(NAME, resources.getString ("action.update.name"));
    }

    /**
     * Handles the update request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      updateSelectedEntry();
    }
  }

  /**
   * Handles cancel requests in the detail editor. 
   */
  private class CancelAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public CancelAction()
    {
      putValue(NAME, resources.getString ("action.cancel.name"));
    }

    /**
     * Handles the cancel request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      ConfigDescriptionEntry ce = getSelectedEntry();
      setSelectedEntry(null);
      setSelectedEntry(ce);
    }
  }

  /**
   * Handles editor type selections within the detail editor. 
   */
  private class SelectTypeAction extends AbstractActionDowngrade
  {
    /** the selected type. */
    private final int type;

    /**
     * Creates a new select type action for the given name and type.
     * 
     * @param name the name of the action.
     * @param type the type that should be selected whenever this action
     * gets called.
     */
    public SelectTypeAction(String name, int type)
    {
      putValue(NAME, name);
      this.type = type;
    }

    /**
     * Handles the select type request.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      setEntryType(type);
    }
  }

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

  /**
   * Handles list selections in the enumeration detail editor. 
   */
  private class EnumerationListSelectionHandler implements ListSelectionListener
  {
    /**
     * Defaultconstructor.
     */
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

  /**
   * A ShortCut action to redefine the entries of the enumeration detail
   * editor to represent a boolean value.
   */
  private class SetBooleanEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public SetBooleanEnumEntryAction()
    {
      putValue(NAME, resources.getString ("action.boolean.name"));
    }

    /**
     * Handles the boolean redefinition request.
     * @param e not used. 
     */
    public void actionPerformed(ActionEvent e)
    {
      enumEntryListModel.clear();
      enumEntryEditField.setText("");
      enumEntryListModel.addElement("true");
      enumEntryListModel.addElement("false");
    }
  }

  /**
   * Handles the request to add a new enumeration entry to the detail
   * editor.
   */
  private class AddEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public AddEnumEntryAction()
    {
      putValue(NAME, resources.getString("action.add-enum-entry.name"));
    }

    /**
     * Handles the request to add a new enumeration entry to the detail
     * editor.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      enumEntryListModel.addElement(enumEntryEditField.getText());
    }
  }

  /**
   * Handles the request to remove an enumeration entry to the detail
   * editor.
   */
  private class RemoveEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public RemoveEnumEntryAction()
    {
      putValue(NAME, resources.getString("action.remove-enum-entry.name"));
    }

    /**
     * Handles the request to remove an enumeration entry to the detail
     * editor.
     * 
     * @param e not used.
     */
    public void actionPerformed(ActionEvent e)
    {
      int[] selectedEntries = enumEntryList.getSelectedIndices();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        enumEntryListModel.remove(selectedEntries[i]);
      }
      enumEntryList.clearSelection();
    }
  }

  /**
   * Handles the request to update an enumeration entry to the detail
   * editor.
   */
  private class UpdateEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public UpdateEnumEntryAction()
    {
      putValue(NAME, resources.getString("action.update-enum-entry.name"));
    }

    /**
     * Handles the request to update an enumeration entry to the detail
     * editor.
     * 
     * @param e not used.
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

  /** An internal value to mark a text detail editor type. */
  private static final int TYPE_TEXT = 0;
  /** An internal value to mark a class detail editor type. */
  private static final int TYPE_CLASS = 1;
  /** An internal value to mark a enumeration detail editor type. */
  private static final int TYPE_ENUM = 2;
  /** 
   * Contains the name of the resource bundle to be used to translate the 
   * dialogs.
   */
  private static final String RESOURCE_BUNDLE =
      ConfigResources.class.getName();

  /** A radio button to select the text editor type for the current key. */ 
  private ActionRadioButton rbText;
  /** A radio button to select the class editor type for the current key. */ 
  private ActionRadioButton rbClass;
  /** A radio button to select the enumeration editor type for the current key. */ 
  private ActionRadioButton rbEnum;
  /** The list model used to collect and manage all available keys. */
  private ConfigDescriptionModel model;
  /** The name of the currently edited key. */
  private JTextField keyNameField;
  /** The description field contains a short description of the current key. */
  private JTextArea descriptionField;
  /** Allows to check, whether the key is a global (boot-time) key. */ 
  private JCheckBox globalField;
  /** Allows to check, whether the key is hidden. */
  private JCheckBox hiddenField;
  /** The name of the base class for the class detail editor. */
  private JTextField baseClassField;
  /** contains a message after validating the given base class. */ 
  private JLabel baseClassValidateMessage;
  /** contains the currently selected entry of the enumeration detail editor. */
  private JTextField enumEntryEditField;
  /** contains all entries of the enumeration detail editor. */
  private DefaultListModel enumEntryListModel;
  /** The current resource bundle used to translate the strings in this dialog. */
  private ResourceBundle resources;
  /** This cardlayout is used to display the currently selected detail editor. */
  private CardLayout detailManager;
  /** Contains the detail editor manager. */
  private JPanel detailManagerPanel;
  /** Contains the detail editor for the key. */
  private JPanel detailEditorPane;
  /** The list is used to manage all available keys. */
  private JList entryList;
  /** This list is used to manage the available entries of the enumeration detail editor. */
  private JList enumEntryList;
  /**the currently selected description entry. */
  private ConfigDescriptionEntry selectedEntry;
  /** The file chooser is used to select the file for the load/save operations. */
  private JFileChooser fileChooser;
  /** Serves as statusline for the dialog. */
  private JLabel statusHolder;
  /** The currently selected detail editor type. */
  private int type;


  /**
   * Constructs a ConfigDescriptionEditor that is initially invisible.
   */
  public ConfigDescriptionEditor()
  {
    this.resources = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    setTitle(resources.getString("config-description-editor.title"));
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());

    detailEditorPane = createEditPane();
    JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, createEntryList(), detailEditorPane);

    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);

    JPanel cPaneStatus = new JPanel();
    cPaneStatus.setLayout(new BorderLayout());
    cPaneStatus.add (contentPane, BorderLayout.CENTER);
    cPaneStatus.add (createStatusBar(), BorderLayout.SOUTH);

    setContentPane(cPaneStatus);
    setEntryType(TYPE_TEXT);
    setSelectedEntry(null);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(
        new ExtensionFileFilter
            (resources.getString("config-description-editor.xml-files"), ".xml"));
    fileChooser.setMultiSelectionEnabled(false);

    setStatusText(resources.getString("config-description-editor.welcome"));

    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        attempExit();
      }
    });
  }

  /**
   * Creates and returns the entry list component that will hold all
   * config description entries within a list.
   * 
   * @return the created entry list.
   */
  private JPanel createEntryList ()
  {
    Action addEntryAction = new AddEntryAction();
    Action removeEntryAction = new RemoveEntryAction();

    model = new ConfigDescriptionModel();
    entryList = new JList(model);
    entryList.addListSelectionListener(new ConfigListSelectionListener());

    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(addEntryAction);
    toolbar.add(removeEntryAction);

    JPanel panel = new JPanel();
    panel.setMinimumSize(new Dimension(200,0));
    panel.setLayout(new BorderLayout());
    panel.add(toolbar, BorderLayout.NORTH);
    panel.add(new JScrollPane
        (entryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
    return panel;
  }

  /**
   * Creates a panel containing all dialog control buttons, like close,
   * load, save and import.
   * 
   * @return the button panel.
   */
  private JPanel createButtonPane ()
  {
    Action closeAction = new CloseAction();
    Action saveAction = new SaveAction();
    Action loadAction = new LoadAction();
    Action importAction = new ImportAction();

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder (5,5,5,5));

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1,4));
    buttonHolder.add (new ActionButton(importAction));
    buttonHolder.add (new ActionButton(loadAction));
    buttonHolder.add (new ActionButton(saveAction));
    buttonHolder.add (new ActionButton (closeAction));

    panel.add(buttonHolder);
    return panel;
  }

  /**
   * Creates the detail editor panel. This panel will contain
   * all specific editors for the keys.
   * @return the detail editor panel.
   */
  private JPanel createEditPane()
  {
    Action updateAction = new UpdateAction();
    Action cancelAction = new CancelAction();

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1,4));
    buttonHolder.add (new ActionButton(cancelAction));
    buttonHolder.add (new ActionButton(updateAction));

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

  /**
   * Creates the enumeration detail editor.
   * @return the enumeration detail editor.
   */
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
    buttonPanel.setLayout(new GridLayout(5,1));
    buttonPanel.add(new ActionButton(new AddEnumEntryAction()));
    buttonPanel.add(new ActionButton(new RemoveEnumEntryAction()));
    buttonPanel.add(new ActionButton(new UpdateEnumEntryAction()));
    buttonPanel.add(new JPanel());
    buttonPanel.add(new ActionButton(new SetBooleanEnumEntryAction()));

    JPanel buttonCarrier = new JPanel();
    buttonCarrier.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    buttonCarrier.add (buttonPanel);

    JPanel editorPanel = new JPanel();
    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(listPanel, BorderLayout.CENTER);
    editorPanel.add(buttonCarrier, BorderLayout.EAST);
    return editorPanel;
  }

  /**
   * Creates the class detail editor.
   * @return the class detail editor.
   */
  private JPanel createClassEditor ()
  {
    baseClassField = new JTextField();
    baseClassValidateMessage = new JLabel(" ");

    JLabel textLabel = new JLabel
        (resources.getString ("config-description-editor.baseclass"));
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

  /**
   * Creates the text detail editor.
   * @return the text detail editor.
   */
  private JPanel createTextEditor ()
  {
    JLabel textLabel = new JLabel
        (resources.getString ("config-description-editor.text-editor-message"));
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add (textLabel);
    return panel;
  }

  /**
   * Creates the common entry detail editor. This editor contains all
   * shared properties.
   * 
   * @return the common entry editor.
   */
  private JPanel createDetailEditorPanel ()
  {
    JLabel keyNameLabel = new JLabel(resources.getString ("config-description-editor.keyname"));
    JLabel descriptionLabel = new JLabel
      (resources.getString ("config-description-editor.description"));
    JLabel typeLabel = new JLabel(resources.getString ("config-description-editor.type"));
    JLabel globalLabel = new JLabel(resources.getString ("config-description-editor.global"));
    JLabel hiddenLabel = new JLabel(resources.getString ("config-description-editor.hidden"));

    hiddenField = new JCheckBox();
    globalField = new JCheckBox();
    String font = ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.modules.gui.config.EditorFont", "Monospaced");
    int fontSize = StringUtil.parseInt
        (ReportConfiguration.getGlobalConfig().getConfigProperty
          ("org.jfree.report.modules.gui.config.EditorFontSize"), 12);
    descriptionField = new JTextArea();
    descriptionField.setFont(new Font (font, Font.PLAIN, fontSize));
    descriptionField.setLineWrap(true);
    descriptionField.setWrapStyleWord(true);
    keyNameField = new JTextField();

    JPanel enumerationEditor = createEnumerationEditor();
    JPanel textEditor = createTextEditor();
    JPanel classEditor = createClassEditor();

    detailManagerPanel = new JPanel();
    detailManager = new CardLayout ();
    detailManagerPanel.setLayout(detailManager);
    detailManagerPanel.add (classEditor, CLASS_DETAIL_EDITOR_NAME);
    detailManagerPanel.add (textEditor, TEXT_DETAIL_EDITOR_NAME);
    detailManagerPanel.add (enumerationEditor, ENUM_DETAIL_EDITOR_NAME);

    JPanel commonEntryEditorPanel = new JPanel();
    commonEntryEditorPanel.setLayout(new GridBagLayout());
    commonEntryEditorPanel.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));

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
    commonEntryEditorPanel.add(hiddenLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(hiddenField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    commonEntryEditorPanel.add(typeLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(createTypeSelectionPane(), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    commonEntryEditorPanel.add(detailManagerPanel, gbc);

    return commonEntryEditorPanel;
  }

  /**
   * Creates the type selection panel containing some radio buttons to
   * define the detail editor type.
   * 
   * @return the type selection panel.
   */
  private JPanel createTypeSelectionPane ()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3,1));

    rbText = new ActionRadioButton (new SelectTypeAction
      (resources.getString ("config-description-editor.type-text"), TYPE_TEXT));
    rbClass = new ActionRadioButton (new SelectTypeAction
      (resources.getString ("config-description-editor.type-class"), TYPE_CLASS));
    rbEnum = new ActionRadioButton (new SelectTypeAction
      (resources.getString ("config-description-editor.type-enum"), TYPE_ENUM));

    ButtonGroup bg = new ButtonGroup();
    bg.add (rbText);
    bg.add (rbClass);
    bg.add (rbEnum);

    panel.add (rbText);
    panel.add (rbClass);
    panel.add (rbEnum);

    return panel;
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar()
  {
    final JPanel statusPane = new JPanel();
    statusPane.setLayout(new BorderLayout());
    statusPane.setBorder(
        BorderFactory.createLineBorder(UIManager.getDefaults().getColor("controlShadow")));
    statusHolder = new JLabel(" ");
    statusPane.setMinimumSize(statusHolder.getPreferredSize());
    statusPane.add(statusHolder, BorderLayout.WEST);

    return statusPane;
  }

  /**
   * Defines the status text for this dialog.
   * 
   * @param text the new status text.
   */
  private void setStatusText (String text)
  {
    statusHolder.setText(text);
  }

//  private String getStatusText ()
//  {
//    return statusHolder.getText();
//  }

  /**
   * Sets the entry type for the current config description entry. This
   * also selects and activates the correct detail editor for this type.
   * 
   * @param type the type of the currently selected entry.
   */
  private void setEntryType (int type)
  {
    this.type = type;
    if (type == TYPE_CLASS)
    {
      detailManager.show(detailManagerPanel, CLASS_DETAIL_EDITOR_NAME);
      rbClass.setSelected(true);
    }
    else if (type == TYPE_ENUM)
    {
      detailManager.show(detailManagerPanel, ENUM_DETAIL_EDITOR_NAME);
      rbEnum.setSelected(true);
    }
    else
    {
      detailManager.show(detailManagerPanel, TEXT_DETAIL_EDITOR_NAME);
      rbText.setSelected(true);
    }
    invalidate();
  }

  /**
   * Returns the current entry type.
   * @return the current entry type.
   */
  private int getEntryType ()
  {
    return type;
  }

  /**
   * Returns the currently select entry from the entry list model.
   * 
   * @return the currently selected entry.
   */
  private ConfigDescriptionEntry getSelectedEntry()
  {
    return selectedEntry;
  }

  /**
   * Defines the currently selected entry from the entry list model and
   * updates the detail editor to reflect the data from the entry.
   * 
   * @param selectedEntry the selected entry.
   */
  private void setSelectedEntry(ConfigDescriptionEntry selectedEntry)
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
      hiddenField.setSelected(selectedEntry.isHidden());
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

  /**
   * A utility method to enable or disable a component and all childs.
   * @param comp the component that should be enabled or disabled.
   * @param state the new enable state.
   */
  private void deepEnable (Component comp, boolean state)
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

  /**
   * Saves the config description model in a xml file.
   */
  private void save ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        OutputStream out = new BufferedOutputStream
          (new FileOutputStream(fileChooser.getSelectedFile()));
        model.save(out, "ISO-8859-1");
        out.close();
        setStatusText(resources.getString ("config-description-editor.save-complete"));
      }
      catch (Exception ioe)
      {
        Log.debug ("Failed", ioe);
        String message = MessageFormat.format
          (resources.getString ("config-description-editor.save-failed"),
           new Object[] { ioe.getMessage()}); 
        setStatusText(message);
      }
    }
  }

  /**
   * Loads the config description model from a xml file.
   */
  private void load ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        InputStream in = new BufferedInputStream
          (new FileInputStream(fileChooser.getSelectedFile()));
        model.load(in);
        in.close();
        model.sort();
        setStatusText(resources.getString ("config-description-editor.load-complete"));
      }
      catch (Exception ioe)
      {
        Log.debug ("Load Failed", ioe);
        String message = MessageFormat.format
          (resources.getString ("config-description-editor.load-failed"),
           new Object[] { ioe.getMessage()}); 
        setStatusText(message);
      }
    }
  }

  /**
   * Updates the currently selected entry from the values found in the 
   * detail editor.
   *
   */
  private void updateSelectedEntry ()
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
          ce.setHidden(hiddenField.isSelected());
          try
          {
            Class c = this.getClass().getClassLoader().loadClass(baseClassField.getText());
            ce.setBaseClass(c);
          }
          catch (Exception e)
          {
            // invalid
            Log.debug ("Class is invalid; defaulting to Object.class");
            ce.setBaseClass(Object.class);
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
          ece.setHidden(hiddenField.isSelected());
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
          te.setHidden(hiddenField.isSelected());
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
    setStatusText(resources.getString ("config-description-editor.update-complete"));
  }

  /**
   * Handles the attemp to quit the program. This method shuts down the VM.
   *
   */
  private void attempExit ()
  {
    System.exit (0);
  }
  
  /**
   * The main entry point to start the detail editor.
   *  
   * @param args ignored.
   */
  public static void main(String[] args)
  {
    ConfigDescriptionEditor ed = new ConfigDescriptionEditor();
    ed.pack();
    ed.setVisible(true);
  }
}
