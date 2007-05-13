/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id: AWTFontMetricsFactory.java,v 1.5 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.awt;

import java.awt.Font;

import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontMetricsFactory;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 16.12.2005, 21:14:54
 *
 * @author Thomas Morgner
 */
public class AWTFontMetricsFactory implements FontMetricsFactory
{
  public AWTFontMetricsFactory()
  {
  }

  public FontMetrics createMetrics(final FontIdentifier identifier,
                                   final FontContext context)
  {
    if (FontType.AWT.equals(identifier.getFontType()) == false)
    {
      throw new IllegalArgumentException
              ("This identifier does not belong to the AWT-font system.");
    }

    // AWT-FontRecords and AWT-FontIdentifiers are implemented in the same class.
    final FontRecord record = (FontRecord) identifier;

    int style = Font.PLAIN;
    if (record.isBold())
    {
      style |= Font.BOLD;
    }
    if (record.isItalic())
    {
      style |= Font.ITALIC;
    }
    final Font font = new Font(record.getFamily().getFamilyName(), style, (int) context.getFontSize());
    return new AWTFontMetrics(font, context);
  }
}
