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
 * $Id: JFreeReportInfo.java,v 1.9 2003/10/27 20:39:52 taqua Exp $
 *
 * Changes:
 * --------
 * 10-May-2002 : Extracted from JFreeReport to declare this class public.
 * 16-May-2002 : added my name to the developer list ;-) (JS)
 * 12-Nov-2002 : Minor changes required to use JCommon 0.7.1 (DG)
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 26-Feb-2003 : Added Heiko Evermann to developer list (DG);
 *
 */

package org.jfree.report;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Library;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.ProjectInfo;

/**
 * Details about the JFreeReport project.
 *
 * @author David Gilbert
 */
public class JFreeReportInfo extends ProjectInfo implements Cloneable
{
  /**
   * Constructs an object containing information about the JFreeReport project.
   * <p>
   * Uses a resource bundle to localise some of the information.
   */
  public JFreeReportInfo()
  {
    setName("JFreeReport");
    setVersion("0.8.4-4");
    setInfo("http://www.jfree.org/jfreereport/index.html");
    setCopyright
        ("(C)opyright 2000-2003, by Thomas Morgner, Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
          new Contributor("J\u00d6rg Sch\u00d6mer", "joerg.schoemer@nikocity.de"),
          new Contributor("Heiko Evermann", "-"),
          new Contributor("Piotr Bzdyl", "-"),
          new Contributor("Patrice Rolland", "-"),
          new Contributor("Cedric Pronzato", "-")
        }
    ));

    setLibraries(Arrays.asList(
        new Library[]
        {
          new Library(JCommon.INFO),
          new Library("Pixie", "0.8.0", "LGPL",
              "http://sourceforge.net/projects/jfreereport/"),
        }
    ));
  }

  /**
   * Tries to read the licence text from jcommon. This method does not reference
   * jcommon directly, as this would increase the size of that class file.
   *
   * @see org.jfree.ui.about.ProjectInfo#getLicenceText()
   * @return the licence text for this project.
   */
  public String getLicenceText()
  {
    try
    {
      final Field f = Licences.class.getField("LGPL");
      if (f.getType().equals(String.class) == false)
      {
        return "<unable to read licence text from jcommon>";
      }
      if (f.isAccessible() && Modifier.isStatic(f.getModifiers()))
      {
        return (String) f.get(null);
      }
      return "<unexpected field state in licence text of jcommon.>";
    }
    catch (Exception e)
    {
      return "<failed to read licence text from jcommon class.>";
    }
  }

  /**
   * Print the library version and information about the library.
   * <p>
   * After there seems to be confusion about which version is currently used by
   * the user, this method will print the project information to clarify this
   * issue.
   *
   * @param args ignored
   */
  public static void main(final String[] args)
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

