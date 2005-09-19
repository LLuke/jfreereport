/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * JFreeReportCoreModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: JFreeReportCoreModule.java,v 1.11 2005/09/07 14:23:49 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report;

import java.io.InputStream;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.report.resourceloader.DrawableFactory;
import org.jfree.report.resourceloader.GIFImageFactoryModule;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.resourceloader.JPEGImageFactoryModule;
import org.jfree.report.resourceloader.PNGImageFactoryModule;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;


/**
 * The CoreModule is used to represent the base classes of JFreeReport in a
 * PackageManager-compatible way. Modules may request a certain core-version to be present
 * by referencing to this module.
 * <p>
 * This module is used to initialize the image and drawable factories. If the
 * Pixie library is available, support for WMF-files is added to the factories.
 *
 * @author Thomas Morgner
 */
public class JFreeReportCoreModule extends AbstractModule
{
  /**
   * The 'no-printer-available' property key.
   */
  public static final String NO_PRINTER_AVAILABLE_KEY
          = "org.jfree.report.NoPrinterAvailable";

  /**
   * The G2 fontrenderer bug override configuration key.
   */
  public static final String FONTRENDERER_ISBUGGY_FRC_KEY
          = "org.jfree.report.layout.fontrenderer.IsBuggyFRC";

  /**
   * The text aliasing configuration key.
   */
  public static final String FONTRENDERER_USEALIASING_KEY
          = "org.jfree.report.layout.fontrenderer.UseAliasing";

  /**
   * A configuration key that defines, whether errors will abort the report
   * processing. This defaults to true.
   */
  public static final String STRICT_ERROR_HANDLING_KEY
          = "org.jfree.report.StrictErrorHandling";
  /**
   * Creates a new module definition based on the 'coremodule.properties' file of this
   * package.
   *
   * @throws ModuleInitializeException if the file could not be loaded.
   */
  public JFreeReportCoreModule ()
          throws ModuleInitializeException
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
            ("coremodule.properties", JFreeReportCoreModule.class);
    if (in == null)
    {
      throw new ModuleInitializeException
              ("File 'coremodule.properties' not found in JFreeReport package.");
    }
    loadModuleInfo(in);
  }

  /**
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    final ImageFactory factory = ImageFactory.getInstance();

    factory.registerModule(new PNGImageFactoryModule());
    factory.registerModule(new GIFImageFactoryModule());
    factory.registerModule(new JPEGImageFactoryModule());
    if (JFreeReportInfo.isPixieAvailable())
    {
      Log.info("Pixie library found. WMF file support will be available.");
      factory.registerModule("org.jfree.report.resourceloader.WmfImageFactoryModule");
      final DrawableFactory drawableFactory = DrawableFactory.getInstance();
      drawableFactory.registerModule("org.jfree.report.resourceloader.WmfImageFactoryModule");
    }
  }
}
