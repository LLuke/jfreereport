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
 * ClassKeyEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ClassKeyEditor.java,v 1.4 2003/11/07 18:33:52 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

/**
 * The class key editor is used to edit report configuration keys which
 * take the name of an class as value.
 * 
 * @author Thomas Morgner
 */
public class ClassKeyEditor extends TextKeyEditor
{
  /** The base class, to which all value classes must be assignable. */
  private Class baseClass;

  /**
   * Creates a new class key editor for the given entry and configuration.
   * The given display name will be used as label text.
   * 
   * @param config the report configuration.
   * @param entry the configuration description entry that describes the key 
   * @param displayName the text for the label
   */
  public ClassKeyEditor(final ReportConfiguration config,
      final ClassConfigDescriptionEntry entry, final String displayName)
  {
    super(config, entry, displayName);
    baseClass = entry.getBaseClass();
    if (baseClass == null)
    {
      Log.warn ("Base class undefined, defaulting to java.lang.Object");
      baseClass = Object.class;
    }
    validateContent();
  }

  /**
   * Checks, whether the given value is a valid classname and is assignable
   * from the base class. 
   * @see org.jfree.report.modules.gui.config.editor.TextKeyEditor#validateContent()
   */
  public void validateContent()
  {
    if (baseClass == null)
    {
      // validate is called before the baseclass is set ... ugly!
      return;
    }
    try
    {
      final Class c = getClass().getClassLoader().loadClass(getContent());
      setValidInput (baseClass.isAssignableFrom(c));
    }
    catch (Exception e)
    {
      // ignored ..
      setValidInput(false);
    }
    // Log.debug ("Validate ClassContent:" + getContent() + " is Valid: " + isValidInput());
  }
}
