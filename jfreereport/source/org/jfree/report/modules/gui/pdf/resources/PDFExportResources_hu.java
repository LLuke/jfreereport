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
 * Original Author:  Demeter F. Tam�s;
 * Contributor(s):   -;
 *
 * $Id: PDFExportResources_hu.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
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
 * Hungarian language resource for the PDF export GUI.
 * 
 * @author Demeter F. Tam�s
 */
public class PDFExportResources_hu extends JFreeReportResources
{
  /**
   * DefaultConstructor.
   */
  public PDFExportResources_hu()
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
        {"action.save-as.name", "Ment�s mint PDF..."},
        {"action.save-as.description", "Ment�s PDF form�tumban"},
        {"action.save-as.mnemonic", new Integer(KeyEvent.VK_M)},

        {"file.save.pdfdescription", "PDF dokumentumok"},

        {"error.processingfailed.title", "A lista feldolgoz�sa nem siker�lt"},
        {"error.processingfailed.message", "A lista k�sz�t�se sor�n hiba t�rt�nt: {0}"},
        {"error.savefailed.message", "Hiba a PDF f�jl ment�sekor: {0}"},
        {"error.savefailed.title", "Hiba a ment�skor"},

        {"pdfsavedialog.dialogtitle", "Lista ment�se PDF-f�jlba ..."},
        {"pdfsavedialog.filename", "F�jln�v"},
        {"pdfsavedialog.author", "Szerz�"},
        {"pdfsavedialog.title", "C�m"},
        {"pdfsavedialog.selectFile", "V�lassz egy f�jlt"},
        {"pdfsavedialog.security", "Biztons�gi be�ll�t�sok �s titkos�t�s"},
        {"pdfsavedialog.encoding", "Karakterk�dol�s"},

        {"pdfsavedialog.securityNone", "Nincs titkos�t�s"},
        {"pdfsavedialog.security40bit", "Titkos�tsd 40 bites kulccsal"},
        {"pdfsavedialog.security128bit", "Titkos�tsd 128 bites kulccsal"},
        {"pdfsavedialog.userpassword", "Felhaszn�l�i jelsz�"},
        {"pdfsavedialog.userpasswordconfirm", "Meger�s�t�s"},
        {"pdfsavedialog.userpasswordNoMatch", "A jelsz� nem egyezik"},
        {"pdfsavedialog.ownerpassword", "Tulajdonos jelsz�"},
        {"pdfsavedialog.ownerpasswordconfirm", "Meger�s�t�s"},
        {"pdfsavedialog.ownerpasswordNoMatch", "A tulajdonosi jelsz� nem egyezik."},
        {"pdfsavedialog.ownerpasswordEmpty", "A tulajdonosi jelsz� �res. A felhaszn�l�k "
      + "megv�ltoztathatj�k a biztons�gi megszor�t�sokat. Folytathatom?"},

        {"pdfsavedialog.warningTitle", "Figyelmeztet�s"},
        {"pdfsavedialog.errorTitle", "Hiba"},
        {"pdfsavedialog.targetIsEmpty", "K�rlek nevezd el a pdf f�jlt."},
        {"pdfsavedialog.targetIsNoFile", "A kiv�lasztott c�l nem f�jl."},
        {"pdfsavedialog.targetIsNotWritable", "A kiv�lasztott f�jl nem �rhat�."},
        {"pdfsavedialog.targetOverwriteConfirmation",
         "A(z) ''{0}'' f�jl l�tezik. Fel�l�rhatom?"},
        {"pdfsavedialog.targetOverwriteTitle", "Fel�l�rhatom a f�jlt?"},


        {"pdfsavedialog.allowCopy", "M�sol�s enged�lyez�se"},
        {"pdfsavedialog.allowPrinting", "Nyomtat�s enged�lyez�se"},
        {"pdfsavedialog.allowDegradedPrinting", "Gyenge min�s�g� nyomtat�s enged�lyez�se"},
        {"pdfsavedialog.allowScreenreader", "K�polvas�k haszn�lat�nak enged�lyez�se"},
        {"pdfsavedialog.allowAssembly", "(�jra-)szerkesz�s enged�lyez�se"},
        {"pdfsavedialog.allowModifyContents", "Tartalom m�dos�t�s�nak enged�lyez�se"},
        {"pdfsavedialog.allowModifyAnnotations", "Megjegyz�sek m�dos�t�s�nak enged�lyez�se"},
        {"pdfsavedialog.allowFillIn", "Kit�lt�sek enged�lyez�se formanyomtatv�nyban"},

        {"pdfsavedialog.option.noprinting", "Nem nyomtathat�"},
        {"pdfsavedialog.option.degradedprinting", "Alacsony min�s�g� nyomtat�s"},
        {"pdfsavedialog.option.fullprinting", "Nyomtat�s enged�lyezve"},

        {"pdfsavedialog.cancel", "Megszak�t"},
        {"pdfsavedialog.confirm", "J�v�hagy"},

      };

  /**
   * Debugging method, prints all defined contents.
   *
   * @param args not used
   */
  public static void main(final String[] args)
  {
    ResourceCompareTool.main(new String[]{PDFExportResources.class.getName(), "hu"});
  }
}
