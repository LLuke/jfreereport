package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentSequence;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.keys.content.ContentValues;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.CounterStyleFactory;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.ElementContext;
import org.jfree.layouting.model.content.CloseQuoteToken;
import org.jfree.layouting.model.content.ContentSpecification;
import org.jfree.layouting.model.content.ContentToken;
import org.jfree.layouting.model.content.ContentsToken;
import org.jfree.layouting.model.content.DrawableContent;
import org.jfree.layouting.model.content.ExternalContent;
import org.jfree.layouting.model.content.OpenQuoteToken;
import org.jfree.layouting.model.content.StringContent;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.loader.ContentCache;
import org.jfree.ui.Drawable;

public class ContentResolveHandler implements ResolveHandler
{
  private HashMap tokenMapping;
  private HashMap tokenAlias;
  private static final DecimalCounterStyle DEFAULT_COUNTER_STYLE = new DecimalCounterStyle();

  public ContentResolveHandler ()
  {
    tokenMapping = new HashMap();
    tokenMapping.put(ContentValues.CONTENTS, new ContentsToken());
    tokenMapping.put(ContentValues.OPEN_QUOTE, new OpenQuoteToken(false));
    tokenMapping.put(ContentValues.NO_OPEN_QUOTE, new OpenQuoteToken(true));
    tokenMapping.put(ContentValues.CLOSE_QUOTE, new CloseQuoteToken(false));
    tokenMapping.put(ContentValues.NO_CLOSE_QUOTE, new CloseQuoteToken(true));

    // todo: Make this an ordinary counter stuff ...
//    tokenMapping.put(ListStyleTypeGlyphs.BOX,
//            new StringContent(ListStyleTypeGlyphs.BOX.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.CHECK,
//            new StringContent(ListStyleTypeGlyphs.CHECK.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.CIRCLE,
//            new StringContent(ListStyleTypeGlyphs.CIRCLE.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.DIAMOND,
//            new StringContent(ListStyleTypeGlyphs.DIAMOND.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.DISC,
//            new StringContent(ListStyleTypeGlyphs.DISC.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.HYPHEN,
//            new StringContent(ListStyleTypeGlyphs.HYPHEN.getDefaultText()));
//    tokenMapping.put(ListStyleTypeGlyphs.SQUARE,
//            new StringContent(ListStyleTypeGlyphs.SQUARE.getDefaultText()));

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
      return; // we ignore non-elements, as they cannot have
      // the string-set property.
    }

