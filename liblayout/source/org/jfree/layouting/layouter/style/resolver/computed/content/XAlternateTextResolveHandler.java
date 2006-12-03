/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.util.ArrayList;

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
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.statics.ExternalContentToken;
import org.jfree.layouting.layouter.content.statics.ResourceContentToken;
import org.jfree.layouting.layouter.content.statics.StaticTextToken;
import org.jfree.layouting.layouter.context.ContentSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionFactory;
import org.jfree.layouting.layouter.style.functions.values.StyleValueFunction;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.loader.URLResourceKey;
import org.jfree.util.Log;

public class XAlternateTextResolveHandler implements ResolveHandler
{
  private static final ContentToken[] DEFAULT_CONTENT = new ContentToken[0];

  public XAlternateTextResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            ContentStyleKeys.COUNTER_RESET,
            ContentStyleKeys.COUNTER_INCREMENT,
            ContentStyleKeys.QUOTES,
            ContentStyleKeys.STRING_DEFINE
    };
  }

  /**
   * Resolves a String. As the string may contain the 'contents' property, it
   * is not resolvable here. The ContentNormalizer needs to handle this property
   * instead. (But this code prepares everything ..)
   *
   * @param currentNode
   * @param style
   */
  public void resolve (final LayoutProcess process,
                       final LayoutElement element,
                       final StyleKey key)
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    final ContentSpecification contentSpecification =
            layoutContext.getContentSpecification();
    final CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSConstant)
    {
      if (ContentValues.NONE.equals(value))
      {
        contentSpecification.setStrings(XAlternateTextResolveHandler.DEFAULT_CONTENT);
        return;
      }
    }

    contentSpecification.setStrings(XAlternateTextResolveHandler.DEFAULT_CONTENT);
    if (value instanceof CSSAttrFunction)
    {
      final ContentToken token =
              evaluateFunction((CSSFunctionValue) value, process, element);
      if (token == null)
      {
        return;
      }
      contentSpecification.setStrings(new ContentToken[]{token});
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
    contentSpecification.setStrings(contents);
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
        return new StaticTextToken(sval.getValue());
      }
      else
      {
        // this is an external URL, so try to load it.
        CSSFunctionValue function = new CSSFunctionValue
                ("url", new CSSValue[]{sval});
        return evaluateFunction(function, process, element);
      }
    }

    if (content instanceof CSSFunctionValue)
    {
      return evaluateFunction((CSSFunctionValue) content, process, element);
    }

    if (content instanceof CSSConstant)
    {
      if (ContentValues.DOCUMENT_URL.equals(content))
      {
        Object docUrl = process.getDocumentContext().getMetaAttribute
                ("document-url");
        if (docUrl != null)
        {
          return new StaticTextToken(String.valueOf(docUrl));
        }

        ResourceKey baseKey = DocumentContextUtility.getBaseResource
                (process.getDocumentContext());
        if (baseKey instanceof URLResourceKey)
        {
          URLResourceKey urlResourceKey = (URLResourceKey) baseKey;
          return new StaticTextToken(urlResourceKey.toExternalForm());
        }
        return null;
      }
    }
    return null;
  }

  private ContentToken evaluateFunction(final CSSFunctionValue function,
                                        final LayoutProcess process,
                                        final LayoutElement element)
  {
    StyleValueFunction styleFunction =
            FunctionFactory.getInstance().getStyleFunction(function.getFunctionName());
    try
    {
      CSSValue value = styleFunction.evaluate(process, element, function);
      if (value instanceof CSSResourceValue)
      {
        CSSResourceValue refValue = (CSSResourceValue) value;
        return new ResourceContentToken(refValue.getValue());
      }
      else if (value instanceof CSSStringValue)
      {
        CSSStringValue strval = (CSSStringValue) value;
        return new StaticTextToken(strval.getValue());
      }
      else if (value instanceof CSSRawValue)
      {
        CSSRawValue rawValue = (CSSRawValue) value;
        return new ExternalContentToken(rawValue.getValue());
      }
      return new StaticTextToken(value.getCSSText());
    }
    catch (FunctionEvaluationException e)
    {
      Log.debug ("Evaluation failed " + e);
      return null;
    }
  }
}
