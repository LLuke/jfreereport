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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Log;
import org.jfree.report.modules.Module;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ModuleNodeFactory;
import org.jfree.report.modules.gui.config.VerticalLayout;

public class DefaultModuleEditor implements ModuleEditor
{
  private static class EnableAction implements ActionListener
  {
    private AbstractKeyEditor editor;
    private JCheckBox source;

    public EnableAction(AbstractKeyEditor ed, JCheckBox source)
    {
      this.editor = ed;
      this.source = source;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      editor.setEnabled(source.isSelected());
    }
  }

  private static class EditorCarrier
  {
    private AbstractKeyEditor editor;
    private JCheckBox enableBox;

    public EditorCarrier(AbstractKeyEditor editor, JCheckBox enableBox)
    {
      this.editor = editor;
      this.enableBox = enableBox;
    }

    public AbstractKeyEditor getEditor()
    {
      return editor;
    }

    public void reset()
    {
      enableBox.setSelected(editor.isDefined());
      editor.setEnabled(editor.isDefined());
    }
  }

  private ReportConfiguration config;
  private ConfigDescriptionEntry[] keyNames;
  private JPanel contentpane;
  private EditorCarrier[] activeEditors;
  private Module module;
  private String modulePackage;

  public DefaultModuleEditor()
  {
    contentpane = new JPanel();
    contentpane.setLayout(new VerticalLayout());
    // this is an empty implementation just to show the light :)
  }

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

  public Module getModule()
  {
    return module;
  }

  public void setModule(Module module)
  {
    if (module == null)
    {
      throw new NullPointerException();
    }
    this.module = module;
    this.modulePackage = ModuleNodeFactory.getPackage(module.getClass());
  }

  public boolean canHandle(Module module)
  {
    return true;
  }

  public ReportConfiguration getConfig()
  {
    return config;
  }

  public void setConfig(ReportConfiguration config)
  {
    this.config = config;
  }

  public ConfigDescriptionEntry[] getKeyNames()
  {
    return keyNames;
  }

  public void setKeyNames(ConfigDescriptionEntry[] keyNames)
  {
    this.keyNames = keyNames;
  }

  public JComponent getComponent()
  {
    return contentpane;
  }

  private String createDisplayName (String keyName)
  {
    if (keyName.startsWith(modulePackage))
    {
      return "~" + keyName.substring(modulePackage.length());
    }
    return keyName;
  }

  private void build()
  {
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
      AbstractKeyEditor editor;
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
      panel.add (editor, BorderLayout.CENTER);

      contentpane.add(panel);
      activeEditors[i] = new EditorCarrier(editor, enableCB);
    }

    int width = 0;
    for (int i = 0; i < activeEditors.length; i++)
    {
      width = Math.max (width, activeEditors[i].getEditor().getLabelWidth());
    }
    Log.debug ("Defining the label width:" + width);
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].getEditor().setLabelWidth(width);
    }
  }

  public void reset()
  {
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].reset();
    }
  }

  public void store()
  {
    for (int i = 0; i < activeEditors.length; i++)
    {
      activeEditors[i].getEditor().store();
    }
  }
}
