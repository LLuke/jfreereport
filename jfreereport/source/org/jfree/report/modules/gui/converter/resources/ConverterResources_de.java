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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ConverterResources_de.java,v 1.6 2003/08/28 17:45:43 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.converter.resources;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * German language resource for the report converter GUI.
 *
 * @author Thomas Morgner
 */
public class ConverterResources_de extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public ConverterResources_de()
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
        {"convertdialog.action.convert.description", "Konvertiert die Quelldatei"},
        {"convertdialog.action.convert.name", "Konvertieren"},
        {"convertdialog.action.selectSource.description", "W\u00e4hlt die Quelldatei aus."},
        {"convertdialog.action.selectSource.name", "Ausw\u00e4hlen"},
        {"convertdialog.action.selectTarget.description", "W\u00e4hlt die Zieldatei aus"},
        {"convertdialog.action.selectTarget.name", "Ausw\u00e4hlen"},
        {"convertdialog.encoding", "Zeichensatz"},
        {"convertdialog.errorTitle", "Fehler"},
        {"convertdialog.sourceFile", "Quelldatei"},
        {"convertdialog.sourceIsEmpty", "Es wurde keine Quelldatei angegeben."},
        {"convertdialog.sourceIsNoFile", "Die Quelldatei ist keine normale Datei."},
        {"convertdialog.sourceIsNotReadable", "Die Quelldatei kann nicht gelesen werden."},
        {"convertdialog.targetFile", "Zieldatei"},
        {"convertdialog.targetIsEmpty", "Es wurde keine Zieldatei angegeben."},
        {"convertdialog.targetIsNoFile", "Die Zieldatei ist keine normale Datei."},
        {"convertdialog.targetIsNotWritable", "Die Zieldatei ist nicht beschreibbar."},
        {"convertdialog.targetOverwriteConfirmation",
         "Die Zieldatei existiert bereits.\nSoll die Datei \u00fcberschrieben werden?"},
        {"convertdialog.targetOverwriteTitle", "Datei \u00fcberschreiben?"},
        {"convertdialog.title", "Report-Konverter"},

        {"ResultTableModel.Severity", "Schwere"},
        {"ResultTableModel.Message", "Nachricht"},
        {"ResultTableModel.Line", "Zeile"},
        {"ResultTableModel.Column", "Spalte"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    new ConverterResources_de().generateResourceProperties("German");
    System.exit(0);
  }


}
