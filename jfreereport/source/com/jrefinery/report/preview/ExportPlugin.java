/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------
 * ExportPlugin.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.jrefinery.report.JFreeReport;

/**
 * An export plug-in is a class that can work with the {@link ExportAction} class to implement
 * an export function for reports.
 * 
 * @author Thomas Morgner.
 */
public interface ExportPlugin
{
  /**
   * Exports a report.
   * 
   * @param report  the report.
   * 
   * @return A boolean.
   */
  public boolean performExport (JFreeReport report);

  /**
   * Returns the display name for the export action.
   * 
   * @return The display name.
   */
  public String getDisplayName();
  
  /**
   * Returns the short description for the export action.
   * 
   * @return The short description.
   */
  public String getShortDescription();

  /**
   * Returns the small icon for the export action.
   * 
   * @return The icon.
   */
  public Icon getSmallIcon();

  /**
   * Returns the large icon for the export action.
   * 
   * @return The icon.
   */
  public Icon getLargeIcon();
  
  /**
   * Returns the accelerator key for the export action.
   * 
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey();

  /**
   * Returns the mnemonic key code.
   * 
   * @return The code.
   */
  public Integer getMnemonicKey();
  
  /**
   * Returns true if the action is separated, and false otherwise.
   * 
   * @return A boolean.
   */
  public boolean isSeparated ();

  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   * 
   * @return A boolean.
   */
  public boolean isAddToToolbar();
}
