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
 * BarcodeTemplateCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BarcodeTemplateCollection.java,v 1.1 2003/07/16 12:12:59 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 16.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.barcode.io.factory.templates;

import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;

public class BarcodeTemplateCollection extends TemplateCollection
{
  public BarcodeTemplateCollection()
  {
    addTemplate(new Barcode128RawTemplateDescription("barcode-128-raw"));
    addTemplate(new Barcode128TemplateDescription("barcode-128"));
    addTemplate(new Barcode39TemplateDescription("barcode-39"));
    addTemplate(new BarcodeEAN13TemplateDescription("barcode-ean13-raw"));
    addTemplate(new BarcodeEAN8TemplateDescription("barcode-ean8-raw"));
    addTemplate(new BarcodeInter25TemplateDescription("barcode-inter25-raw"));
    addTemplate(new BarcodePostnetTemplateDescription("barcode-postnet-raw"));
    addTemplate(new BarcodeSUPP2TemplateDescription("barcode-supp2-raw"));
    addTemplate(new BarcodeSUPP5TemplateDescription("barcode-supp5-raw"));
    addTemplate(new BarcodeUPCATemplateDescription("barcode-upca-raw"));
    addTemplate(new BarcodeUPCETemplateDescription("barcode-upce-raw"));
  }
}
