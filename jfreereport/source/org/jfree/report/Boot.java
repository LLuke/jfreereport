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
 * Boot.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.9 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 01-Sep-2003 : Initial version
 *  
 */

package org.jfree.report;

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
 * @deprecated Use <code>JFreeReportBoot</code> now.
 */
public final class Boot
{
  /**
   * Hidden default constructor.
   */
  private Boot ()
  {
  }

  /**
   * Checks, whether the booting of JFreeReport is in progress.
   *
   * @return true, if the booting is in progress, false otherwise.
   */
  public synchronized static boolean isBootInProgress ()
  {
    return JFreeReportBoot.getInstance().isBootInProgress();
  }

  /**
   * Checks, whether the booting of JFreeReport is complete.
   *
   * @return true, if the booting is complete, false otherwise.
   */
  public synchronized static boolean isBootDone ()
  {
    return JFreeReportBoot.getInstance().isBootDone();
  }

  /**
   * Starts the boot process. This method does nothing, if the booting is currently in
   * progress or already done.
   */
  public static void start ()
  {
    JFreeReportBoot.getInstance().start();
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
  public static boolean isStrictFP ()
  {
    final double d = 8e+307;
    final double result1 = 4.0 * d * 0.5;
    final double result2 = 2.0 * d;
    return (result1 != result2 && (result1 == Double.POSITIVE_INFINITY));
  }
}

