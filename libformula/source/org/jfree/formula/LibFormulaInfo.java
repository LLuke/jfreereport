/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: LibFormulaInfo.java,v 1.7 2007/03/01 17:30:37 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommon;

/**
 * Creation-Date: 31.10.2006, 12:31:15
 *
 * @author Thomas Morgner
 */
public class LibFormulaInfo extends ProjectInfo
{
  private static LibFormulaInfo instance;

  public static synchronized LibFormulaInfo getInstance()
  {
    if (instance == null)
    {
      instance = new LibFormulaInfo();
    }
    return instance;
  }

  public LibFormulaInfo()
  {
    setName("LibFormula");
    setVersion("0.1.5");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setInfo("http://reporting.pentaho.org/libformula/");
    setCopyright ("(C)opyright 2007, by Pentaho Corporation and Contributors");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
      new Contributor("Cedric Pronzato", "-"),
    }));

    setBootClass("org.jfree.formula.LibFormulaBoot");
    setAutoBoot(true);

    addLibrary(JCommon.INFO);
  }
}
