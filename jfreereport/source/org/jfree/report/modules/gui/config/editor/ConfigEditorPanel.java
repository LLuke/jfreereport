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
 * ConfigEditorPanel.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigEditorPanel.java,v 1.1 2003/08/31 19:31:22 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 30-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.MessageFormat;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.jfree.report.modules.Module;
import org.jfree.report.modules.gui.config.VerticalLayout;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.ReportConfiguration;

/**
 * The container component that is responsible for creating and managing the 
 * module editor for the currently selected module.
 * 
 * @author Thomas Morgner
 */
public class ConfigEditorPanel extends JPanel
{
  /** The currently edited module. */
  private Module module;
  /** The report configuration used to edit the module. */
  private ReportConfiguration config;
  
  /** A component holding the module description. */
  private JTextArea descriptionArea;
  /** A component holding the name of the module. */
  private JTextArea moduleNameField;
  /** A component holding the producer of the current module. */
  private JTextArea producerField;

  /** The message format used to create the module name and version. */
  private MessageFormat moduleNameFormat;
  /** The container to hold the editor. */
  private JPanel editorArea;

  /** The current module editor (may be null). */
  private ModuleEditor moduleEditor;

  /**
   * Creates a new ConfigEditorPanel.
   */
  public ConfigEditorPanel()
  {
    moduleNameFormat = new MessageFormat("{0} - Version {1}.{2}-{3}");

    moduleNameField = new JTextArea();
    moduleNameField.setName("ModuleNameField");
    moduleNameField.setMinimumSize(new Dimension(100, 10));
    moduleNameField.setEditable(false);
    moduleNameField.setLineWrap(false);
    moduleNameField.setFont(new Font ("SansSerif",
        Font.BOLD, moduleNameField.getFont().getSize() + 4));

    producerField = new JTextArea();
    producerField.setName("ProducerField");
    producerField.setMinimumSize(new Dimension(100, 10));
    producerField.setEditable(false);
    producerField.setLineWrap(false);
    producerField.setWrapStyleWord(true);
    producerField.setFont(producerField.getFont().deriveFont(Font.ITALIC));
    producerField.setBackground(UIManager.getColor("controlLtHighlight"));

    descriptionArea = new JTextArea();
    descriptionArea.setName("DescriptionArea");
    descriptionArea.setMinimumSize(new Dimension(100, 10));
    descriptionArea.setEditable(false);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setBackground(UIManager.getColor("controlShadow"));

    editorArea = new JPanel();
    editorArea.setLayout(new BorderLayout());

    JPanel contentArea = new JPanel();
    contentArea.setLayout (new VerticalLayout());//this, BoxLayout.Y_AXIS));
    contentArea.add (moduleNameField);
    contentArea.add (producerField);
    contentArea.add (descriptionArea);

    setLayout(new BorderLayout());
    add (contentArea, BorderLayout.NORTH);
    add (editorArea, BorderLayout.CENTER);
  }

  /**
   * Defines the currently edited module and initializes an module editor
   * for that module.
   * 
   * @param module the module that should be edited.
   * @param config the report configuration that supplies the values for the module.
   * @param entries a list of entries which should be edited.
   */
  public void editModule(Module module,
                        ReportConfiguration config,
                        ConfigDescriptionEntry[] entries)
  {
    this.module = module;
    this.config = config;

    Object[] params = new Object[4];
    params[0] = module.getName();
    params[1] = module.getMajorVersion();
    params[2] = module.getMinorVersion();
    params[3] = module.getPatchLevel();
    moduleNameField.setText(moduleNameFormat.format(params));
    producerField.setText(module.getProducer());
    descriptionArea.setText(module.getDescription());

    editorArea.removeAll();

    moduleEditor = EditorFactory.getInstance().getModule
        (module, config, entries);
    if (moduleEditor != null)
    {
      editorArea.add(moduleEditor.getComponent());
      moduleEditor.reset();
    }
    invalidate();
  }

  /**
   * Resets the currently edited module to the default values from the 
   * report configuration.
   */
  public void reset()
  {
    if (moduleEditor != null)
    {
      moduleEditor.reset();
    }
  }

  /**
   * Stores all values from the module editor into the report configuration.
   *
   */
  public void store()
  {
    if (moduleEditor != null)
    {
      moduleEditor.store();
    }
  }

}
