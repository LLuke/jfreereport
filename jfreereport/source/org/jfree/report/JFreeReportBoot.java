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
 * JFreeReportBoot.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: JFreeReportBoot.java,v 1.1 2005/01/25 01:13:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20-Jan-2005 : Initial version
 */
package org.jfree.report;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.base.modules.PackageManager;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.Configuration;

/**
 * An utility class to safely boot and initialize the JFreeReport library. This class
 * should be called before using the JFreeReport classes, to make sure that all subsystems
 * are initialized correctly and in the correct order.
 * <p/>
 * Application developers should make sure, that the booting is done, before JFreeReport
 * objects are used. Although the boot process will be started automaticly if needed, this
 * automated start may no longer guarantee the module initialization order.
 * <p/>
 * Additional modules can be specified by defining the system property
 * "org.jfree.report.boot.Modules". The property expects a comma-separated list of Module
 * implementations.
 *
 * @author Thomas Morgner
 */
public class JFreeReportBoot extends AbstractBoot
{
  private static JFreeReportBoot instance;
  private BootableProjectInfo projectInfo;

  private JFreeReportBoot ()
  {
    projectInfo = new JFreeReportInfo();
  }

  public static synchronized JFreeReportBoot getInstance ()
  {
    if (instance == null)
    {
      instance = new JFreeReportBoot();
    }
    return instance;
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return projectInfo;
  }

  /**
   * Loads the configuration. This will be called exactly once.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    return ReportConfiguration.getGlobalConfig();
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
    final JFreeReportInfo info = new JFreeReportInfo();
    Log.info(info.getName() + " " + info.getVersion());

    if (isStrictFP() == false)
    {
      Log.warn("The used VM seems to use a non-strict floating point arithmetics");
      Log.warn("Layouts computed with this Java Virtual Maschine may be invalid.");
      Log.warn("JFreeReport and the library 'iText' depend on the strict floating point rules");
      Log.warn("of Java1.1 as implemented by the Sun Virtual Maschines.");
      Log.warn("If you are using the BEA JRockit VM, start the Java VM with the option");
      Log.warn("'-Xstrictfp' to restore the default behaviour.");
    }

    final PackageManager mgr = getPackageManager();

    mgr.addModule(JFreeReportCoreModule.class.getName());
    mgr.addModule(DefaultLogModule.class.getName());
    mgr.load("org.jfree.report.modules.");
    mgr.load("org.jfree.report.ext.modules.");

    bootAdditionalModules();
    mgr.initializeModules();
  }

  private void bootAdditionalModules ()
  {
    try
    {
      final String bootModules =
              ReportConfiguration.getGlobalConfig().getConfigProperty
              ("org.jfree.report.boot.Modules");
      if (bootModules != null)
      {
        final CSVTokenizer csvToken = new CSVTokenizer(bootModules, ",");
        while (csvToken.hasMoreTokens())
        {
          final String token = csvToken.nextToken();
          getPackageManager().load(token);
        }
      }
    }
    catch (SecurityException se)
    {
      Log.info("Security settings forbid to check the system properties for extension modules.");
    }
    catch (Exception se)
    {
      Log.error
              ("An error occured while checking the system properties for extension modules.", se);
    }
  }


  /**
   * This method returns true on non-strict floating point systems.
   * <p/>
   * Since Java1.2 VMs may implement the floating point arithmetics in a more performant
   * way, which does not put the old strict constraints on the floating point types
   * <code>float</code> and <code>double</code>
   * <p/>
   * As iText and this library requires strict (in the sense of Java1.1) floating point
   * operations, we have to test for that feature here.
   * <p/>
   * The only known VM that seems to implement that feature is the JRockit VM. The strict
   * mode can be restored on that VM by adding the "-Xstrictfp" VM parameter.
   *
   * @return true, if the VM uses strict floating points by default, false otherwise.
   */
  private static boolean isStrictFP ()
  {
    final double d = 8e+307;
    final double result1 = 4.0 * d * 0.5;
    final double result2 = 2.0 * d;
    return (result1 != result2 && (result1 == Double.POSITIVE_INFINITY));
  }
}
