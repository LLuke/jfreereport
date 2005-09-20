/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * RawDataSource.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RawDataSource.java,v 1.2 2005/03/03 22:59:59 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter;

/**
 * The raw data source allows direct access to the filtered raw data.
 * It is mainly used in the table exports, where access to Number and
 * Date objects is a requirement.
 * <p/>
 * There is no enforced requirement to implement this interface, all exports
 * will be able to work without it. But raw datasources definitly improve the
 * quality and value of the generated output, so it is generally a good idea
 * to implement it.
 *
 * @author Thomas Morgner
 */
public interface RawDataSource extends DataSource
{
  /**
   * Returns the unformated raw value. Whether that raw value is useable for
   * the export is beyond the scope of this API definition, but providing
   * access to {@link java.lang.Number} or {@link java.util.Date} objects is
   * a good idea.
   *
   * @return the raw data.
   */
  public Object getRawValue();
}
