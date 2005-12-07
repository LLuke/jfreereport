/**
 * ========================================
 * libLayout : a free Java font reading library
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
 * TrueTypeFontRecord.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TrueTypeFontRecord.java,v 1.3 2005/12/07 22:57:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts.registry;

import java.io.IOException;

import org.jfree.fonts.truetype.TrueTypeFont;
import org.jfree.fonts.truetype.OS2Table;
import org.jfree.fonts.truetype.NameTable;
import org.jfree.fonts.truetype.FontHeaderTable;

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

  private String name;
  private String variant;
  private String[] allNames;
  private String[] allVariants;


  public TrueTypeFontRecord(final TrueTypeFont trueTypeFont,
                            final FontFamily family) throws IOException
  {
    this.family = family;
    this.collectionIndex = trueTypeFont.getCollectionIndex();
    this.offset = trueTypeFont.getOffset();
    this.fontFile = trueTypeFont.getFilename().toString();

    final OS2Table table = (OS2Table) trueTypeFont.getTable(OS2Table.TABLE_ID);
    if (table != null)
    {
      this.embeddable = table.isRestricted() == false;
    }

    final NameTable nameTable = (NameTable) trueTypeFont.getTable(NameTable.TABLE_ID);
    this.name = nameTable.getPrimaryName(NameTable.NAME_FULLNAME);
    this.allNames = nameTable.getAllNames(NameTable.NAME_FULLNAME);
    this.variant = nameTable.getPrimaryName(NameTable.NAME_SUBFAMILY);
    this.allVariants = nameTable.getAllNames(NameTable.NAME_SUBFAMILY);
    this.oblique = variant.toLowerCase().indexOf("oblique") >= 0;

    final FontHeaderTable headTable = (FontHeaderTable) trueTypeFont.getTable(FontHeaderTable.TABLE_ID);
    this.bold = headTable.isBold();
    this.italics = headTable.isItalic();
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

  public boolean isItalics()
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
}
