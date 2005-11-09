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
 * DefaultFontFamily.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts.registry;

import java.util.TreeSet;

/**
 * Creation-Date: 07.11.2005, 19:45:50
 *
 * @author Thomas Morgner
 */
public class DefaultFontFamily implements FontFamily
{
  private TreeSet allNames;
  private String familyName;

  private FontRecord[] fontRecords;

  public DefaultFontFamily(final String familyName)
  {
    if (familyName == null)
    {
      throw new NullPointerException("A FamilyName must be given");
    }

    this.familyName = familyName;
    this.allNames = new TreeSet();
    this.allNames.add(familyName);
    this.fontRecords = new FontRecord[4];
  }

  public String getFamilyName()
  {
    return familyName;
  }

  public void addName (String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }
    allNames.add(name);
  }

  public String[] getAllNames()
  {
    return (String[]) allNames.toArray(new String[allNames.size()]);
  }

  public FontRecord getFontRecord(final boolean bold, final boolean italics)
  {
    if (bold && italics)
    {
      final FontRecord record = fontRecords[3];
      if (record != null)
      {
        return record;
      }
    }
    if (italics)
    {
      final FontRecord record = fontRecords[2];
      if (record != null)
      {
        return record;
      }
    }
    if (bold)
    {
      final FontRecord record = fontRecords[1];
      if (record != null)
      {
        return record;
      }
    }
    final FontRecord record = fontRecords[0];
    if (record != null)
    {
      return record;
    }

    for (int i = 0; i < fontRecords.length; i++)
    {
      FontRecord fontRecord = fontRecords[i];
      if (fontRecord != null)
      {
        return fontRecord;
      }
    }
    // we tried everything, with no luck ..
    return null;
  }

  public void setFontRecord (final FontRecord record)
  {
    final boolean bold = record.isBold();
    final boolean italics = record.isItalics();

    int index;
    if (bold && italics)
    {
      index = 3;
    }
    else if (italics)
    {
      index = 2;
    }
    else if (bold)
    {
      index = 1;
    }
    else
    {
      index = 0;
    }

    final FontRecord oldRecord = fontRecords[index];
    if (oldRecord == null)
    {
      fontRecords[index] = record;
    }
    else if (oldRecord.isEmbeddable() == false &&
             record.isEmbeddable())
    {
      fontRecords[index] = record;
    }

  }
}
