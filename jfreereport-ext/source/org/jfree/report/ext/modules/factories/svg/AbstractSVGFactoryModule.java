/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * AbstractSVGLoaderModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.factories.svg;

import org.jfree.resourceloader.factory.AbstractFactoryModule;

/**
 * Creation-Date: 11.11.2006, 20:57:08
 *
 * @author Thomas Morgner
 */
public abstract class AbstractSVGFactoryModule extends AbstractFactoryModule
{
  private static final String[] MIMETYPES = new String[]{
      "image/svg-xml", "image/svg+xml"
  };

  private static final String[] EXTENSIONS = new String[]{
      "svg"
  };


  public AbstractSVGFactoryModule()
  {
  }

  protected int[] getFingerPrint()
  {
    return new int[0];
  }

  protected String[] getMimeTypes()
  {
    return MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return EXTENSIONS;
  }

  public int getHeaderFingerprintSize()
  {
    return 0;
  }

}
