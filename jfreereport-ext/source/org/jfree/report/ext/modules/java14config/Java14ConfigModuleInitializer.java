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
 * Java14ConfigModuleInitializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14ConfigModuleInitializer.java,v 1.4 2005/01/31 17:16:38 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 23-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14config;

import java.util.prefs.Preferences;

import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;

/**
 * An initializer for the Java 1.4 configuration provider.
 * @author Thomas Morgner
 */
public class Java14ConfigModuleInitializer implements ModuleInitializer
{
  /**
   * Default Constructor.
   */
  public Java14ConfigModuleInitializer()
  {
  }

  /**
   * Initializes the module and defines the storage implementation. 
   *
   * @throws ModuleInitializeException if an error ocurred.
   */
  public void performInit() throws ModuleInitializeException
  {
    final ConfigFactory factory = ConfigFactory.getInstance();
    factory.defineUserStorage
        (new Java14ConfigStorage(Preferences.userNodeForPackage(JFreeReport.class)));
    factory.defineUserStorage
        (new Java14ConfigStorage(Preferences.systemNodeForPackage(JFreeReport.class)));
  }
}
