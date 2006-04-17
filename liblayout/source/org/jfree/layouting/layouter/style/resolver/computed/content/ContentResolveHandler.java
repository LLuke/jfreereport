/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ContentResolveHandler.java
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

package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.keys.content.ContentValues;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionFactory;
import org.jfree.layouting.layouter.style.functions.StyleFunction;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.content.CloseQuoteToken;
import org.jfree.layouting.model.content.ContentSpecification;
import org.jfree.layouting.model.content.ContentToken;
import org.jfree.layouting.model.content.ContentsToken;
import org.jfree.layouting.model.content.OpenQuoteToken;
import org.jfree.layouting.model.content.StringContentToken;
import org.jfree.layouting.model.content.ResourceContentToken;
import org.jfree.layouting.model.content.ExternalContentToken;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.loader.URLResourceKey;
import org.jfree.util.Log;

public class ContentResolveHandler implements ResolveHandler
{
  private static final ContentToken[] DEFAULT_CONTENT = new ContentToken[]{ContentsToken.CONTENTS};
  private static final DecimalCounterStyle DEFAULT_COUNTER_STYLE = new DecimalCounterStyle();

  private HashMap tokenMapping;
  private HashMap tokenAlias;

  public ContentResolveHandler ()
  {
    tokenMapping = new HashMap();
    tokenMapping.put(ContentValues.CONTENTS, ContentsToken.CONTENTS);
    tokenMapping.put(ContentValues.OPEN_QUOTE, new OpenQuoteToken(false));
    tokenMapping.put(ContentValues.NO_OPEN_QUOTE, new OpenQuoteToken(true));
    tokenMapping.put(ContentValues.CLOSE_QUOTE, new CloseQuoteToken(false));
    tokenMapping.put(ContentValues.NO_CLOSE_QUOTE, new CloseQuoteToken(true));

    tokenAlias = new HashMap();
    tokenAlias.put(ContentValues.FOOTNOTE, new CSSFunctionValue("counter", new CSSValue[]{
            new CSSConstant("footnote"), new CSSConstant("normal"),
    }));
    tokenAlias.put(ContentValues.ENDNOTE, new CSSFunctionValue("counter", new CSSValue[]{
            new CSSConstant("endnote"), new CSSConstant("normal"),
    }));
    tokenAlias
            .put(ContentValues.SECTIONNOTE, new CSSFunctionValue("counter", new CSSValue[]{
                    new CSSConstant("section-note"), new CSSConstant("normal"),
            }));
    tokenAlias.put(ContentValues.LISTITEM, new CSSFunctionValue("counter", new CSSValue[]{
            new CSSConstant("list-item"), new CSSConstant("normal"),
    }));
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[]{
            ContentStyleKeys.COUNTER_RESET,
            ContentStyleKeys.COUNTER_INCREMENT,
            ContentStyleKeys.QUOTES,
            ContentStyleKeys.STRING_SET
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (final LayoutProcess process,
                       final LayoutNode currentNode,
                       final LayoutStyle style,
                       final StyleKey key)
  {
    if (currentNode instanceof LayoutElement == false)
    {
      return;
    }


    final LayoutElement element = (LayoutElement) currentNode;

    final ContentSpecification contentSpecification =
            element.getLayoutContext().getContentSpecification();
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant)
    {
      if (ContentValues.NONE.equals(value))
      {
        contentSpecification.setAllowContentProcessing(false);
      }
      else if (ContentValues.INHIBIT.equals(value))
      {
        contentSpecification.setAllowContentProcessing(false);
      }
      return;
    }
    contentSpecification.setAllowContentProcessing(true);
    contentSpecification.setContents((ContentToken[]) DEFAULT_CONTENT.clone());

    if (value instanceof CSSAttrFunction)
    {
      final ContentToken token =
              evaluateFunction((CSSFunctionValue) value, process, element);
      if (token == null)
      {
        return;
      }
      contentSpecification.setContents(new ContentToken[]{token});
    }

    if (value instanceof CSSValueList == false)
    {
      return; // cant handle that one
    }

    final ArrayList tokens = new ArrayList();
    final CSSValueList list = (CSSValueList) value;
    final int size = list.getLength();
    for (int i = 0; i < size; i++)
    {
      final CSSValueList sequence = (CSSValueList) list.getItem(i);
      for (int j = 0; j < sequence.getLength(); j++)
      {
        final CSSValue content = sequence.getItem(j);
        final ContentToken token = createToken(process, element, content);
        if (token == null)
        {
          // ok, a failure. Skip to the next content spec ...
          tokens.clear();
          break;
        }
        tokens.add(token);
      }
    }

    final ContentToken[] contents = (ContentToken[]) tokens.toArray
            (new ContentToken[tokens.size()]);
    contentSpecification.setContents(contents);
  }


  private ContentToken createToken (LayoutProcess process,
                                    LayoutElement element,
                                    CSSValue content)
  {
    if (content instanceof CSSStringValue)
    {
      CSSStringValue sval = (CSSStringValue) content;
      if (CSSStringType.STRING.equals(sval.getType()))
      {
        return new StringContentToken(sval.getValue());
      }
      else
      {
        // this is an external URL, so try to load it.
        CSSFunctionValue function = new CSSFunctionValue
                ("url", new CSSValue[]{sval});
        return evaluateFunction(function, process, element);
      }
    }
    else if (content instanceof CSSConstant)
    {
      if (ContentValues.DOCUMENT_URL.equals(content))
      {
        Object docUrl = process.getDocumentContext().getMetaAttribute
                ("document-url");
        if (docUrl != null)
        {
          return new StringContentToken(String.valueOf(docUrl));
        }

        ResourceKey baseKey = DocumentContextUtility.getBaseResource
                (process.getDocumentContext());
        if (baseKey instanceof URLResourceKey)
        {
          URLResourceKey urlResourceKey = (URLResourceKey) baseKey;
          return new StringContentToken(urlResourceKey.toExternalForm());
        }
        return null;
      }

      ContentToken token = (ContentToken) tokenMapping.get(content);
      if (token != null)
      {
        return token;
      }

      content = (CSSValue) tokenAlias.get(content);
    }

    if (content instanceof CSSFunctionValue)
    {
      return evaluateFunction((CSSFunctionValue) content, process, element);
    }

    return null; // todo
  }

  private ContentToken evaluateFunction(final CSSFunctionValue function,
                                        final LayoutProcess process,
                                        final LayoutElement element)
  {
    StyleFunction styleFunction =
            FunctionFactory.getInstance().getFunction(function.getFunctionName());
    try
    {
      CSSValue value = styleFunction.getValue(process, element, function);
      if (value instanceof CSSResourceValue)
      {
        CSSResourceValue refValue = (CSSResourceValue) value;
        return new ResourceContentToken(refValue.getValue());
      }
      else if (value instanceof CSSStringValue)
      {
        CSSStringValue strval = (CSSStringValue) value;
        return new StringContentToken(strval.getValue());
      }
      else if (value instanceof CSSRawValue)
      {
        CSSRawValue rawValue = (CSSRawValue) value;
        return new ExternalContentToken(rawValue.getValue());
      }
      return new StringContentToken(value.getCSSText());
    }
    catch (FunctionEvaluationException e)
    {
      Log.debug ("Evaluation failed " + e);
      return null;
    }
  }
}