    final LayoutElement element = (LayoutElement) currentNode;
    final ContentSpecification contentSpecification =
            element.getLayoutContext().getContentSpecification();
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant)
    {
      if (ContentValues.NONE.equals(value))
      {
        contentSpecification.setContentProcessing(ContentSpecification.NONE);
      }
      else if (ContentValues.INHIBIT.equals(value))
      {
        contentSpecification.setContentProcessing(ContentSpecification.INHIBIT);
      }
      return;
    }

    if (value instanceof CSSValueList == false)
    {
      return; // cant handle that one
    }

    final CSSValueList list = (CSSValueList) value;
    final int size = list.getLength();
    for (int i = 0; i < size; i++)
    {
      final ContentSequence sequence = (ContentSequence) list.getItem(i);
      final CSSValue[] contents = sequence.getContents();
      for (int j = 0; j < contents.length; j++)
      {
        final CSSValue content = contents[j];
        final ContentToken token = createToken(process, element, content);

      }
    }
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
        return new StringContent(sval.getValue());
      }
      else
      {
        // this is an external URL, so try to load it.
        try
        {
          final URL url = new URL(sval.getValue());
          final ContentCache cc = process.getDocumentContext().getResourceLoader();
          final Object externalContent = cc.getContent(url);
          if (externalContent instanceof Drawable)
          {
            return new DrawableContent((Drawable) externalContent);
          }
          else if (externalContent != null)
          {
            return new ExternalContent(externalContent);
          }
        }
        catch (IOException e)
        {
          // ignore, we cant handle that one ...
        }
        return null;
      }
    }
    else if (content instanceof CSSConstant)
    {
      if (ContentValues.DOCUMENT_URL.equals(content))
      {
        return new StringContent
                (String.valueOf(process.getDocumentContext().getBaseURL()));
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
      final CSSFunctionValue functionValue = (CSSFunctionValue) content;
      final String fnName = functionValue.getFunctionName();
      if ("counter".equals(fnName))
      {
        return handleCounterFunction(functionValue, element, process);
      }
      else if ("counters".equals(fnName))
      {
        return handleCountersFunction(functionValue, element, process);
      }
      else if ("attr".equals(fnName))
      {
        return handleAttrFunction(functionValue, element);
      }
      else if ("string".equals(fnName))
      {
        return handleStringFunction(functionValue, process);
      }
      else if ("date".equals(fnName))
      {
        return handleDateFunction(functionValue, process);
      }
      else if ("pending".equals(fnName))
      {
        // todo Not today ...
      }
    }

    return null; // todo
  }

  private ContentToken handleDateFunction (CSSFunctionValue value, LayoutProcess process)
  {
    final Date date = process.getDocumentContext().getDate();
    final CSSValue[] parameters = value.getParameters();
    final LocalizationContext localizationContext =
            process.getDocumentContext().getLocalizationContext();
    DateFormat format = getDateFormat(parameters, localizationContext);
    return new StringContent(format.format(date));
  }

  private DateFormat getDateFormat (CSSValue[] parameters,
                              LocalizationContext localizationContext)
  {
    if (parameters.length < 1)
    {
      return localizationContext.getDateFormat(Locale.getDefault());
    }

    final CSSValue formatValue = parameters[0];
    if (formatValue instanceof CSSStringValue == false)
    {
      return localizationContext.getDateFormat(Locale.getDefault());
    }

    CSSStringValue sval = (CSSStringValue) formatValue;
    DateFormat format = localizationContext.getDateFormat
            (sval.getValue(), Locale.getDefault());
    if (format != null)
    {
      return format;
    }
    return localizationContext.getDateFormat(Locale.getDefault());
  }

  private ContentToken handleStringFunction (CSSFunctionValue value,
                                             LayoutProcess process)
  {
    final CSSValue[] parameters = value.getParameters();
    if (parameters.length < 1)
    {
      return null;
    }
    final CSSValue counterName = parameters[0];
    if (counterName instanceof CSSConstant == false)
    {
      return null;
    }
    final CSSConstant sval = (CSSConstant) counterName;
    final Object attribute = process.getDocumentContext().getString(sval.getCSSText());
    // todo: The string to be printed should be taken from the page context ...
    return new StringContent(String.valueOf(attribute));
  }

  private ContentToken handleAttrFunction (CSSFunctionValue functionValue,
                                           LayoutElement element)
  {
    final CSSValue[] parameters = functionValue.getParameters();
    if (parameters.length < 1)
    {
      return null;
    }
    final CSSValue counterName = parameters[0];
    if (counterName instanceof CSSConstant == false)
    {
      return null;
    }
    final CSSConstant sval = (CSSConstant) counterName;
    final Object attribute = element.getAttribute(sval.getCSSText());
    // todo: This can be an URL or an image as well ...
    return new StringContent(String.valueOf(attribute));
  }

  private ContentToken handleCountersFunction (CSSFunctionValue functionValue,
                                               LayoutElement element,
                                               LayoutProcess process)
  {
    final CSSValue[] parameters = functionValue.getParameters();
    if (parameters.length < 2)
    {
      return null;
    }
    final CSSValue counterName = parameters[0];
    if (counterName instanceof CSSConstant == false)
    {
      return null;
    }
    final CSSConstant sval = (CSSConstant) counterName;

    final CSSValue separatorValue = parameters[1];
    if (separatorValue instanceof CSSStringValue == false)
    {
      return null;
    }
    final CSSStringValue separatorSval = (CSSStringValue) separatorValue;

    CounterStyle cstyle = DEFAULT_COUNTER_STYLE;
    final String counterNameStr = sval.getCSSText();
    if (parameters.length > 2)
    {
      final CSSValue styleValue = parameters[1];

      // cascading order: First ask the function itself ..
      cstyle = getCounterStyle(styleValue, process);
      if (cstyle == null)
      {
        // next ask the pageContext
        // todo: Define where the page context is stored..

        // if that fails, as the global document context.
        cstyle = process.getDocumentContext().getCounterStyle (counterNameStr);
      }
    }

    final StringBuffer buffer = new StringBuffer();
    buildCountersValue(element, counterNameStr,
            separatorValue.getCSSText(), cstyle, buffer);
    return new StringContent(separatorSval.getValue());
  }

  private void buildCountersValue (final LayoutElement element,
                                   final String counterName,
                                   final String separator,
                                   final CounterStyle cstyle,
                                   final StringBuffer buffer)
  {
    if (element.getParent() != null)
    {
      buildCountersValue(element.getParent(), counterName, separator, cstyle, buffer);
    }

    ElementContext elementContext = element.getElementContext();
    if (elementContext.isCounterDefined(counterName))
    {
      if (buffer.length() != 0)
      {
        buffer.append(separator);
      }

      final int cval = elementContext.getCounterValue(counterName);
      final String counterText = cstyle.getCounterValue(cval);
      buffer.append(counterText);
    }
  }

  private ContentToken handleCounterFunction (CSSFunctionValue functionValue,
                                              LayoutElement element,
                                              LayoutProcess process)
  {
    final CSSValue[] parameters = functionValue.getParameters();
    if (parameters.length < 1)
    {
      return null;
    }
    final CSSValue counterName = parameters[0];
    if (counterName instanceof CSSConstant == false)
    {
      return null;
    }
    final CSSConstant sval = (CSSConstant) counterName;
    final String counterNameStr = sval.getCSSText();
    final int cval = element.getElementContext().getCounterValue(counterNameStr);

    CounterStyle cstyle = DEFAULT_COUNTER_STYLE;
    if (parameters.length > 1)
    {
      final CSSValue styleValue = parameters[1];

      // cascading order: First ask the function itself ..
      cstyle = getCounterStyle(styleValue, process);
      if (cstyle == null)
      {
        // next ask the pageContext
        // todo: Define where the page context is stored..

        // if that fails, as the global document context.
        cstyle = process.getDocumentContext().getCounterStyle (counterNameStr);
      }
    }
    final String counterText = cstyle.getCounterValue(cval);
    return new StringContent(counterText);
  }

  private CounterStyle getCounterStyle (final CSSValue value,
                                        final LayoutProcess process)
  {
    if (value instanceof CSSConstant == false)
    {
      return null;
    }

    final String styleName = value.getCSSText();
    if ("normal".equals(styleName))
    {
      // use document context to resolve the counter's default style.
      return null;
    }

    final CounterStyle style = CounterStyleFactory.getInstance().getCounterStyle(styleName);
    if (style != null)
    {
      return style;
    }
    return null;
  }
}
