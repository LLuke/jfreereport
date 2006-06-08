/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
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
 * FontMetricsFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FontMetricsFactory.java,v 1.4 2006/04/17 16:33:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.registry;

/**
 * An abstract method to load font metrics.
 *
 * Implementations of this class fully manage the creation of resources
 * and should make sure that no unneccessary metrics are loaded.
 *
 * @author Thomas Morgner
 */
public interface FontMetricsFactory
{
  /**
   * Loads the font metrics for the font identified by the given identifier.
   *
   * @param identifier
   * @param context
   * @return
   */
  public FontMetrics createMetrics (final FontIdentifier identifier,
                                    final FontContext context);
}
