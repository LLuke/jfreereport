/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libloader/
 * Project Lead:  Thomas Morgner;
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
 * ------------
 * AbstractWMFFactoryModule.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.resourceloader.modules.factory.wmf;

import org.jfree.resourceloader.factory.AbstractFactoryModule;

/**
 * Creation-Date: 05.04.2006, 17:52:57
 *
 * @author Thomas Morgner
 */
public abstract class AbstractWMFFactoryModule extends AbstractFactoryModule
{
  private static final int FINGERPRINT[] = {0xD7, 0xCD};

  private static final String[] MIMETYPES =
          {
            "application/x-msmetafile",
            "application/wmf",
            "application/x-wmf",
            "image/wmf",
            "image/x-wmf",
            "image/x-win-metafile",
            "zz-application/zz-winassoc-wmf"
          };

  private static final String[] FILEEXTENSIONS =
          {
            ".wmf"
          };

  public AbstractWMFFactoryModule()
  {
  }

  protected int[] getFingerPrint()
  {
    return FINGERPRINT;
  }

  protected String[] getMimeTypes()
  {
    return MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return FILEEXTENSIONS;
  }

  public int getHeaderFingerprintSize()
  {
    return 2;
  }
}