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
 * Java14ConfigModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14ConfigModule.java,v 1.1 2003/07/23 16:08:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.07.2003 : Initial version
 *
 */

package org.jfree.report.ext.modules.java14config;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.util.ReportConfiguration;

public class Java14ConfigModule extends AbstractModule
{
  private static final String JAVA14_CONFIG_STORE_CLASS =
      "org.jfree.report.ext.modules.java14config.Java14ConfigStorage";
  private static final String JAVA14_CONFIG_STORE_INITIALIZER =
      "org.jfree.report.ext.modules.java14config.Java14ConfigModuleInitializer";

  public Java14ConfigModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize() throws ModuleInitializeException
  {
    String value = ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.ConfigStore", "<not defined>");
    if (value.equals(JAVA14_CONFIG_STORE_CLASS))
    {
      performExternalInitialize(JAVA14_CONFIG_STORE_INITIALIZER);
    }
  }
}
