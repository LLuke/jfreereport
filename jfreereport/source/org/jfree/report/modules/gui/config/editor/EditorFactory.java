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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import java.util.Enumeration;
import java.util.Hashtable;

import org.jfree.report.modules.Module;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.ReportConfiguration;

public final class EditorFactory
{
  private static EditorFactory factory;

  private Hashtable priorities;

  private EditorFactory()
  {
    priorities = new Hashtable();
    registerModuleEditor(new DefaultModuleEditor(), -1);
  }

  public static EditorFactory getInstance()
  {
    if (factory == null)
    {
      factory = new EditorFactory();
    }
    return factory;
  }

  public void registerModuleEditor (ModuleEditor editor, int priority)
  {
    priorities.put (editor, new Integer (priority));
  }

  public ModuleEditor getModule
      (Module module, ReportConfiguration config, ConfigDescriptionEntry[] keyNames)
  {
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
