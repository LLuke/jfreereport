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
 * --------------------------
 * ReferenceDocGenerator.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReferenceDocGenerator.java,v 1.6 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.ext.factory.datasource.DataSourceReferenceGenerator;
import com.jrefinery.report.io.ext.factory.objects.ObjectReferenceGenerator;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyReferenceGenerator;

/**
 * An application that generates reports that document properties of the JFreeReport extended
 * parser.
 *
 * @author Thomas Morgner.
 */
public class ReferenceDocGenerator
{
  /**
   * DefaultConstructor.
   */
  protected ReferenceDocGenerator()
  {
  }

  /**
   * The starting point for the application.
   *
   * @param args  command line arguments.
   */
  public static void main(final String[] args)
  {
    StyleKeyReferenceGenerator.main(args);
    ObjectReferenceGenerator.main(args);
    DataSourceReferenceGenerator.main(args);
  }
}
