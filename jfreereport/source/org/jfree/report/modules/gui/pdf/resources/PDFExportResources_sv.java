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
 * PDFExportResources.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Dilts;
 * Contributor(s):   -;
 *
 * $Id: PDFExportResources_sv.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.pdf.resources;

import java.awt.event.KeyEvent;

import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.modules.gui.base.resources.ResourceCompareTool;

/**
 * Swedish language resource for the PDF export GUI.
 * 
 * @author Thomas Dilts
 */
public class PDFExportResources_sv extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_sv()
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
        {"action.save-as.name", "Spara som PDF..."},
        {"action.save-as.description", "Spara till PDF format"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_A)},

        {"file.save.pdfdescription", "PDF dokument"},

        {"error.processingfailed.title", "Report generation misslyckades"},
        {"error.processingfailed.message", "Fel n�r rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel intr�ffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},

        {"pdfsavedialog.dialogtitle", "Sparning av rapporten till en PDF-fil ..."},
        {"pdfsavedialog.filename", "Filnamn"},
        {"pdfsavedialog.author", "F�rfattaren"},
        {"pdfsavedialog.title", "Titeln"},
        {"pdfsavedialog.selectFile", "V�lja filen"},
        {"pdfsavedialog.security", "S�kerhets inst�llningar och kodningen"},
        {"pdfsavedialog.encoding", "Kodningen"},

        {"pdfsavedialog.securityNone", "Inga s�kerhet"},
        {"pdfsavedialog.security40bit", "Kryptera med 40 bit nycklar"},
        {"pdfsavedialog.security128bit", "Kryptera med 128 bit nycklar"},
        {"pdfsavedialog.userpassword", "Anv�nd l�senord"},
        {"pdfsavedialog.userpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.userpasswordNoMatch", "L�senorden �r inte likadan."},
        {"pdfsavedialog.ownerpassword", "�garens l�senord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.ownerpasswordNoMatch", "�garens l�senord �r inte likadan."},
        {"pdfsavedialog.ownerpasswordEmpty", "�garens l�senord �r tom. Anv�ndare kan "
      + "�ndra p� s�kerheten. Forts�tta?"},

        {"pdfsavedialog.warningTitle", "Varning"},
        {"pdfsavedialog.errorTitle", "Fel"},
        {"pdfsavedialog.targetIsEmpty", "Ange filnamnet till pdf filen."},
        {"pdfsavedialog.targetIsNoFile", "M�l filen �r inte en vanlig fil."},
        {"pdfsavedialog.targetIsNotWritable", "Den valde filen �r skrivskydad."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva �ver den?"},
        {"pdfsavedialog.targetOverwriteTitle", "Skriva �ver filen?"},


        {"pdfsavedialog.allowCopy", "Till�ta kopiering"},
        {"pdfsavedialog.allowPrinting", "Till�ta skrivning"},
        {"pdfsavedialog.allowDegradedPrinting", "Till�ta f�rs�mrade skrivning"},
        {"pdfsavedialog.allowScreenreader", "Till�ta sk�rml�sare"},
        {"pdfsavedialog.allowAssembly", "Till�ta �terskapelse"},
        {"pdfsavedialog.allowModifyContents", "Till�ta modifiering av inneh�llet"},
        {"pdfsavedialog.allowModifyAnnotations", "Till�ta modifiering av anteckningar"},
        {"pdfsavedialog.allowFillIn", "Till�ta �ndring av formul�r data"},

        {"pdfsavedialog.option.noprinting", "F�r ej skrivas ut"},
        {"pdfsavedialog.option.degradedprinting", "L�g kvalitet utskrifter"},
        {"pdfsavedialog.option.fullprinting", "Skrivning till�ten"},

        {"pdfsavedialog.cancel", "Avbryt"},
        {"pdfsavedialog.confirm", "Konfirmera"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "sv"});
  }
}
