/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------------
 * JFreeReportResources.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportResources.java,v 1.34 2002/12/19 17:43:59 taqua Exp $
 *
 */
package com.jrefinery.report.resources;

import java.awt.event.KeyEvent;

/**
 * French Language Resources.
 *
 * @author PR
 */
public class JFreeReportResources_fr extends JFreeReportResources
{
  /**
   * Returns an array of localised resources.
   *
   * @return an array of localised resources.
   */
  public Object[][] getContents ()
  {
    return CONTENTS;
  }

  /** The resources to be localised. */
  private static final Object[][] CONTENTS =
          {
            {"action.save-as.name", "Enregistrer sous..."},
            {"action.save-as.description", "Enregistrer en PDF"},
            {"action.save-as.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.save-as.small-icon", getIcon ("com/jrefinery/report/resources/SaveAs16.gif")},
            {"action.save-as.icon", getIcon ("com/jrefinery/report/resources/SaveAs24.gif")},

            {"action.page-setup.name", "Mise en page"},
            {"action.page-setup.description", "Mise en page"},
            {"action.page-setup.mnemonic", new Integer (KeyEvent.VK_G)},
            {"action.page-setup.small-icon",
                getIcon ("com/jrefinery/report/resources/PageSetup16.gif")},
            {"action.page-setup.icon", getIcon ("com/jrefinery/report/resources/PageSetup24.gif")},

            {"action.print.name", "Impression..."},
            {"action.print.description", "Impression"},
            {"action.print.mnemonic", new Integer (KeyEvent.VK_P)},
            {"action.print.small-icon", getIcon ("com/jrefinery/report/resources/Print16.gif")},
            {"action.print.icon", getIcon ("com/jrefinery/report/resources/Print24.gif")},

            {"action.close.name", "Fermer"},
            {"action.close.description", "Fermer aperçu avant impression"},
            {"action.close.mnemonic", new Integer (KeyEvent.VK_C)},

            {"action.gotopage.name", "Aller à la page ..."},
            {"action.gotopage.description", "Voir une page directement"},
            {"action.gotopage.mnemonic", new Integer (KeyEvent.VK_G)},

            {"dialog.gotopage.message", "Entrer un numéro de page"},
            {"dialog.gotopage.title", "Aller à la page"},

            {"action.about.name", "A propos..."},
            {"action.about.description", "Information à propos de l'application"},
            {"action.about.mnemonic", new Integer (KeyEvent.VK_A)},
            {"action.about.small-icon", getIcon ("com/jrefinery/report/resources/About16.gif")},
            {"action.about.icon", getIcon ("com/jrefinery/report/resources/About24.gif")},

            {"action.firstpage.name", "Début"},
            {"action.firstpage.mnemonic", new Integer (KeyEvent.VK_HOME)},
            {"action.firstpage.description", "Aller à la première page"},
            {"action.firstpage.small-icon",
                getIcon ("com/jrefinery/report/resources/FirstPage16.gif")},
            {"action.firstpage.icon", getIcon ("com/jrefinery/report/resources/FirstPage24.gif")},

            {"action.back.name", "Précédent"},
            {"action.back.description", "Aller à la page précédente"},
            {"action.back.mnemonic", new Integer (KeyEvent.VK_PAGE_UP)},
            {"action.back.small-icon", getIcon ("com/jrefinery/report/resources/Back16.gif")},
            {"action.back.icon", getIcon ("com/jrefinery/report/resources/Back24.gif")},

            {"action.forward.name", "Retour"},
            {"action.forward.description", "Aller à la page suivante"},
            {"action.forward.mnemonic", new Integer (KeyEvent.VK_PAGE_DOWN)},
            {"action.forward.small-icon",
                getIcon ("com/jrefinery/report/resources/Forward16.gif")},
            {"action.forward.icon", getIcon ("com/jrefinery/report/resources/Forward24.gif")},

            {"action.lastpage.name", "Fin"},
            {"action.lastpage.description", "Aller à la dernière page"},
            {"action.lastpage.mnemonic", new Integer (KeyEvent.VK_END)},
            {"action.lastpage.small-icon",
                getIcon ("com/jrefinery/report/resources/LastPage16.gif")},
            {"action.lastpage.icon", getIcon ("com/jrefinery/report/resources/LastPage24.gif")},

            {"action.zoomIn.name", "Agrandir"},
            {"action.zoomIn.description", "Agrandir"},
            {"action.zoomIn.mnemonic", new Integer (KeyEvent.VK_PLUS)},
            {"action.zoomIn.small-icon", getIcon ("com/jrefinery/report/resources/ZoomIn16.gif")},
            {"action.zoomIn.icon", getIcon ("com/jrefinery/report/resources/ZoomIn24.gif")},

            {"action.zoomOut.name", "Rétrécir"},
            {"action.zoomOut.description", "Rétrécir"},
            {"action.zoomOut.mnemonic", new Integer (KeyEvent.VK_MINUS)},
            {"action.zoomOut.small-icon",
                getIcon ("com/jrefinery/report/resources/ZoomOut16.gif")},
            {"action.zoomOut.icon", getIcon ("com/jrefinery/report/resources/ZoomOut24.gif")},

            // preview frame...
            {"preview-frame.title", "Aperçu avant impression"},

            // menu labels...
            {"menu.file.name", "Fichier"},
            {"menu.file.mnemonic", new Character ('F')},

            {"menu.navigation.name", "Navigation"},
            {"menu.navigation.mnemonic", new Character ('N')},

            {"menu.zoom.name", "Zoom"},
            {"menu.zoom.mnemonic", new Character ('Z')},

            {"menu.help.name", "Aide"},
            {"menu.help.mnemonic", new Character ('H')},

            {"file.save.pdfdescription", "PDF documents"},
            {"statusline.pages", "Page {0} of {1}"},
            {"statusline.error", "Reportgeneration produced an error: {0}"},
            {"error.processingfailed.title", "Report processing failed"},
            {"error.processingfailed.message", "Error on processing this report: {0}"},
            {"error.savefailed.message", "Erreur durant l'enregistrement en PDF : {0}"},
            {"error.savefailed.title", "Erreur durant la sauvegarde"},
            {"error.printfailed.message", "Erreur à l'impression : {0}"},
            {"error.printfailed.title", "Erreur à l'impression"},

            {"tabletarget.page", "Page {0}"},

            {"pdfsavedialog.dialogtitle", "Enregistrement du rapport en PDF ..."},
            {"pdfsavedialog.filename", "Nom du fichier"},
            {"pdfsavedialog.author", "Auteur"},
            {"pdfsavedialog.title", "Titre"},
            {"pdfsavedialog.selectFile", "Selectionner un fichier"},
            {"pdfsavedialog.security", "Paramêtre de sécurité et d'encodage"},

            {"pdfsavedialog.securityNone", "Pas de sécurité"},
            {"pdfsavedialog.security40bit", "Encodage en 40 bits"},
            {"pdfsavedialog.security128bit", "Encodage en 128 bits"},
            {"pdfsavedialog.userpassword", "Mot de passe"},
            {"pdfsavedialog.userpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.userpasswordNoMatch", "Le mot de passe est incorrect."},
            {"pdfsavedialog.ownerpassword", "Mot de passe du propriétaire"},
            {"pdfsavedialog.ownerpasswordconfirm", "Confirmer"},
            {"pdfsavedialog.ownerpasswordNoMatch", "Le mot de passe du propriétaire est incorrect."},
            {"pdfsavedialog.ownerpasswordEmpty", "Le mot de passe du propriétaire est vide. Les utilisateurs "
             + "pourront modifier la sécurité. Continuer ?" },

            {"pdfsavedialog.warningTitle", "Attention"},
            {"pdfsavedialog.errorTitle", "Erreur"},
            {"pdfsavedialog.targetIsEmpty", "Veuillez spécifier un chemin pour le PDF."},
            {"pdfsavedialog.targetIsNoFile", "Le chemin spécifié est incorrect."},
            {"pdfsavedialog.targetIsNotWritable", "Le fichier sélectionner est en lecture seule."},
            {"pdfsavedialog.targetOverwriteConfirmation",
                "Le fichier ''{0}'' existe. Voulez vous l'écraser?"},
            {"pdfsavedialog.targetOverwriteTitle", "Ecraser le fichier?"},


            {"pdfsavedialog.allowCopy", "Autoriser la copie"},
            {"pdfsavedialog.allowPrinting", "Autoriser l'impression"},
            {"pdfsavedialog.allowDegradedPrinting", "Autoriser les impression de dégradés"},
            {"pdfsavedialog.allowScreenreader", "Autiriser l'usage de lecteur d'écran"},
            {"pdfsavedialog.allowAssembly", "Autoriser (Re-)assemblage"},
            {"pdfsavedialog.allowModifyContents", "Autoriser les modifications du contenu"},
            {"pdfsavedialog.allowModifyAnnotations", "Autoriser les modifications des annotations"},
            {"pdfsavedialog.allowFillIn", "Autoriser le remplssage des formules"},

            {"pdfsavedialog.option.noprinting", "Aucune imprssion"},
            {"pdfsavedialog.option.degradedprinting", "Basse qualité d'impression"},
            {"pdfsavedialog.option.fullprinting", "Impression autorisé"},

            {"pdfsavedialog.cancel", "Annuler"},
            {"pdfsavedialog.confirm", "Confirmer"},

          };

}
