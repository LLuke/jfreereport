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
        {"error.processingfailed.message", "Fel när rapporten skapades: {0}"},
        {"error.savefailed.message", "Fel inträffade under PDF sparning: {0}"},
        {"error.savefailed.title", "Fel under sparningen"},

        {"pdfsavedialog.dialogtitle", "Sparning av rapporten till en PDF-fil ..."},
        {"pdfsavedialog.filename", "Filnamn"},
        {"pdfsavedialog.author", "Författaren"},
        {"pdfsavedialog.title", "Titeln"},
        {"pdfsavedialog.selectFile", "Välja filen"},
        {"pdfsavedialog.security", "Säkerhets inställningar och kodningen"},
        {"pdfsavedialog.encoding", "Kodningen"},

        {"pdfsavedialog.securityNone", "Inga säkerhet"},
        {"pdfsavedialog.security40bit", "Kryptera med 40 bit nycklar"},
        {"pdfsavedialog.security128bit", "Kryptera med 128 bit nycklar"},
        {"pdfsavedialog.userpassword", "Använd lösenord"},
        {"pdfsavedialog.userpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.userpasswordNoMatch", "Lösenorden är inte likadan."},
        {"pdfsavedialog.ownerpassword", "Ägarens lösenord"},
        {"pdfsavedialog.ownerpasswordconfirm", "Konfirmera"},
        {"pdfsavedialog.ownerpasswordNoMatch", "Ägarens lösenord är inte likadan."},
        {"pdfsavedialog.ownerpasswordEmpty", "Ägarens lösenord är tom. Användare kan "
      + "ändra på säkerheten. Fortsätta?"},

        {"pdfsavedialog.warningTitle", "Varning"},
        {"pdfsavedialog.errorTitle", "Fel"},
        {"pdfsavedialog.targetIsEmpty", "Ange filnamnet till pdf filen."},
        {"pdfsavedialog.targetIsNoFile", "Mål filen är inte en vanlig fil."},
        {"pdfsavedialog.targetIsNotWritable", "Den valde filen är skrivskydad."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "Filen ''{0}'' Finns. Skriva över den?"},
        {"pdfsavedialog.targetOverwriteTitle", "Skriva över filen?"},


        {"pdfsavedialog.allowCopy", "Tillåta kopiering"},
        {"pdfsavedialog.allowPrinting", "Tillåta skrivning"},
        {"pdfsavedialog.allowDegradedPrinting", "Tillåta försämrade skrivning"},
        {"pdfsavedialog.allowScreenreader", "Tillåta skärmläsare"},
        {"pdfsavedialog.allowAssembly", "Tillåta återskapelse"},
        {"pdfsavedialog.allowModifyContents", "Tillåta modifiering av innehållet"},
        {"pdfsavedialog.allowModifyAnnotations", "Tillåta modifiering av anteckningar"},
        {"pdfsavedialog.allowFillIn", "Tillåta ändring av formulär data"},

        {"pdfsavedialog.option.noprinting", "Får ej skrivas ut"},
        {"pdfsavedialog.option.degradedprinting", "Låg kvalitet utskrifter"},
        {"pdfsavedialog.option.fullprinting", "Skrivning tillåten"},

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
