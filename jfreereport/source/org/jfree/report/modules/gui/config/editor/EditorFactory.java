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
 * EditorFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EditorFactory.java,v 1.1 2003/08/31 19:31:22 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.util.Enumeration;
import java.util.Hashtable;

import org.jfree.report.modules.Module;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.ReportConfiguration;

/**
 * The editor factory is used to create a module editor for an given
 * module. Implementors may add their own, more specialized editor components
 * to the factory.
 * 
 * @author Thomas Morgner
 */
public final class EditorFactory
{
  /** The singleton instance of the factory. */
  private static EditorFactory factory;
  /** A collection containing all defined modules and their priorities. */
  private Hashtable priorities;

  /**
   * Creates a new editor factory, which has the default module editor
   * registered at lowest priority.
   */
  private EditorFactory()
  {
    priorities = new Hashtable();
    registerModuleEditor(new DefaultModuleEditor(), -1);
  }

  /**
   * Returns the singleton instance of this factory or creates a 
   * new one if no already done.
   * 
   * @return the editor factory instance.
   */
  public static EditorFactory getInstance()
  {
    if (factory == null)
    {
      factory = new EditorFactory();
    }
    return factory;
  }

  /**
   * Registers a module editor with this factory. The editor will be 
   * registered at the given priority.
   * 
   * @param editor the editor that should be registered.
   * @param priority the priority.
   */
  public void registerModuleEditor (ModuleEditor editor, int priority)
  {
    if (editor == null)
    {
      throw new NullPointerException("Editor is null.");
    }
    priorities.put (editor, new Integer (priority));
  }

  /**
   * Returns the module editor that will be most suitable for editing 
   * the given module.
   *  
   * @param module the module that should be edited.
   * @param config the configuration which will supply the values for the edited keys.
   * @param keyNames the configuration entries which should be edited within the module. 
   * @return the module editor for the given module or null, if no editor
   * is suitable for the given module.
   */
  public ModuleEditor getModule
      (Module module, ReportConfiguration config, ConfigDescriptionEntry[] keyNames)
  {
    if (module == null)
    {
      throw new NullPointerException ("Module is null.");
    }
    if (config == null)
    {
      throw new NullPointerException ("config is null.");
    }
    if (keyNames == null)
    {
      throw new NullPointerException ("keyNames is null.");
    }
    Enumeration enum = priorities.keys();
    ModuleEditor currentEditor = null;
    int currentEditorPriority = Integer.MIN_VALUE;

    while (enum.hasMoreElements())
    {
      ModuleEditor ed = (ModuleEditor) enum.nextElement();
      if (ed.canHandle(module))
      {
        Integer prio = (Integer) priorities.get (ed);
        if (prio.intValue() > currentEditorPriority)
        {
          currentEditorPriority = prio.intValue();
          currentEditor = ed;
        }
      }
    }
    if (currentEditor != null)
    {
      return currentEditor.createInstance(module, config, keyNames);
    }
    return null;
  }

}
