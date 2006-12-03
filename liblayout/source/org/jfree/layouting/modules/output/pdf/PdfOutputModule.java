/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.pdf;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.SubSystem;
import org.jfree.base.modules.ModuleInitializeException;

/**
 * Creation-Date: 02.12.2006, 17:02:40
 *
 * @author Thomas Morgner
 */
public class PdfOutputModule extends AbstractModule
{
  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_NONE = "none";

  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_40BIT = "40bit";

  /**
   * A constant for the encryption type (128 bit).
   */
  public static final String SECURITY_ENCRYPTION_128BIT = "128bit";

  
  public PdfOutputModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initializes the module. Use this method to perform all initial setup
   * operations. This method is called only once in a modules lifetime. If the
   * initializing cannot be completed, throw a ModuleInitializeException to
   * indicate the error,. The module will not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize(SubSystem subSystem) throws ModuleInitializeException
  {

  }
}
