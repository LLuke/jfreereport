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
 * DefaultFontStorage.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultFontStorage.java,v 1.3 2006/04/17 16:33:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.registry;

import java.util.HashMap;

/**
 * Creation-Date: 15.12.2005, 18:07:53
 *
 * @author Thomas Morgner
 */
public class DefaultFontStorage implements FontStorage
{
  private HashMap knownMetrics;
  private FontRegistry registry;
  private FontMetricsFactory metricsFactory;

  public DefaultFontStorage(FontRegistry registry)
  {
    this.knownMetrics = new HashMap();
    this.registry = registry;
    this.metricsFactory = registry.createMetricsFactory();
  }

  public FontRegistry getFontRegistry()
  {
    return registry;
  }

  public FontMetrics getFontMetrics(final FontIdentifier record,
                                    final FontContext context)
  {
    return metricsFactory.createMetrics(record, context);
  }
}
