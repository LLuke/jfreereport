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
 * ConfigResources_ca.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas;
 * Contributor(s):   -;
 *
 * $Id: $
 *
 * Changes
 * -------------------------
 * 03-Sep-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

/**
 * Provides localizable resources for the Configuration editor.
 * 
 * @author Marc Casas
 */
public class ConfigResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public ConfigResources_ca()
  {
  }


  /**
   * Returns the array of strings in the resource bundle.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
      {
        {"action.add-entry.name", "Afegir entrada"},

        {"action.remove-entry.name", "Eliminar entrada"},

        {"action.import.name", "Importar"},

        {"action.load.name", "Obrir"},

        {"action.save.name", "Desar"},

      };
}
