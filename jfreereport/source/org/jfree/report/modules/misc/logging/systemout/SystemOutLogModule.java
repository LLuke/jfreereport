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
 * SystemOutLogModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SystemOutLogModule.java,v 1.1 2003/07/11 18:35:42 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 11.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.logging.systemout;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Log;
import org.jfree.report.util.SystemOutLogTarget;

public class SystemOutLogModule extends AbstractModule
{
  public SystemOutLogModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize() throws ModuleInitializeException
  {
    if (ReportConfiguration.getGlobalConfig().isDisableLogging())
    {
      return;
    }
    if (ReportConfiguration.getGlobalConfig().getLogTarget().equals
        (SystemOutLogTarget.class.getName()))
    {
      Log.getJFreeReportLog().addTarget(new SystemOutLogTarget());
      Log.info ("System.out log target started ... previous log messages could have been ignored.");
    }
  }
}
