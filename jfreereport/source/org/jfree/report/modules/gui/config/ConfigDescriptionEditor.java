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
 * $Id: ConfigDescriptionEditor.java,v 1.16 2005/09/07 14:25:10 taqua Exp $
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

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionModel;
import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.TextConfigDescriptionEntry;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionRadioButton;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.ResourceBundleSupport;

/**
 * The config description editor is used to edit the configuration metadata used in the
 * ConfigEditor to describe the ReportConfiguration keys.
 *
 * @author Thomas Morgner
 */
public class ConfigDescriptionEditor extends JFrame
{
  /** A configuration key to define the Font used in the editor. */
  protected static final String EDITOR_FONT_KEY = "org.jfree.report.modules.gui.config.EditorFont";
  /** A configuration key to define the Font size used in the editor. */ 
  protected static final String EDITOR_FONT_SIZE_KEY = "org.jfree.report.modules.gui.config.EditorFontSize";

  /**
   * An internal constant to activate the class detail editor.
   */
  private static final String CLASS_DETAIL_EDITOR_NAME = "Class";
  /**
   * An internal constant to activate the enumeration detail editor.
   */
  private static final String ENUM_DETAIL_EDITOR_NAME = "Enum";
  /**
   * An internal constant to activate the text detail editor.
   */
  private static final String TEXT_DETAIL_EDITOR_NAME = "Text";

  /**
   * Handles close requests in this editor.
   */
  private class CloseAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public CloseAction ()
    {
      putValue(Action.NAME, getResources().getString("action.exit.name"));
    }

    /**
     * Handles the close request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
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
    public SaveAction ()
    {
      putValue(Action.NAME, getResources().getString("action.save.name"));
      putValue(Action.SMALL_ICON, getResources().getIcon("action.save.small-icon"));
    }

    /**
     * Handles the save request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      save();
    }
  }

  /**
   * Handles import requests in this editor. Imports try to build a new description model
   * from a given report configuration.
   */
  private class ImportAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public ImportAction ()
    {
      putValue(Action.NAME, getResources().getString("action.import.name"));
      putValue(Action.SMALL_ICON, getResources().getIcon("action.import.small-icon"));

    }

    /**
     * Handles the import request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final ConfigDescriptionModel model = getModel();
      model.importFromConfig(JFreeReportBoot.getInstance().getGlobalConfig());
      model.sort();
      setStatusText(getResources().getString("config-description-editor.import-complete"));
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
    public AddEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.add-entry.name"));
      putValue(Action.SMALL_ICON, getResources().getIcon("action.add-entry.small-icon"));
    }

    /**
     * Handles the add-entry request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final TextConfigDescriptionEntry te =
              new TextConfigDescriptionEntry
                      (getResources().getString("config-description-editor.unnamed-entry"));
      final ConfigDescriptionModel model = getModel();
      model.add(te);
      getEntryList().setSelectedIndex(model.getSize() - 1);
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
    public RemoveEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.remove-entry.name"));
      putValue(Action.SMALL_ICON, getResources().getIcon("action.remove-entry.small-icon"));
    }

    /**
     * Handles the remove entry request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final int[] selectedEntries = getEntryList().getSelectedIndices();
      final ConfigDescriptionModel model = getModel();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        model.remove(model.get(selectedEntries[i]));
      }
      getEntryList().clearSelection();
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
    public LoadAction ()
    {
      putValue(Action.NAME, getResources().getString("action.load.name"));
      putValue(Action.SMALL_ICON, getResources().getIcon("action.load.small-icon"));
    }

    /**
     * Handles the laod request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
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
    public UpdateAction ()
    {
      putValue(Action.NAME, getResources().getString("action.update.name"));
    }

    /**
     * Handles the update request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
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
    public CancelAction ()
    {
      putValue(Action.NAME, getResources().getString("action.cancel.name"));
    }

    /**
     * Handles the cancel request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final ConfigDescriptionEntry ce = getSelectedEntry();
      setSelectedEntry(null);
      setSelectedEntry(ce);
    }
  }

  /**
   * Handles editor type selections within the detail editor.
   */
  private class SelectTypeAction extends AbstractActionDowngrade
  {
    /**
     * the selected type.
     */
    private final int type;

