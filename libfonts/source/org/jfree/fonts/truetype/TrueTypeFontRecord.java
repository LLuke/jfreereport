/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/libfonts/
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
 * TrueTypeFontRecord.java
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
package org.jfree.fonts.truetype;

import java.io.IOException;

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.util.Log;

/**
 * Creation-Date: 09.11.2005, 16:27:35
 *
 * @author Thomas Morgner
 */
public class TrueTypeFontRecord implements FontRecord
{
  private String fontFile;
  private int collectionIndex;
  private long offset;
  private boolean bold;
  private boolean italics;
  private boolean oblique;
  private FontFamily family;
  private boolean embeddable;
  private FontIdentifier identifier;

  private String name;
  private String variant;
  private String[] allNames;
  private String[] allVariants;
  private FontDataInputSource fontInputSource;

  public TrueTypeFontRecord(final TrueTypeFont trueTypeFont,
                            final FontFamily family) throws IOException
  {
    if (trueTypeFont == null)
    {
      throw new NullPointerException("The font must not be null");
    }
    if (family == null)
    {
      throw new NullPointerException("The font-family must not be null");
    }
    this.family = family;
    this.collectionIndex = trueTypeFont.getCollectionIndex();
    this.offset = trueTypeFont.getOffset();
    this.fontFile = trueTypeFont.getFilename();
    this.fontInputSource = trueTypeFont.getInputSource();

    final OS2Table table = (OS2Table) trueTypeFont.getTable(OS2Table.TABLE_ID);
    if (table != null)
    {
      this.embeddable = (table.isRestricted() == false);
    }

    final NameTable nameTable = (NameTable) trueTypeFont.getTable(NameTable.TABLE_ID);
    this.name = nameTable.getPrimaryName(NameTable.NAME_FULLNAME);
    this.allNames = nameTable.getAllNames(NameTable.NAME_FULLNAME);
    this.variant = nameTable.getPrimaryName(NameTable.NAME_SUBFAMILY);
    this.allVariants = nameTable.getAllNames(NameTable.NAME_SUBFAMILY);

    final FontHeaderTable headTable = (FontHeaderTable) trueTypeFont.getTable(FontHeaderTable.TABLE_ID);
    this.bold = headTable.isBold();
    if (variant.toLowerCase().indexOf("oblique") >= 0)
    {
      this.oblique = true;
      this.italics = false;
    }
    else
    {
      this.italics = headTable.isItalic();
      this.oblique = this.italics;
    }

    this.identifier = new TrueTypeFontIdentifier
            (fontFile, name, variant, collectionIndex, offset);
  }

  public long getOffset()
  {
    return offset;
  }

  public FontFamily getFamily()
  {
    return family;
  }

  public boolean isEmbeddable()
  {
    return embeddable;
  }

  public String getName()
  {
    return name;
  }

  public String[] getAllNames()
  {
    return (String[]) allNames.clone();
  }

  public String getVariant()
  {
    return variant;
  }

  public String[] getAllVariants()
  {
    return (String[]) allVariants.clone();
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italics;
  }

  public boolean isOblique()
  {
    return oblique;
  }

  public String getFontFile()
  {
    return fontFile;
  }

  public int getCollectionIndex()
  {
    return collectionIndex;
  }

  public FontDataInputSource getFontInputSource()
  {
    return fontInputSource;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final TrueTypeFontRecord that = (TrueTypeFontRecord) o;

    if (bold != that.bold)
    {
      return false;
    }
    if (embeddable != that.embeddable)
    {
      return false;
    }
    if (italics != that.italics)
    {
      return false;
    }
    if (oblique != that.oblique)
    {
      return false;
    }
    if (!name.equals(that.name))
    {
      return false;
    }
    return variant.equals(that.variant);

  }

  /**
   * This identifies the font resource assigned to this record.
   *
   * @return
   */
  public FontIdentifier getIdentifier()
  {
    return identifier;
  }

  public int hashCode()
  {
    int result = (bold ? 1 : 0);
    result = 29 * result + (italics ? 1 : 0);
    result = 29 * result + (oblique ? 1 : 0);
    result = 29 * result + (embeddable ? 1 : 0);
    result = 29 * result + name.hashCode();
    result = 29 * result + variant.hashCode();
    return result;
  }
}
