/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReportInfo.java,v 1.9 2002/12/02 18:24:00 taqua Exp $
 *
 * Changes:
 * --------
 * 10-May-2002 : Extracted from JFreeReport to declare this class public.
 * 16-May-2002 : added my name to the developer list ;-) (JS)
 * 12-Nov-2002 : Minor changes required to use JCommon 0.7.1 (DG)
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.JCommon;
import com.jrefinery.ui.about.Contributor;
import com.jrefinery.ui.about.Library;
import com.jrefinery.ui.about.Licences;
import com.jrefinery.ui.about.ProjectInfo;

import java.util.Arrays;
import java.util.ResourceBundle;

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
  public JFreeReportInfo ()
  {
    // get a locale-specific resource bundle...
    String baseResourceClass = "com.jrefinery.report.resources.JFreeReportResources";
    ResourceBundle resources = ResourceBundle.getBundle (baseResourceClass);

    setName (resources.getString ("project.name"));
    setVersion (resources.getString ("project.version"));
    setInfo (resources.getString ("project.info"));
    setCopyright (resources.getString ("project.copyright"));
    setLicenceText (Licences.LGPL);

    setContributors(Arrays.asList (
            new Contributor[]
            {
              new Contributor ("David Gilbert", "david.gilbert@object-refinery.com"),
              new Contributor ("Thomas Morgner", "-"),
              new Contributor ("J�rg Sch�mer", "joerg.schoemer@nikocity.de")
            }
    ));

    setLibraries (Arrays.asList (
            new Library[]
            {
              new Library (JCommon.INFO),
              new Library ("JUnit", "3.7.1", "IBM Public Licence", "http://www.junit.org/"),
              // 0.95 is also suitable ...
              new Library ("iText", "0.94", "LGPL", "http://www.lowagie.com/iText/index.html"),
              new Library ("GNU JAXP", "1.0beta1", "GPL with library exception",
                      "http://www.gnu.org/software/classpathx/jaxp/"),
              new Library ("Pixie", "0.7.0", "LGPL",
                      "http://sourceforge.net/projects/jfreereport/"),
              new Library ("BeanShell", "1.2B6", "LGPL", "http://www.beanshell.org/"),
            }
    ));
  }
}

