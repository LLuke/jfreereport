/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------------------
 * ConverterResources.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Marc Casas;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources_ca.java,v 1.2 2003/09/10 18:20:25 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03-09-2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Catalan language resource for the report converter GUI.
 *
 * @author Marc Casas
 */
public class ConverterResources_ca extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public ConverterResources_ca()
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
        {"convertdialog.targetIsEmpty", "El destí no està especificat"},
        {"convertdialog.errorTitle", "Error"},
        {"convertdialog.targetIsNoFile", "El fitxer destí especificat no és un fitxer ordinari."},
        {"convertdialog.targetIsNotWritable", "No es pot escriure al fitxer destí."},
        {"convertdialog.targetOverwriteConfirmation",
         "El fitxer ''{0}'' existeix. El vols sobreescriure?"},
        {"convertdialog.targetOverwriteTitle", "Sobreescriure el fitxer?"},
        {"convertdialog.targetFile", "Fitxer de destí"},
        {"convertdialog.encoding", "Transformant"},
        {"convertdialog.sourceIsEmpty", "No s'ha especificat el fitxer orígen"},
        {"convertdialog.sourceIsNoFile", "El fitxer d'orígen no és un fitxer."},
        {"convertdialog.sourceIsNotReadable", "No es pot llegir el fitxer d'orígen."},
        {"convertdialog.sourceFile", "Fitxer orígen"},

        {"convertdialog.action.selectTarget.name", "Elegir"},
        {"convertdialog.action.selectTarget.description", "Elegeix el fitxer de destí."},
        {"convertdialog.action.selectSource.name", "Elegir"},
        {"convertdialog.action.selectSource.description", "Elegeix el fitxer d'origen."},
        {"convertdialog.action.convert.name", "Converteix"},
        {"convertdialog.action.convert.description", "Converteix el fitxer orígen."},

        {"convertdialog.title", "Conversor d'informes"},

        {"ResultTableModel.Severity", "Severitat"},
        {"ResultTableModel.Message", "Missatge"},
        {"ResultTableModel.Line", "Linia"},
        {"ResultTableModel.Column", "Columna"},
      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new ConverterResources_ca().generateResourceProperties("Catalan");
    System.exit(0);
  }


}
