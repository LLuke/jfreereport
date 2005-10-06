/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Thomas Morgner, Object Refinery Limited and Contributors.
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
 * --------------------
 * JFreeReportInfo.java
 * --------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportInfo.java,v 1.32 2005/09/21 12:38:24 taqua Exp $
 *
 * Changes:
 * --------
 * 10-May-2002 : Extracted from JFreeReport to declare this class public.
 * 16-May-2002 : added my name to the developer list ;-) (JS)
 * 12-Nov-2002 : Minor changes required to use JCommon 0.7.1 (DG)
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 26-Feb-2003 : Added Heiko Evermann to developer list (DG);
 * 16-Feb-2005 : Updated the contributors list.
 */

package org.jfree.report;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.base.BootableProjectInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.util.ObjectUtilities;

/**
 * Details about the JFreeReport project.
 *
 * @author David Gilbert
 */
public class JFreeReportInfo extends ProjectInfo
{
  /**
   * Constructs an object containing information about the JFreeReport project.
   * <p/>
   * Uses a resource bundle to localise some of the information.
   */
  public JFreeReportInfo ()
  {
    setName("JFreeReport");
    setVersion("0.8.6-1");
    setInfo("http://www.jfree.org/jfreereport/");
    setCopyright
            ("(C)opyright 2000-2005, by Thomas Morgner, Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
      new Contributor("J\u00d6rg Sch\u00d6mer", "joerg.schoemer@nikocity.de"),
      new Contributor("Heiko Evermann", "-"),
      new Contributor("Piotr Bzdyl", "-"),
      new Contributor("Patrice Rolland", "-"),
      new Contributor("Cedric Pronzato", "mimil@users.sourceforge.get"),
      new Contributor("Maciej Wegorkiewicz", "-"),
      new Contributor("Michael Tennes", "michael@tennes.com"),
      new Contributor("Dmitri Colebatch", "dimc@users.sourceforge.net"),
      new Contributor("J\u00d6rg Schaible", "joehni@users.sourceforge.net"),
      new Contributor("Marc Casas", "-"),
      new Contributor("Ramon Juanes", "-"),
      new Contributor("Demeter F. Tamas", "-"),
      new Contributor("Hendri Smit", "-"),
      new Contributor("Sergey M Mozgovoi", "-"),
      new Contributor("Thomas Dilts", "-"),
      new Contributor("Illka", "ipriha@users.sourceforge.net"),
    }));

    addLibrary(JCommon.INFO);
    addDependency(JCommon.INFO);

    final BootableProjectInfo pixieLibraryInfo = tryLoadPixieInfo();
    if (pixieLibraryInfo != null)
    {
      addLibrary(pixieLibraryInfo);
      addDependency(pixieLibraryInfo);
    }
  }

  /**
   * Tries to load the Pixie boot classes. If the loading fails,
   * this method returns null.
   *
   * @return the Pixie boot info, if Pixie is available.
   */
  private static BootableProjectInfo tryLoadPixieInfo ()
  {
    try
    {
      return (BootableProjectInfo) ObjectUtilities.loadAndInstantiate
              ("org.jfree.pixie.PixieInfo", JFreeReportInfo.class);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Checks, whether the Pixie-library is available and in a working condition.
   *
   * @return true, if Pixie is available, false otherwise.
   */
  public static boolean isPixieAvailable ()
  {
    return tryLoadPixieInfo() != null;
  }

  /**
   * Tries to read the licence text from jcommon. This method does not reference jcommon
   * directly, as this would increase the size of that class file.
   *
   * @return the licence text for this project.
   *
   * @see org.jfree.ui.about.ProjectInfo#getLicenceText()
   */
  public String getLicenceText ()
  {
    return Licences.getInstance().getLGPL();
  }

  /**
   * Print the library version and information about the library.
   * <p/>
   * After there seems to be confusion about which version is currently used by the user,
   * this method will print the project information to clarify this issue.
   *
   * @param args ignored
   */
  public static void main (final String[] args)
  {
    final JFreeReportInfo info = new JFreeReportInfo();
    System.out.println(info.getName() + " " + info.getVersion());
    System.out.println("----------------------------------------------------------------");
    System.out.println(info.getCopyright());
    System.out.println(info.getInfo());
    System.out.println("----------------------------------------------------------------");
    System.out.println("This project is licenced under the terms of the "
            + info.getLicenceName() + ".");
    System.exit(0);
  }
}

