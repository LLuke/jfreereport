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
 * ClassKeyEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ClassKeyEditor.java,v 1.1 2003/08/31 19:31:22 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 31.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Log;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;

public class ClassKeyEditor extends TextKeyEditor
{
  private Class baseClass;

  public ClassKeyEditor(ReportConfiguration config, 
      ClassConfigDescriptionEntry entry, String displayName)
  {
    super(config, entry, "C:" + displayName);
    Log.debug ("Constructing ...");
    baseClass = entry.getBaseClass();
    if (baseClass == null)
    {
      Log.warn ("Base class undefined, defaulting to java.lang.Object");
      baseClass = Object.class;
    }
    validateContent();
  }

  public void validateContent()
  {
    if (baseClass == null)
    {
      // validate is called before the baseclass is set ... ugly!
      return;
    }
    try
    {
      Class c = getClass().getClassLoader().loadClass(getContent());
      setValidInput (baseClass.isAssignableFrom(c));
    }
    catch (Exception e)
    {
      // ignored ..
      setValidInput(false);
    }
    Log.debug ("Validate ClassContent:" + getContent() + " is Valid: " + isValidInput());
  }
}
