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
 * $Id: TrueTypeFontMetricsFactory.java,v 1.1 2006/01/27 20:38:37 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16.12.2005 : Initial version
 */
package org.jfree.fonts.truetype;

import java.util.HashMap;
import java.io.IOException;

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
  private HashMap fontRecords;

  public TrueTypeFontMetricsFactory()
  {
  }

  public FontMetrics createMetrics(final FontRecord record,
                                   final FontContext context)
  {
    try
    {
      final FontDataInputSource fdis = record.getFontInputSource();
      final TrueTypeFont font = new TrueTypeFont(fdis);
      return new TrueTypeFontMetrics(new ScalableTrueTypeFontMetrics(font), context.getFontSize());
    }
    catch (IOException e)
    {
      return null;
    }
  }
}
