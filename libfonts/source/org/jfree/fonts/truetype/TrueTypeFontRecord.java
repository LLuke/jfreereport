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
 * TrueTypeFontRecord.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TrueTypeFontRecord.java,v 1.5 2006/05/06 15:16:19 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.truetype;

import java.io.IOException;

import org.jfree.fonts.FontException;
import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontRecord;

/**
 * A true-type font record. The record contains meta-information about the
 * font, which allows the system to lookup the font by one of its names and
 * other style attributes.
 * <p/>
 * A font without a 'name' table is rejected. The Name-Table is a mandatory
 * table in the OpenType standard, and only weird MacOS fonts omit that table.
 * <p/>
 * Missing 'head' or 'OS/2' tables are ignored and default values are assumed
 * instead.
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
  private boolean nonWindows; // the font does not have an OS2-Table
  private FontIdentifier identifier;

  private String name;
  private String variant;
  private String[] allNames;
  private String[] allVariants;
  private FontDataInputSource fontInputSource;

  public TrueTypeFontRecord(final TrueTypeFont trueTypeFont,
                            final FontFamily family) throws IOException,
          FontException
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
      this.nonWindows = false;
    }
    else
    {
      this.nonWindows = true;
    }

    final NameTable nameTable = (NameTable) trueTypeFont.getTable(NameTable.TABLE_ID);
    if (nameTable == null)
    {
      throw new FontException
              ("This font does not have a 'name' table. It is not valid.");
    }

    this.name = nameTable.getPrimaryName(NameTable.NAME_FULLNAME);
    this.allNames = nameTable.getAllNames(NameTable.NAME_FULLNAME);
    this.variant = nameTable.getPrimaryName(NameTable.NAME_SUBFAMILY);
    this.allVariants = nameTable.getAllNames(NameTable.NAME_SUBFAMILY);

    final FontHeaderTable headTable = (FontHeaderTable)
            trueTypeFont.getTable(FontHeaderTable.TABLE_ID);
    if (headTable != null)
    {
      this.bold = headTable.isBold();
      this.italics = headTable.isItalic();
    }
    else
    {
      final OS2Table os2Table = (OS2Table)
              trueTypeFont.getTable(OS2Table.TABLE_ID);
      if (os2Table != null)
      {
        this.bold = os2Table.isBold();
        this.italics = os2Table.isItalic();
      }
      else
      {
        // try to use the english name instead. If there is no english name,
        // then do whatever you like. Buggy non standard fonts are not funny ..
        this.bold = (variant.toLowerCase().indexOf("bold") >= 0);
        this.italics = (variant.toLowerCase().indexOf("italic") >= 0);
      }
    }

    // A font may declare that it is oblique (which is the poor man's italics
    // mode), but a font that supports italics is automaticly oblique as well.
    if (this.oblique || variant.toLowerCase().indexOf("oblique") >= 0)
    {
      this.oblique = true;
    }
    else
    {
      this.oblique = false;
    }

    this.identifier = new TrueTypeFontIdentifier
            (fontInputSource, name, variant, collectionIndex, offset);
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

  public boolean isNonWindows()
  {
    return nonWindows;
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
