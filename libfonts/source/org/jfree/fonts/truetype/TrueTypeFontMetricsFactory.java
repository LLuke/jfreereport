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
 * TrueTypeFontMetricsFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TrueTypeFontMetricsFactory.java,v 1.4 2006/04/17 16:33:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.truetype;

import java.io.IOException;
import java.util.HashMap;

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.fonts.registry.FontType;
import org.jfree.fonts.registry.EmptyFontMetrics;
import org.jfree.util.Log;

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
    this.fontRecords = new HashMap();
  }

  public FontMetrics createMetrics(final FontIdentifier record,
                                   final FontContext context)
  {
    if (FontType.OPENTYPE.equals(record.getFontType()) == false)
    {
      throw new IllegalArgumentException
              ("This identifier does not belong to the OpenType-font system.");
    }

    final TrueTypeFontIdentifier ttfId = (TrueTypeFontIdentifier) record;

    final ScalableTrueTypeFontMetrics fromCache =
            (ScalableTrueTypeFontMetrics) fontRecords.get(ttfId);
    if (fromCache != null)
    {
      return new TrueTypeFontMetrics(fromCache, context.getFontSize());
    }

    try
    {
      final FontDataInputSource fdis = ttfId.getInputSource();
      final TrueTypeFont font = new TrueTypeFont(fdis);
      final ScalableTrueTypeFontMetrics fontMetrics =
              new ScalableTrueTypeFontMetrics(font);
      this.fontRecords.put(ttfId, fontMetrics);
      return new TrueTypeFontMetrics(fontMetrics, context.getFontSize());
    }
    catch (IOException e)
    {
      Log.warn ("Unable to read the font.", e);
      // todo: We should throw exceptions instead, shouldnt we?
      return new EmptyFontMetrics();
    }
  }
}