    /**
     * Creates a new select type action for the given name and type.
     *
     * @param name the name of the action.
     * @param type the type that should be selected whenever this action gets called.
     */
    public SelectTypeAction (final String name, final int type)
    {
      putValue(Action.NAME, name);
      this.type = type;
    }

    /**
     * Handles the select type request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
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
    public ConfigListSelectionListener ()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final ListSelectionEvent e)
    {
      if (getEntryList().getSelectedIndex() == -1)
      {
        setSelectedEntry(null);
      }
      else
      {
        setSelectedEntry(getModel().get(getEntryList().getSelectedIndex()));
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
    public EnumerationListSelectionHandler ()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final ListSelectionEvent e)
    {
      if (getEnumEntryList().getSelectedIndex() == -1)
      {
        getEnumEntryEditField().setText("");
      }
      else
      {
        getEnumEntryEditField().setText((String) getEnumEntryListModel().get
                (getEnumEntryList().getSelectedIndex()));
      }
    }
  }

  /**
   * A ShortCut action to redefine the entries of the enumeration detail editor to
   * represent a boolean value.
   */
  private class SetBooleanEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public SetBooleanEnumEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.boolean.name"));
    }

    /**
     * Handles the boolean redefinition request.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final DefaultListModel enumEntryListModel = getEnumEntryListModel();
      enumEntryListModel.clear();
      getEnumEntryEditField().setText("");
      enumEntryListModel.addElement("true");
      enumEntryListModel.addElement("false");
    }
  }

  /**
   * Handles the request to add a new enumeration entry to the detail editor.
   */
  private class AddEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public AddEnumEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.add-enum-entry.name"));
    }

    /**
     * Handles the request to add a new enumeration entry to the detail editor.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      getEnumEntryListModel().addElement(getEnumEntryEditField().getText());
    }
  }

  /**
   * Handles the request to remove an enumeration entry to the detail editor.
   */
  private class RemoveEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public RemoveEnumEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.remove-enum-entry.name"));
    }

    /**
     * Handles the request to remove an enumeration entry to the detail editor.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final JList enumEntryList = getEnumEntryList();
      final DefaultListModel enumEntryListModel = getEnumEntryListModel();
      final int[] selectedEntries = enumEntryList.getSelectedIndices();
      for (int i = selectedEntries.length - 1; i >= 0; i--)
      {
        enumEntryListModel.remove(selectedEntries[i]);
      }
      enumEntryList.clearSelection();
    }
  }

  /**
   * Handles the request to update an enumeration entry to the detail editor.
   */
  private class UpdateEnumEntryAction extends AbstractActionDowngrade
  {
    /**
     * Defaultconstructor.
     */
    public UpdateEnumEntryAction ()
    {
      putValue(Action.NAME, getResources().getString("action.update-enum-entry.name"));
    }

    /**
     * Handles the request to update an enumeration entry to the detail editor.
     *
     * @param e not used.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final int idx = getEnumEntryList().getSelectedIndex();
      if (idx == -1)
      {
        getEnumEntryListModel().addElement(getEnumEntryEditField().getText());
      }
      else
      {
        getEnumEntryListModel().setElementAt(getEnumEntryEditField().getText(), idx);
      }
    }
  }

  /**
   * An internal value to mark a text detail editor type.
   */
  private static final int TYPE_TEXT = 0;
  /**
   * An internal value to mark a class detail editor type.
   */
  private static final int TYPE_CLASS = 1;
  /**
   * An internal value to mark a enumeration detail editor type.
   */
  private static final int TYPE_ENUM = 2;

  /**
   * A radio button to select the text editor type for the current key.
   */
  private ActionRadioButton rbText;
  /**
   * A radio button to select the class editor type for the current key.
   */
  private ActionRadioButton rbClass;
  /**
   * A radio button to select the enumeration editor type for the current key.
   */
  private ActionRadioButton rbEnum;
  /**
   * The list model used to collect and manage all available keys.
   */
  private ConfigDescriptionModel model;
  /**
   * The name of the currently edited key.
   */
  private JTextField keyNameField;
  /**
   * The description field contains a short description of the current key.
   */
  private JTextArea descriptionField;
  /**
   * Allows to check, whether the key is a global (boot-time) key.
   */
  private JCheckBox globalField;
  /**
   * Allows to check, whether the key is hidden.
   */
  private JCheckBox hiddenField;
  /**
   * The name of the base class for the class detail editor.
   */
  private JTextField baseClassField;
  /**
   * contains a message after validating the given base class.
   */
  private JLabel baseClassValidateMessage;
  /**
   * contains the currently selected entry of the enumeration detail editor.
   */
  private JTextField enumEntryEditField;
  /**
   * contains all entries of the enumeration detail editor.
   */
  private DefaultListModel enumEntryListModel;
  /**
   * The current resource bundle used to translate the strings in this dialog.
   */
  private ResourceBundleSupport resources;
  /**
   * This cardlayout is used to display the currently selected detail editor.
   */
  private CardLayout detailManager;
  /**
   * Contains the detail editor manager.
   */
  private JPanel detailManagerPanel;
  /**
   * Contains the detail editor for the key.
   */
  private final JPanel detailEditorPane;
  /**
   * The list is used to manage all available keys.
   */
  private JList entryList;
  /**
   * This list is used to manage the available entries of the enumeration detail editor.
   */
  private JList enumEntryList;
  /**
   * the currently selected description entry.
   */
  private ConfigDescriptionEntry selectedEntry;
  /**
   * The file chooser is used to select the file for the load/save operations.
   */
  private final JFileChooser fileChooser;
  /**
   * Serves as statusline for the dialog.
   */
  private JLabel statusHolder;
  /**
   * The currently selected detail editor type.
   */
  private int type;


  /**
   * Constructs a ConfigDescriptionEditor that is initially invisible.
   */
  public ConfigDescriptionEditor ()
  {
    this.resources = new ResourceBundleSupport(ConfigEditor.RESOURCE_BUNDLE);

    setTitle(resources.getString("config-description-editor.title"));
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());

    detailEditorPane = createEditPane();
    final JSplitPane splitPane = new JSplitPane
            (JSplitPane.HORIZONTAL_SPLIT, createEntryList(), detailEditorPane);

    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);

    final JPanel cPaneStatus = new JPanel();
    cPaneStatus.setLayout(new BorderLayout());
    cPaneStatus.add(contentPane, BorderLayout.CENTER);
    cPaneStatus.add(createStatusBar(), BorderLayout.SOUTH);

    setContentPane(cPaneStatus);
    setEntryType(TYPE_TEXT);
    setSelectedEntry(null);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new ExtensionFileFilter
            (resources.getString("config-description-editor.xml-files"), ".xml"));
    fileChooser.setMultiSelectionEnabled(false);

    setStatusText(resources.getString("config-description-editor.welcome"));

    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed. The close operation can
       * be overridden at this point.
       */
      public void windowClosing (final WindowEvent e)
      {
        attempExit();
      }
    });
  }

  /**
   * Creates and returns the entry list component that will hold all config description
   * entries within a list.
   *
   * @return the created entry list.
   */
  private JPanel createEntryList ()
  {
    final Action addEntryAction = new AddEntryAction();
    final Action removeEntryAction = new RemoveEntryAction();

    model = new ConfigDescriptionModel();
    entryList = new JList(model);
    entryList.addListSelectionListener(new ConfigListSelectionListener());

    final JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(addEntryAction);
    toolbar.add(removeEntryAction);

    final JPanel panel = new JPanel();
    panel.setMinimumSize(new Dimension(200, 0));
    panel.setLayout(new BorderLayout());
    panel.add(toolbar, BorderLayout.NORTH);
    panel.add(new JScrollPane
            (entryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
    return panel;
  }

  /**
   * Returns the JList component containing all entries of the enumeration detail editor.
   *
   * @return the enumeration entry list.
   */
  protected JList getEnumEntryList ()
  {
    return enumEntryList;
  }

  /**
   * Returns the text field containing the currently edited enumeration entry.
   *
   * @return the textfield containing the current entry.
   */
  protected JTextField getEnumEntryEditField ()
  {
    return enumEntryEditField;
  }

  /**
   * Returns the List Model containing all entries of the current enumeration entry
   * editor.
   *
   * @return the entry list.
   */
  protected DefaultListModel getEnumEntryListModel ()
  {
    return enumEntryListModel;
  }

  /**
   * Returns the JList component containing all configuration entries.
   *
   * @return the entry list.
   */
  protected JList getEntryList ()
  {
    return entryList;
  }

  /**
   * Creates a panel containing all dialog control buttons, like close, load, save and
   * import.
   *
   * @return the button panel.
   */
  private JPanel createButtonPane ()
  {
    final Action closeAction = new CloseAction();
    final Action saveAction = new SaveAction();
    final Action loadAction = new LoadAction();
    final Action importAction = new ImportAction();

    final JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));

    final JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1, 4));
    buttonHolder.add(new ActionButton(importAction));
    buttonHolder.add(new ActionButton(loadAction));
    buttonHolder.add(new ActionButton(saveAction));
    buttonHolder.add(new ActionButton(closeAction));

    panel.add(buttonHolder);
    return panel;
  }

  /**
   * Creates the detail editor panel. This panel will contain all specific editors for the
   * keys.
   *
   * @return the detail editor panel.
   */
  private JPanel createEditPane ()
  {
    final Action updateAction = new UpdateAction();
    final Action cancelAction = new CancelAction();

    final JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1, 4));
    buttonHolder.add(new ActionButton(cancelAction));
    buttonHolder.add(new ActionButton(updateAction));

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    buttonPanel.add(buttonHolder);

    final JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(createDetailEditorPanel(), BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
  }

  /**
   * Creates the enumeration detail editor.
   *
   * @return the enumeration detail editor.
   */
  private JPanel createEnumerationEditor ()
  {
    enumEntryEditField = new JTextField();
    enumEntryListModel = new DefaultListModel();

    enumEntryList = new JList(enumEntryListModel);
    enumEntryList.addListSelectionListener(new EnumerationListSelectionHandler());

    final JPanel listPanel = new JPanel();
    listPanel.setLayout(new BorderLayout());
    listPanel.add(enumEntryEditField, BorderLayout.NORTH);
    listPanel.add(new JScrollPane(enumEntryList), BorderLayout.CENTER);

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(5, 1));
    buttonPanel.add(new ActionButton(new AddEnumEntryAction()));
    buttonPanel.add(new ActionButton(new RemoveEnumEntryAction()));
    buttonPanel.add(new ActionButton(new UpdateEnumEntryAction()));
    buttonPanel.add(new JPanel());
    buttonPanel.add(new ActionButton(new SetBooleanEnumEntryAction()));

    final JPanel buttonCarrier = new JPanel();
    buttonCarrier.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    buttonCarrier.add(buttonPanel);

    final JPanel editorPanel = new JPanel();
    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(listPanel, BorderLayout.CENTER);
    editorPanel.add(buttonCarrier, BorderLayout.EAST);
    return editorPanel;
  }

  /**
   * Creates the class detail editor.
   *
   * @return the class detail editor.
   */
  private JPanel createClassEditor ()
  {
    baseClassField = new JTextField();
    baseClassValidateMessage = new JLabel(" ");

    final JLabel textLabel = new JLabel
            (resources.getString("config-description-editor.baseclass"));
    final JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(textLabel, BorderLayout.WEST);
    panel.add(baseClassField, BorderLayout.CENTER);
    panel.add(baseClassValidateMessage, BorderLayout.SOUTH);

    final JPanel carrier = new JPanel();
    carrier.setLayout(new BorderLayout());
    carrier.add(panel, BorderLayout.NORTH);
    return carrier;
  }

  /**
   * Creates the text detail editor.
   *
   * @return the text detail editor.
   */
  private JPanel createTextEditor ()
  {
    final JLabel textLabel = new JLabel
            (resources.getString("config-description-editor.text-editor-message"));
    final JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add(textLabel);
    return panel;
  }

  /**
   * Creates the common entry detail editor. This editor contains all shared properties.
   *
   * @return the common entry editor.
   */
  private JPanel createDetailEditorPanel ()
  {
    final JLabel keyNameLabel = new JLabel
            (resources.getString("config-description-editor.keyname"));
    final JLabel descriptionLabel = new JLabel
            (resources.getString("config-description-editor.description"));
    final JLabel typeLabel = new JLabel(resources.getString("config-description-editor.type"));
    final JLabel globalLabel = new JLabel(resources.getString("config-description-editor.global"));
    final JLabel hiddenLabel = new JLabel(resources.getString("config-description-editor.hidden"));

    hiddenField = new JCheckBox();
    globalField = new JCheckBox();
    final String font = JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            (EDITOR_FONT_KEY, "Monospaced");
    final int fontSize = StringUtil.parseInt
            (JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            (EDITOR_FONT_SIZE_KEY), 12);
    descriptionField = new JTextArea();
    descriptionField.setFont(new Font(font, Font.PLAIN, fontSize));
    descriptionField.setLineWrap(true);
    descriptionField.setWrapStyleWord(true);
    keyNameField = new JTextField();

    final JPanel enumerationEditor = createEnumerationEditor();
    final JPanel textEditor = createTextEditor();
    final JPanel classEditor = createClassEditor();

    detailManagerPanel = new JPanel();
    detailManager = new CardLayout();
    detailManagerPanel.setLayout(detailManager);
    detailManagerPanel.add(classEditor, CLASS_DETAIL_EDITOR_NAME);
    detailManagerPanel.add(textEditor, TEXT_DETAIL_EDITOR_NAME);
    detailManagerPanel.add(enumerationEditor, ENUM_DETAIL_EDITOR_NAME);

    final JPanel commonEntryEditorPanel = new JPanel();
    commonEntryEditorPanel.setLayout(new GridBagLayout());
    commonEntryEditorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

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
   * Creates the type selection panel containing some radio buttons to define the detail
   * editor type.
   *
   * @return the type selection panel.
   */
  private JPanel createTypeSelectionPane ()
  {
    final JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 1));

    rbText = new ActionRadioButton(new SelectTypeAction
            (resources.getString("config-description-editor.type-text"), TYPE_TEXT));
    rbClass = new ActionRadioButton(new SelectTypeAction
            (resources.getString("config-description-editor.type-class"), TYPE_CLASS));
    rbEnum = new ActionRadioButton(new SelectTypeAction
            (resources.getString("config-description-editor.type-enum"), TYPE_ENUM));

    final ButtonGroup bg = new ButtonGroup();
    bg.add(rbText);
    bg.add(rbClass);
    bg.add(rbEnum);

    panel.add(rbText);
    panel.add(rbClass);
    panel.add(rbEnum);

    return panel;
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status
   * bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar ()
  {
    final JPanel statusPane = new JPanel();
    statusPane.setLayout(new BorderLayout());
    statusPane.setBorder(BorderFactory.createLineBorder(UIManager.getDefaults().getColor("controlShadow")));
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
  protected void setStatusText (final String text)
  {
    statusHolder.setText(text);
  }

  /**
   * Returns the currently visible status text of this dialog.
   *
   * @return the status text.
   */
  protected String getStatusText ()
  {
    return statusHolder.getText();
  }

  /**
   * Sets the entry type for the current config description entry. This also selects and
   * activates the correct detail editor for this type.
   *
   * @param type the type of the currently selected entry.
   */
  protected void setEntryType (final int type)
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
   *
   * @return the current entry type.
   */
  protected int getEntryType ()
  {
    return type;
  }

  /**
   * Returns the currently select entry from the entry list model.
   *
   * @return the currently selected entry.
   */
  protected ConfigDescriptionEntry getSelectedEntry ()
  {
    return selectedEntry;
  }

  /**
   * Defines the currently selected entry from the entry list model and updates the detail
   * editor to reflect the data from the entry.
   *
   * @param selectedEntry the selected entry.
   */
  protected void setSelectedEntry (final ConfigDescriptionEntry selectedEntry)
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
        final ClassConfigDescriptionEntry ce = (ClassConfigDescriptionEntry) selectedEntry;
        setEntryType(TYPE_CLASS);
        if (ce.getBaseClass() != null)
        {
          baseClassField.setText(ce.getBaseClass().getName());
        }
      }
      else if (selectedEntry instanceof EnumConfigDescriptionEntry)
      {
        final EnumConfigDescriptionEntry en = (EnumConfigDescriptionEntry) selectedEntry;
        final String[] enums = en.getOptions();
        for (int i = 0; i < enums.length; i++)
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
   *
   * @param comp  the component that should be enabled or disabled.
   * @param state the new enable state.
   */
  private void deepEnable (final Component comp, final boolean state)
  {
    comp.setEnabled(state);
    if (comp instanceof Container)
    {
      final Container cont = (Container) comp;
      final Component[] childs = cont.getComponents();
      for (int i = 0; i < childs.length; i++)
      {
        deepEnable(childs[i], state);
      }
    }
  }

  /**
   * Saves the config description model in a xml file.
   */
  protected void save ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        final OutputStream out = new BufferedOutputStream
                (new FileOutputStream(fileChooser.getSelectedFile()));
        model.save(out, "ISO-8859-1");
        out.close();
        setStatusText(resources.getString("config-description-editor.save-complete"));
      }
      catch (Exception ioe)
      {
        Log.debug("Failed", ioe);
        final String message = MessageFormat.format
                (resources.getString("config-description-editor.save-failed"),
                        new Object[]{ioe.getMessage()});
        setStatusText(message);
      }
    }
  }

  /**
   * Loads the config description model from a xml file.
   */
  protected void load ()
  {
    fileChooser.setVisible(true);

    final int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        final InputStream in = new BufferedInputStream
                (new FileInputStream(fileChooser.getSelectedFile()));
        model.load(in);
        in.close();
        model.sort();
        setStatusText(resources.getString("config-description-editor.load-complete"));
      }
      catch (Exception ioe)
      {
        Log.debug("Load Failed", ioe);
        final String message = MessageFormat.format
                (resources.getString("config-description-editor.load-failed"),
                        new Object[]{ioe.getMessage()});
        setStatusText(message);
      }
    }
  }

  /**
   * Updates the currently selected entry from the values found in the detail editor.
   */
  protected void updateSelectedEntry ()
  {
    final ConfigDescriptionEntry entry;
    switch (getEntryType())
    {
      case TYPE_CLASS:
        {
          final ClassConfigDescriptionEntry ce =
                  new ClassConfigDescriptionEntry(keyNameField.getText());
          ce.setDescription(descriptionField.getText());
          ce.setGlobal(globalField.isSelected());
          ce.setHidden(hiddenField.isSelected());
          try
          {
            final Class c = ObjectUtilities.getClassLoader(getClass()).loadClass(baseClassField.getText());
            ce.setBaseClass(c);
          }
          catch (Exception e)
          {
            // invalid
            Log.debug("Class is invalid; defaulting to Object.class");
            ce.setBaseClass(Object.class);
          }
          entry = ce;
          break;
        }
      case TYPE_ENUM:
        {
          final EnumConfigDescriptionEntry ece =
                  new EnumConfigDescriptionEntry(keyNameField.getText());
          ece.setDescription(descriptionField.getText());
          ece.setGlobal(globalField.isSelected());
          ece.setHidden(hiddenField.isSelected());
          final String[] enumEntries = new String[enumEntryListModel.getSize()];
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
          final TextConfigDescriptionEntry te =
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
    setStatusText(resources.getString("config-description-editor.update-complete"));
  }

  /**
   * Returns the config description model containing all metainformation about the
   * configuration.
   *
   * @return the config description model.
   */
  protected ConfigDescriptionModel getModel ()
  {
    return model;
  }

  /**
   * Handles the attemp to quit the program. This method shuts down the VM.
   */
  protected void attempExit ()
  {
    System.exit(0);
  }

  /**
   * Returns the resource bundle of this editor for translating strings.
   *
   * @return the resource bundle.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
  }

  /**
   * The main entry point to start the detail editor.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    final ConfigDescriptionEditor ed = new ConfigDescriptionEditor();
    ed.pack();
    ed.setVisible(true);
  }
}
