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
 * KeyEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: KeyEditor.java,v 1.2 2003/09/12 21:06:42 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 12-Sep-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.editor;

import javax.swing.JComponent;

/**
 * The key editor is used to define an editor for a single report
 * configuration key. 
 *  
 * @author Thomas Morgner
 */
public interface KeyEditor
{
  /**
   * Defines, whether the key editor should be enabled.
   *  
   * @param b true, if the editor should be enabled, false otherwise.
   */
  public void setEnabled (boolean b);
  /**
   * Returns true, if the editor component is enabled, false otherwise.
   * @return true, if the editor component is enabled, false otherwise.
   */
  public boolean isEnabled ();

  /**
   * Sets the width of the label for the editor component. This is a layout
   * hint to help the module editor to build a suitable table layout.
   * 
   * @param width the preferred width for the label.
   */
  public void setLabelWidth (int width);
  
  /**
   * Returns the width of the label component. Return 0 if no label component
   * is used.
   * 
   * @return the width of the lable component.
   */
  public int getLabelWidth ();

  /**
   * Checks, whether the key is defined locally in the report configuration
   * of the editor.
   *  
   * @return true, if the local configuration provides the value for the editor,
   * false if the value is read from the default configuration. 
   */
  public boolean isDefined();

  /**
   * Returns the editor component used to display the key editor in the GUI.
   * 
   * @return the editor component.
   */
  public JComponent getComponent();

  /**
   * Resets the value to the original value from the report configuration.
   */
  public void reset ();
  
  /**
   * Stores the value into the report configuration.
   *
   */
  public void store();
}
