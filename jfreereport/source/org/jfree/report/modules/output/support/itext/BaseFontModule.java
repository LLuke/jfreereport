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
 * BaseFontModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BaseFontModule.java,v 1.4 2003/08/24 15:06:42 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.support.itext;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;

/**
 * The module definition for the itext font processing module.
 *
 * @author Thomas Morgner
 */
public class BaseFontModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public BaseFontModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initialialize the font factory when this class is loaded and the system property
   * of  <code>"org.jfree.report.modules.output.pageable.itext.PDFOutputTarget.AutoInit"</code> is
   * set to <code>true</code>.
   *
   * @see org.jfree.report.modules.Module#initialize()
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
    if (isClassLoadable("com.lowagie.text.Document") == false)
    {
      throw new ModuleInitializeException("Unable to load iText classes. " +
          "Check your classpath configuration.");
    }

    if (BaseFontFactory.getFontFactory().getPDFTargetAutoInit().equals
        (BaseFontFactory.ITEXT_FONT_AUTOINIT_ONINIT))
    {
      BaseFontFactory.getFontFactory().registerDefaultFontPath();
    }
  }

}
