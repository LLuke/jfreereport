/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TrueTypeFontMetricsFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.12.2005 : Initial version
 */
package org.jfree.fonts.truetype;

import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.io.FontDataInputSource;

/**
 * Creation-Date: 16.12.2005, 22:33:07
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontMetricsFactory implements FontMetricsFactory
{


  public TrueTypeFontMetricsFactory()
  {
  }

  public FontMetrics createMetrics(final FontRecord record,
                                   final FontContext context)
  {
    final FontDataInputSource fdis = record.getFontInputSource();
    
    final TrueTypeFontMetrics metrics =
            new TrueTypeFontMetrics(null, context.getFontSize());
    return metrics;
  }
}
