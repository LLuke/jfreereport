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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 31.10.2006, 12:30:43
 *
 * @author Thomas Morgner
 */
public class LibFormulaBoot extends AbstractBoot
{
  private static LibFormulaBoot instance;

  public static synchronized LibFormulaBoot getInstance()
  {
    if (instance == null)
    {
      instance = new LibFormulaBoot();
    }
    return instance;
  }

  private LibFormulaBoot()
  {
  }

  protected Configuration loadConfiguration()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/formula/libformula.properties",
             "/libformula.properties", true);

  }

  protected void performBoot()
  {
  }

  protected BootableProjectInfo getProjectInfo()
  {
    return LibFormulaInfo.getInstance();
  }
}
