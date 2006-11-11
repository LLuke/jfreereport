/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * Java14ConfigModuleInitializer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.misc.java14config;

import java.util.prefs.Preferences;

import org.jfree.base.modules.ModuleInitializer;
import org.jfree.base.modules.ModuleInitializeException;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.preferences.base.ConfigFactory;

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
   * @throws org.jfree.base.modules.ModuleInitializeException if an error ocurred.
   */
  public void performInit() throws ModuleInitializeException
  {
    final ConfigFactory factory = ConfigFactory.getInstance();
    final Java14ConfigStorage storage =
        new Java14ConfigStorage(Preferences.userNodeForPackage(JFreeReport.class));
    factory.defineUserStorage(storage);
    factory.defineUserStorage
        (new Java14ConfigStorage(Preferences.systemNodeForPackage(JFreeReport.class)));
  }
}
