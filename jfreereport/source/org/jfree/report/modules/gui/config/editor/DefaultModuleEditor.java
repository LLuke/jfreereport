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
 * DefaultModuleEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultModuleEditor.java,v 1.3 2003/09/12 21:06:42 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.text.html.HTMLEditorKit;

import org.jfree.report.modules.Module;
import org.jfree.report.modules.gui.config.VerticalLayout;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ModuleNodeFactory;
import org.jfree.report.util.ReportConfiguration;

/**
 * The default module editor provides a simple default implementation to
 * edit all configuration keys for a given module.
 * 
 * @author Thomas Morgner
 */
public class DefaultModuleEditor implements ModuleEditor
{
  /**
   * Handles the selection of an checkbox and enables the assigned
   * editor component.
   */
  private static class EnableAction implements ActionListener
  {
    /** The key editor that is assigned to the checkbox. */
    private KeyEditor editor;
    /** The source checkbox, to which this action is assigned. */
    private JCheckBox source;

    /**
     * Creates a new enable action for the given checkbox.
     * 
     * @param ed the key editor that is assigned to the checkbox
     * @param source the checkbox on which this action is registered-
     */
    public EnableAction(KeyEditor ed, JCheckBox source)
    {
      this.editor = ed;
      this.source = source;
    }

    /**
     * Enables the key editor if the checkbox is selected.
     * 
     * @param e not used
     */
    public void actionPerformed(ActionEvent e)
    {
      editor.setEnabled(source.isSelected());
    }
  }

  /**
   * A editor carrier implementation used to collect all active editor
   * components and their assigned checkboxes.
   */
  private static class EditorCarrier
  {
    /** The editor component. */
    private KeyEditor editor;
    /** The checkbox that enabled the editor. */
    private JCheckBox enableBox;
  
    /**
     * Creates a new carrier for the given editor and checkbox. 
     * 
     * @param editor the editor component to which the checkbox is assigned
     * @param enableBox the checkbox that enabled the editor.
     */
    public EditorCarrier(KeyEditor editor, JCheckBox enableBox)
    {
      this.editor = editor;
      this.enableBox = enableBox;
    }

    /**
     * Return the key editor.
     * @return the editor.
     */
    public KeyEditor getEditor()
    {
      return editor;
    }

    /**
     * Resets the keyeditor and the checkbox to the default value.
     *
     */
    public void reset()
    {
      enableBox.setSelected(editor.isDefined());
      editor.setEnabled(editor.isDefined());
    }
  }
  
  /** The report configuration used in this module editor. */
  private ReportConfiguration config;
  /** The list of keynames used in the editor. */
  private ConfigDescriptionEntry[] keyNames;
  /** The contentpane that holds all other components. */
  private JPanel contentpane;
  /** all active key editors as array. */
  private EditorCarrier[] activeEditors;
  /** The module which we edit. */
  private Module module;
  /** The package of the module implementation. */
  private String modulePackage;
  /** The rootpane holds the editor and the help area. */
  private JSplitPane rootpane;
  /** The rootpane holds the editor and the help area. */
  private JEditorPane helpPane;

  /**
   * Creates a new, uninitialized module editor.
   */
  public DefaultModuleEditor()
  {
    contentpane = new JPanel();
    contentpane.setLayout(new VerticalLayout());

    helpPane = new JEditorPane();
    helpPane.setEditable(false);
    helpPane.setEditorKit(new HTMLEditorKit());
    JPanel toolbar = new JPanel();
    toolbar.setLayout(new BorderLayout());
    toolbar.add (new JScrollPane(helpPane));
    toolbar.setMinimumSize(new Dimension (100, 150));

    rootpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    rootpane.setResizeWeight(1);
    rootpane.setBottomComponent(toolbar);
    rootpane.setTopComponent(new JScrollPane(contentpane));
  }

  /**
   * Creates a new, initialized instance of the default module editor. 
   * @see ModuleEditor#createInstance(Module, 
   * ReportConfiguration, ConfigDescriptionEntry[])
   * 
   * @param module the module that should be edited.
   * @param config the report configuration used to fill the values of the editors.
   * @param keyNames the list of keynames this module editor should handle.
   * @return the created new editor instance. 
   */
  public ModuleEditor createInstance
      (Module module, ReportConfiguration config, ConfigDescriptionEntry[] keyNames)
  {
    DefaultModuleEditor ed = new DefaultModuleEditor();
    ed.setConfig(config);
    ed.setKeyNames(keyNames);
    ed.setModule(module);
    ed.build();
    return ed;
  }

  /**
   * Returns the currently edited module. 
   * 
   * @return the module of this editor.
   */
  protected Module getModule()
  {
    return module;
  }

