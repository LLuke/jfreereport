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
 * ------------------------------
 * XLSExportResources_nl.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Hendri Smit;
 * Contributor(s):   -;
 *
 * $Id: XLSExportResources_nl.java,v 1.4 2003/08/25 14:29:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.xls.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Dutch language resource for the excel export GUI.
 *
 * @author Hendri Smit
 */
public class XLSExportResources_nl extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public XLSExportResources_nl()
  {
  }

  /**
   * Returns an array of localised resources.
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
        {"action.export-to-excel.name", "Exporteren naar Excel..."},
        {"action.export-to-excel.description", "Opslaan in MS-Excel formaat"},
        {"action.export-to-excel.mnemonic", new Integer(KeyEvent.VK_X)},

        {"excelexportdialog.strict-layout", "Exacte layout gebruiken."},
        {"excelexportdialog.dialogtitle", "Opslaan als Excel bestand..."},
        {"excelexportdialog.filename", "Bestandsnaam"},
        {"excelexportdialog.author", "Auteur"},
        {"excelexportdialog.title", "Titel"},
        {"excelexportdialog.selectFile", "Selecteer Bestand"},

        {"excelexportdialog.warningTitle", "Waarschuwing"},
        {"excelexportdialog.errorTitle", "Fout"},
        {"excelexportdialog.targetIsEmpty",
         "Geef een bestandsnaam voor het Excel bestand."},
        {"excelexportdialog.targetIsNoFile",
         "Doelbestand is ongeldig."},
        {"excelexportdialog.targetIsNotWritable",
         "Doelbestand kan niet worden beschreven."},
        {"excelexportdialog.targetOverwriteConfirmation",
         "Het bestand ''{0}'' bestaat reeds. Overschrijven?"},
        {"excelexportdialog.targetOverwriteTitle", "Bestand overschrijven?"},

        {"excelexportdialog.cancel", "Annuleren"},
        {"excelexportdialog.confirm", "OK"},

        {"error.processingfailed.title", "Fout tijdens rapport generatie"},
        {"error.processingfailed.message", "Rapport generatie mislukt: {0}"},
        {"error.savefailed.message", "Opslaan als PDF mislukt: {0}"},
        {"error.savefailed.title", "Fout tijdens opslaan"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{XLSExportResources.class.getName(), "nl"});
  }
}
