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
 * ContentReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ContentReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentSequence;
import org.jfree.layouting.input.style.keys.content.ContentValues;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeGlyphs;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 01.12.2005, 18:06:38
 *
 * @author Thomas Morgner
 */
public class ContentReadHandler extends OneOfConstantsReadHandler
{
  public ContentReadHandler ()
  {
    super(false);
    addValue(ContentValues.CLOSE_QUOTE);
    addValue(ContentValues.CONTENTS);
    addValue(ContentValues.DOCUMENT_URL);
    addValue(ContentValues.ENDNOTE);
    addValue(ContentValues.FOOTNOTE);
    addValue(ContentValues.LISTITEM);
    addValue(ContentValues.NO_CLOSE_QUOTE);
    addValue(ContentValues.NO_OPEN_QUOTE);
    addValue(ContentValues.OPEN_QUOTE);
    addValue(ContentValues.SECTIONNOTE);

    addValue(ListStyleTypeGlyphs.BOX);
    addValue(ListStyleTypeGlyphs.CHECK);
    addValue(ListStyleTypeGlyphs.CIRCLE);
    addValue(ListStyleTypeGlyphs.DIAMOND);
    addValue(ListStyleTypeGlyphs.DISC);
    addValue(ListStyleTypeGlyphs.HYPHEN);
    addValue(ListStyleTypeGlyphs.SQUARE);
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      final String text = value.getStringValue();
      if (ContentValues.NONE.getCSSText().equals(text))
      {
        return ContentValues.NONE;
      }
      if (ContentValues.INHIBIT.getCSSText().equals(text))
      {
        return ContentValues.INHIBIT;
      }
      if (ContentValues.NORMAL.getCSSText().equals(text))
      {
        return ContentValues.NORMAL;
      }
    }

    final ArrayList contents = new ArrayList();
    final ArrayList contentList = new ArrayList();
    while (value != null)
    {
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        CSSValue o = lookupValue(value);
        if (o == null)
        {
          return null;
        }
        contentList.add(o);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
      {
        contentList.add(new CSSConstant(value.getStringValue()));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_URI)
      {
        contentList.add(CSSValueFactory.createUriValue(value));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
      {
        contentList.add(new CSSFunctionValue(value.getStringValue(), createFunctionParameters(value.getParameters())));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
      {
        contentList.add(new CSSFunctionValue(value.getStringValue(), createFunctionParameters(value.getParameters())));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_COUNTER_FUNCTION)
      {
        contentList.add(new CSSFunctionValue(value.getStringValue(), createFunctionParameters(value.getParameters())));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_COUNTERS_FUNCTION)
      {
        contentList.add(new CSSFunctionValue(value.getStringValue(),
                createFunctionParameters(value.getParameters())));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
      {
        final CSSValue[] values =
                (CSSValue[]) contentList.toArray(new CSSValue[contentList.size()]);
        final ContentSequence sequence = new ContentSequence(values);
        contents.add(sequence);
      }
      value = value.getNextLexicalUnit();
    }

    final CSSValue[] values =
            (CSSValue[]) contentList.toArray(new CSSValue[contentList.size()]);
    final ContentSequence sequence = new ContentSequence(values);
    contents.add(sequence);
    return new CSSValueList(contents);
  }

  private CSSValue[] createFunctionParameters (LexicalUnit value)
  {
    final ArrayList contentList = new ArrayList();
    while (value != null)
    {
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        contentList.add(new CSSConstant(value.getStringValue()));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
      {
        contentList.add(new CSSConstant(value.getStringValue()));
      }
      else if (CSSValueFactory.isNumericValue(value))
      {
        contentList.add(CSSValueFactory.createNumericValue(value));
      }
      else if (CSSValueFactory.isLengthValue(value))
      {
        contentList.add(CSSValueFactory.createLengthValue(value));
      }

      value = CSSValueFactory.parseComma(value);
    }
    return (CSSValue[]) contentList.toArray(new CSSValue[contentList.size()]);
  }
}