  /**
   * Defines the module for this editor.
   * 
   * @param module the module, which should be handled by this editor.
   */
  protected void setModule(Module module)
  {
    if (module == null)
    {
      throw new NullPointerException();
    }
    this.module = module;
    this.modulePackage = ModuleNodeFactory.getPackage(module.getClass());
  }

  /**
   * Checks, whether this module editor can handle the given module.
   *  
   * @param module the module to be edited.
   * @return true, if this editor may be used to edit the module, false otherwise.
   * @see ModuleEditor#canHandle(Module)
   */
  public boolean canHandle(Module module)
  {
    return true;
  }

  /**
   * Returns the report configuration used when loading values for this editor.
   * @return the report configuration.
   */
  protected ReportConfiguration getConfig()
  {
    return config;
  }

  /**
   * Defines the report configuration for this editor.
   * @param config the report configuration.
   */
  protected void setConfig(ReportConfiguration config)
  {
    this.config = config;
  }

  /**
   * Returns the key names used in this editor.
   * 
   * @return the keynames.
   */
  protected ConfigDescriptionEntry[] getKeyNames()
  {
    return keyNames;
  }

  /**
   * Defines the suggested key names for the module editor. This 
   * implementation will use these keys to build the key editors.
   * 
   * @param keyNames the key names for the editor.
   */
  protected void setKeyNames(ConfigDescriptionEntry[] keyNames)
  {
    this.keyNames = keyNames;
  }

  /**
   * Returns the editor component of the module. Calling this method is 
   * only valid on instances created with createInstance.
   *   
   * @return the editor component for the GUI.
   */
  public JComponent getComponent()
  {
    return rootpane;
  }

  /**
   * Creates a cut down display name for the given key. The display name
   * will replace the module package with '~'.
   * 
   * @param keyName the keyname which should be shortend.
   * @return the modified keyname suitable to be displayed as label.
   */
  private String createDisplayName (String keyName)
  {
    if (keyName.startsWith(modulePackage))
    {
      return "~" + keyName.substring(modulePackage.length());
    }
    return keyName;
  }

  /**
   * Initializes all component for the module editor and creates
   * and layouts all keyeditors.
   */
  protected void build()
  {
    StringWriter writer = new StringWriter();
    writer.write("<html><head><title></title></head><body>");

    JLabel mangleInfo = new JLabel();
    mangleInfo.setText
        ("All keys marked with '~.' are relative to the module package '" +
        modulePackage + "'");
    contentpane.add (mangleInfo);

    ConfigDescriptionEntry[] keyNames = getKeyNames();
    if (keyNames == null)
    {
      throw new IllegalStateException
          ("No keys defined. Are you working on the template?");
    }
    activeEditors = new EditorCarrier[keyNames.length];
    for (int i = 0; i < keyNames.length; i++)
    {
      KeyEditor editor;
      String displayName = createDisplayName(keyNames[i].getKeyName());

      if (keyNames[i] instanceof EnumConfigDescriptionEntry)
      {
        EnumConfigDescriptionEntry entry = (EnumConfigDescriptionEntry) keyNames[i];
        editor = new EnumKeyEditor(getConfig(), entry, displayName);
      }
      else if (keyNames[i] instanceof ClassConfigDescriptionEntry)
      {
        ClassConfigDescriptionEntry entry = (ClassConfigDescriptionEntry) keyNames[i];
        editor = new ClassKeyEditor(getConfig(), entry, displayName);
      }
      else
      {
        editor = new TextKeyEditor(getConfig(), keyNames[i], displayName);
      }

      JCheckBox enableCB = new JCheckBox();
      enableCB.addActionListener(new EnableAction(editor, enableCB));
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add (enableCB, BorderLayout.WEST);
      panel.add (editor.getComponent(), BorderLayout.CENTER);

      contentpane.add(panel);
      activeEditors[i] = new EditorCarrier(editor, enableCB);

      writer.write("<h3><b>");
      writer.write(keyNames[i].getKeyName());
      writer.write("</b></h3>");
      writer.write("<p>");
      writer.write(keyNames[i].getDescription());
      writer.write("</p><hr>");
    }

    int width = 0;
    for (int i = 0; i < activeEditors.length; i++)
    {
      width = Math.max (width, activeEditors[i].getEditor().getLabelWidth());
    }
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].getEditor().setLabelWidth(width);
    }
    writer.write("</body></html>");

    helpPane.setText(writer.toString());

  }

  /**
   * Resets all keys to the values from the report configuration.
   */
  public void reset()
  {
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].reset();
    }
  }

  /**
   * Stores all values for the editor's keys into the report configuration.
   */
  public void store()
  {
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].getEditor().store();
    }
  }
}
