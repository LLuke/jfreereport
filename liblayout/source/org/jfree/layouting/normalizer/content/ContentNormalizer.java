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
 * ContentNormalizer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ContentNormalizer.java,v 1.1 2006/07/11 13:45:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.content;

import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.CloseQuoteToken;
import org.jfree.layouting.layouter.content.computed.ComputedToken;
import org.jfree.layouting.layouter.content.computed.ContentsToken;
import org.jfree.layouting.layouter.content.computed.CounterToken;
import org.jfree.layouting.layouter.content.computed.CountersToken;
import org.jfree.layouting.layouter.content.computed.OpenQuoteToken;
import org.jfree.layouting.layouter.content.computed.VariableToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedStringToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedToken;
import org.jfree.layouting.layouter.content.statics.StaticTextToken;
import org.jfree.layouting.layouter.context.ContentSpecification;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.DefaultLayoutContext;
import org.jfree.layouting.layouter.context.DefaultPageContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.QuotesPair;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.DefaultStyleResolver;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.page.PageAreaType;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.util.Log;

/**
 * This class is responsible for normalizing content from the 'content' style
 * property and for hiding content that has 'display:none' set. The
 * ContentNormalizer also resolves all styles for elements.
 * <p/>
 * Pagebreaks are determined in the model-builder or the layouter. A pagebreak
 * is only activated if it affects a line-box; the normalizer has no information
 * about lineboxes and therefore cannot perform any pagebreak computation at
 * all.
 * <p/>
 * Todo: Content that has been ignored because there was no 'content:contents'
 * definition for it, should have this content moved into the '::alternate'
 * pseudo-element. This one can be used to build footnotes and other fancy
 * stuff.
 * <p/>
 * More todo: Quote-Level cannot be resolved, until the content has been
 * processed by the renderer. The Quote-Tokens need to be passed down to the
 * renderer unchanged; they need the defined quotes for the current element when
 * being printed.
 * <p/>
 * The language is currently unresolved. It needs to be takes from the parent
 * context or the xml:lang attribute. Resolving the language using stylesheets
 * does not work, as there is a language-matching rule which depends on that
 * value.
 * <p/>
 * Todo: DisplayNone does not remove the element from the document tree. Quote:
 * <p/>
 * The element is not rendered. The rendering is the same as if the element had
 * been removed from the document tree, except for possible effects on counters
 * (see [generated] or [paged]).
 * <p/>
 * [generated]: An element that is not displayed ('display' set to 'none')
 * cannot increment or reset a counter.
 * <p/>
 * Note that :before and :after pseudo elements of this element are also not
 * rendered, see [generated].)
 *
 * @author Thomas Morgner
 */
public class ContentNormalizer implements Normalizer
{
  protected static class ContentNormalizerState implements State
  {
    private State styleResolverState;
    private State modelBuilderState;
    private LayoutProcess layoutProcess;

    // The LayoutElements are immutable; only their LayoutContext may change
    private LayoutElement currentElement;
    private LayoutElement currentSilbling;

    private long nextId;
    private State recordingContentNormalizerState;

    public ContentNormalizerState()
    {
    }

    public State getRecordingContentNormalizerState()
    {
      return recordingContentNormalizerState;
    }

    public void setRecordingContentNormalizerState(final State recordingContentNormalizerState)
    {
      this.recordingContentNormalizerState = recordingContentNormalizerState;
    }

    public State getStyleResolverState()
    {
      return styleResolverState;
    }

    public void setStyleResolverState(final State styleResolverState)
    {
      this.styleResolverState = styleResolverState;
    }

    public State getModelBuilderState()
    {
      return modelBuilderState;
    }

    public void setModelBuilderState(final State modelBuilderState)
    {
      this.modelBuilderState = modelBuilderState;
    }

    public LayoutProcess getLayoutProcess()
    {
      return layoutProcess;
    }

    public void setLayoutProcess(final LayoutProcess layoutProcess)
    {
      this.layoutProcess = layoutProcess;
    }

    public LayoutElement getCurrentElement()
    {
      return currentElement;
    }

    public void setCurrentElement(final LayoutElement currentElement)
    {
      this.currentElement = currentElement;
    }

    public LayoutElement getCurrentSilbling()
    {
      return currentSilbling;
    }

    public void setCurrentSilbling(final LayoutElement currentSilbling)
    {
      this.currentSilbling = currentSilbling;
    }

    public long getNextId()
    {
      return nextId;
    }

    public void setNextId(final long nextId)
    {
      this.nextId = nextId;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      ContentNormalizer contentNormalizer =
              new ContentNormalizer(layoutProcess, false);
      contentNormalizer.restore(this);
      return contentNormalizer;
    }
  }

  private static final long NO_PARENT = -1;

  private StyleResolver styleResolver;
  private ModelBuilder modelBuilder;

  private LayoutElement currentElement;
  private LayoutElement currentSilbling;

  private LayoutProcess layoutProcess;
  private RecordingContentNormalizer recordingContentNormalizer;
  private long nextId;
  private int ignoreContext;

  /**
   * The quote-level is a global setting. The quotes get resolved during
   * the content generation phase.
   */
  private int quoteLevel;


  public ContentNormalizer(final LayoutProcess layoutProcess)
  {
    this(layoutProcess, true);
  }

  protected ContentNormalizer(final LayoutProcess layoutProcess,
                              final boolean init)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException("LayoutProcess must not be null.");
    }
    this.layoutProcess = layoutProcess;
    if (init)
    {
      this.modelBuilder = layoutProcess.getOutputProcessor().createModelBuilder
              (layoutProcess);
    }
  }

  public void startDocument()
          throws IOException, NormalizationException
  {
    styleResolver = new DefaultStyleResolver();
    styleResolver.initialize(layoutProcess);

    final DefaultPageContext dpc = new DefaultPageContext();
    final PageAreaType[] pat = PageAreaType.getPageAreas();
    for (int i = 0; i < pat.length; i++)
    {
      final PageAreaType pageAreaType = pat[i];
      final LayoutStyle style = styleResolver.resolvePageStyle
              (CSSAutoValue.getInstance(), new PseudoPage[0], pageAreaType);
      dpc.setAreaDefinition(pageAreaType, style);
    }

    modelBuilder.startDocument(dpc);
  }

  /**
   * Starts a new element. The element uses the given namespace and tagname. The
   * element's attributes are given as collection, each attribute is keyed with
   * a namespace and attributename. The values contained in the attributes are
   * not defined.
   *
   * @param namespace
   * @param tag
   * @param attributes
   * @throws NormalizationException
   * @throws IOException
   */
  public void startElement(String namespace,
                           String tag,
                           AttributeMap attributes)
          throws NormalizationException, IOException
  {
    // the contents of what is stored here is rather uninteresting,
    // important is the fact *that* we saved something.
    //
    // This is not correct yet, as right now, this would not record a
    // html->head->title if the html->head element is not displayed.
    if (recordingContentNormalizer != null)
    {
      recordingContentNormalizer.startElement(namespace, tag, attributes);
      return;
    }

    startElementInternal(namespace, tag, null, attributes);
  }

  protected void startElementInternal(String namespace,
                                      String tag,
                                      String pseudo,
                                      AttributeMap attributes)
          throws NormalizationException, IOException
  {

    final ContextId ctxId = new ContextId
            (ContextId.SOURCE_NORMALIZER, NO_PARENT, nextId);
    nextId += 1;

    final LayoutContext layoutContext = new DefaultLayoutContext
            (ctxId, namespace, tag, pseudo, attributes);
    final LayoutElement element =
            new LayoutElement(currentElement, currentSilbling, layoutContext);
    currentElement = element;
    currentSilbling = null;

    styleResolver.resolveStyle(element);
    if (isStringRecordingNeeded(element))
    {
      // this will record all calls. We later go through and extract all
      // CDATA - regardless of the element content. Extracting is done,
      // even if the element has the display:none condition.
      recordingContentNormalizer = new RecordingContentNormalizer();
    }
    else
    {
      startCurrentElement();
    }
  }

  /**
   * Processes the current element. The element has been resolved fully and it
   * is made sure that it can be processed right now (No pinning is enabled).
   *
   * @throws PageBreakException
   * @throws NormalizationException
   * @throws IOException
   */
  private void startCurrentElement()
          throws NormalizationException, IOException
  {
    final LayoutContext layoutContext = currentElement.getLayoutContext();
    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue displayRole = style.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.NONE.equals(displayRole))
    {
      // ignore that element ..
      ignoreContext += 1;
      Log.debug("Ignoring element (and all childs) of " +
              layoutContext.getPseudoElement() +
              " as it has Display:NONE");
    }
    else if (ignoreContext > 0)
    {
      // keep track of our distance to the first hidden element
      ignoreContext += 1;
    }

//    Log.debug ("Element " + layoutContext.getTagName() + " " + displayRole);

    // update counters and strings ...
    if (DisplayRole.LIST_ITEM.equals(displayRole))
    {
      currentElement.incrementCounter("list-item", 1);
    }


    // the whole design here should be queued. If we encounter the contents
    // token, then we have to wait for that content before we can continue.
    // thats DOM oriented and not streaming, so we do our own fast version
    // instead ..

    // now process the 'x-liblayout-alternate-text'
    if (ignoreContext == 0)
    {

      // check, whether the element allows content at all..
      // isAllowContentProcessing is false, if the 'content' property
      // had been set to 'none' or 'inhibit'.
      final ContentSpecification contentSpec =
              currentElement.getLayoutContext().getContentSpecification();
      if (contentSpec.isAllowContentProcessing() == false)
      {
        // Quote-the-standard:
        // -------------------
        // On elements, this inhibits the children of the element from
        // being rendered as children of this element, as if the element
        // was empty.
        //
        // On pseudo-elements, this inhibits the creation of the pseudo-element,
        // as if 'display' computed to 'none'.
        //
        // In both cases, this further inhibits the creation of any
        // pseudo-elements which have this pseudo-element as a superior.
        if (contentSpec.isInhibitContent() &&
            layoutContext.isPseudoElement())
        {
          Log.debug("Starting to ignore childs of psuedo element " +
                  layoutContext.getTagName() + ":" +
                  layoutContext.getPseudoElement() +
                  " as it inhibits content creation.");
          modelBuilder.startElement(currentElement.getLayoutContext());
          ignoreContext += 1;
        }
        else
        {
          Log.debug("Starting to ignore childs of element " +
                  layoutContext.getTagName() +
                  " as it inhibits content creation.");
          generateOutsidePseudoElements(currentElement);
          modelBuilder.startElement(currentElement.getLayoutContext());
          generateBeforePseudoElements(currentElement);
          ignoreContext += 1;
        }
      }
      else
      {
        // normal content processing here ...
        generateOutsidePseudoElements(currentElement);
        modelBuilder.startElement(currentElement.getLayoutContext());
        generateBeforePseudoElements(currentElement);

        // check, whether we have a 'contents' token inside the 'content'
        // style property.
        generateContentBefore(currentElement);
        if (currentElement.isContentsConsumed() == false)
        {
          // we have none, so we will ignore all other content of that element
          // lets generate an 'alternate-element'
          if (generateAlternateContent(currentElement) == false)
          {
            Log.debug("Starting to ignore childs of element " + currentElement +
                    " as it does not contain the 'contents' token.");
            ignoreContext += 1;
          }
          else
          {
            // OK, this means, that this element is still open. Take care of
            // that or suffer ..
          }
        }
      }
    }
    else
    {
      // this is an ignored context. We check for counters and string
      // definitions but do not forward anything to the display-model-builder.
    }
  }

  protected boolean generateBeforePseudoElements(LayoutElement element)
          throws IOException, NormalizationException
  {
    final LayoutContext layoutContext = element.getLayoutContext();

    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue displayRole = style.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.LIST_ITEM.equals(displayRole))
    {
      if (styleResolver.isPseudoElementStyleResolvable(element, "marker"))
      {
        startElementInternal(layoutContext.getNamespace(),
                layoutContext.getTagName(), "marker",
                layoutContext.getAttributes());
        endElement();
      }
    }

    // it might be against the standard, but we do not accept the
    // contents-token here.
    if (styleResolver.isPseudoElementStyleResolvable(element, "before"))
    {
      startElementInternal(layoutContext.getNamespace(),
              layoutContext.getTagName(), "before",
              layoutContext.getAttributes());
      endElement();
    }

    return false;
  }

  /**
   * Return true, if the alternate element contained the contents token.
   * In that case, this element receives all content. We have to make sure
   * that the beast gets closed down later ..
   *
   * @param element
   * @return
   */
  private boolean generateAlternateContent(final LayoutElement element)
          throws IOException, NormalizationException
  {
    // it might be against the standard, but we do not accept the
    // contents-token here.
    if (element.isContentsConsumed())
    {
      return false;
    }
    // we do not generate an alternate element, if there is no need to do so.
    if (styleResolver.isPseudoElementStyleResolvable(element, "alternate"))
    {
      final LayoutContext layoutContext = element.getLayoutContext();
      startElementInternal(layoutContext.getNamespace(),
              layoutContext.getTagName(), "alternate",
              layoutContext.getAttributes());
      if (element.isContentsConsumed())
      {
        // if the alternate element consumes the content, fine. We have
        // to close the element later ..
        element.setAlternateOpen(true);
        return true;
      }
      endElement();
    }
    return false;
  }

  private boolean generateOutsidePseudoElements(final LayoutElement element)
          throws IOException, NormalizationException
  {
    return false;
  }

  /**
   * There are two cases, when elements need recording. First, if there is a
   * 'string-set' property with a 'contents()' token. We have to copy all
   * char-data content from the element. The content will be availble *after*
   * the element has been closed. It will not be available while the element is
   * processed. All content processing is suspended until the whole char-data
   * has been extracted. Generated content is not extracted at all - we work on
   * the user's data only.
   * <p/>
   * The recorded content is replayed when the element ends (as signaled by
   * the input-feed) so that the string-content WILL be available for all
   * childs. Warning: This possibly duplicates content and is definitly an
   * expensive operation.
   */
  private boolean isStringRecordingNeeded(final LayoutElement element)
  {
    final ContentSpecification contentSpecification =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] strings = contentSpecification.getStrings();

    // todo: This might be invalid.
    
    final LayoutStyle style = element.getLayoutContext().getStyle();
    final CSSValue value = style.getValue(ContentStyleKeys.STRING_DEFINE);
    if (value == null)
    {
      return false;
    }
    if (value instanceof CSSValueList)
    {
      final CSSValueList list = (CSSValueList) value;
      if (list.getLength() == 0)
      {
        return false;
      }
      for (int i = 0; i < list.getLength(); i++)
      {
        CSSValue val = list.getItem(i);
        if (val instanceof ContentsToken)
        {
          return true;
        }
      }
    }
    return false;
  }

  protected boolean generateContentBefore(LayoutElement element)
          throws IOException, NormalizationException
  {
    final ContentSpecification cspec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = cspec.getContents();
    for (int i = 0; i < tokens.length; i++)
    {
      final ContentToken token = tokens[i];
      if (token instanceof ContentsToken)
      {
        element.setContentsConsumed(true);
        return true;
      }
      if (token instanceof ComputedToken)
      {
        final ContentToken resolved = computeToken(token, cspec);
        if (resolved != null)
        {
          addContent(resolved);
        }
      }
      else
      {
        addContent(token);
      }
    }
    return false;
  }

  protected void generateContentAfter(LayoutElement element)
          throws IOException, NormalizationException
  {
    final ContentSpecification cspec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = cspec.getContents();
    int posContent = 0;
    // first skip everything up to the first 'contents' token.
    for (; posContent < tokens.length; posContent++)
    {
      final ContentToken token = tokens[posContent];
      if (token instanceof ContentsToken)
      {
        break;
      }
    }
    posContent += 1;
    for (; posContent < tokens.length; posContent++)
    {
      final ContentToken token = tokens[posContent];
      // subsequent contents tokens are ignored ..
      if (token instanceof ContentsToken)
      {
        continue;
      }

      if (token instanceof ComputedToken)
      {
        final ContentToken resolved = computeToken(token, cspec);
        if (resolved != null)
        {
          addContent(resolved);
        }
      }
      else
      {
        addContent(token);
      }
    }
  }


  protected void generateAfterPseudoElements(LayoutElement element)
          throws IOException, NormalizationException
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    // it might be against the standard, but we do not accept the
    // contents-token here.
    if (styleResolver.isPseudoElementStyleResolvable(element, "after"))
    {
      startElementInternal(layoutContext.getNamespace(),
              layoutContext.getTagName(), "after",
              layoutContext.getAttributes());
      endElement();
    }

  }

  /**
   * Adds text content to the current element.
   *
   * @param text
   * @throws NormalizationException
   * @throws IOException
   */
  public void addText(String text) throws NormalizationException, IOException
  {
    if (ignoreContext > 0)
    {
      Log.debug ("Ignored context active: " + ignoreContext);
      if (recordingContentNormalizer != null)
      {
        recordingContentNormalizer.addText(text);
      }
      return;
    }

    //Log.debug ("Adding: " + text);
    modelBuilder.addContent(new StaticTextToken(text));
  }

  private void addContent(final ContentToken token)
          throws NormalizationException
  {
    if (token instanceof ComputedToken)
    {
      throw new NormalizationException("ComputedContent cannot be added.");
    }
    if (ignoreContext == 0)
    {
      modelBuilder.addContent(token);
    }
  }

  private ResolvedToken computeToken(final ContentToken token,
                                     final ContentSpecification cspec)
  {
    if (token instanceof CloseQuoteToken)
    {
      CloseQuoteToken closeQuoteToken = (CloseQuoteToken) token;
      if (closeQuoteToken.isSurpressQuoteText())
      {
        quoteLevel -= 1;
        return null;
      }
      // Important: The quote level must be decreased before the quote gets
      // queried. (To be in sync with the open-quote thing..
      quoteLevel -= 1;
      final QuotesPair currentQuote = getQuotesPair(cspec);
      if (currentQuote != null)
      {
        return new ResolvedStringToken
                (closeQuoteToken, currentQuote.getCloseQuote());
      }
    }
    else if (token instanceof OpenQuoteToken)
    {
      OpenQuoteToken openQuoteToken = (OpenQuoteToken) token;
      if (openQuoteToken.isSurpressQuoteText())
      {
        quoteLevel += 1;
        return null;
      }

      // Important: The quote level must be increased after the quote has
      // been queried. Our quoting implementation uses zero-based arrays.
      final QuotesPair currentQuote = getQuotesPair(cspec);
      quoteLevel += 1;
      if (currentQuote != null)
      {
        return new ResolvedStringToken
                (openQuoteToken, currentQuote.getOpenQuote());
      }
    }
    else if (token instanceof VariableToken)
    {
      final VariableToken variableToken = (VariableToken) token;
      final String resolvedText =
              currentElement.getString(variableToken.getVariable());
      return new ResolvedStringToken(variableToken, resolvedText);
    }
    else if (token instanceof CounterToken)
    {
      final CounterToken counterToken = (CounterToken) token;
      final String name = counterToken.getName();
      final CounterStyle style = counterToken.getStyle();
      final String resolvedText =
              style.getCounterValue(currentElement.getCounterValue(name));
      return new ResolvedStringToken(counterToken, resolvedText);
    }
    else if (token instanceof CountersToken)
    {
      final CountersToken counterToken = (CountersToken) token;
      final String name = counterToken.getName();
      final CounterStyle style = counterToken.getStyle();
      final String separator = counterToken.getSeparator();
      final StringBuffer buffer = new StringBuffer();
      while (currentElement != null)
      {
        if (currentElement.isCounterDefined(name))
        {
          if (buffer.length() > 0)
          {
            buffer.append(separator);
          }

          final String resolvedText =
                  style.getCounterValue(currentElement.getCounterValue(name));
          buffer.append(resolvedText);
        }
        currentElement = currentElement.getParent();
      }

      return new ResolvedStringToken(counterToken, buffer.toString());
    }
    // not recognized ...
    return null;
  }

  private QuotesPair getQuotesPair(final ContentSpecification cspec)
  {
    QuotesPair[] qps = cspec.getQuotes();
    final int maxQuote = Math.min(qps.length - 1, quoteLevel);
    if (maxQuote == -1)
    {
      return null;
    }
    else
    {
      return qps[maxQuote];
    }
  }

  /**
   * Ends the current element. The namespace and tagname are given for
   * convienience.
   *
   * @param namespace
   * @param tag
   * @throws NormalizationException
   * @throws IOException
   */
  public void endElement()
          throws NormalizationException, IOException
  {

//    final LayoutElement element = currentElement;
    if (currentElement == null)
    {
      throw new NullPointerException
              ("This is unexpected: I dont have a current element.");
    }

    if (ignoreContext > 1)
    {
      // this is an inner ignored element.
      ignoreContext -= 1;
      if (recordingContentNormalizer != null)
      {
        recordingContentNormalizer.endElement();
      }
      currentSilbling = currentElement;
      currentElement = currentElement.getParent();
      return;
    }

    if (ignoreContext == 1)
    {
      // check, what caused the trouble ..
      LayoutContext layoutContext = currentElement.getLayoutContext();
      LayoutStyle style = layoutContext.getStyle();
      final CSSValue displayRole = style.getValue(BoxStyleKeys.DISPLAY_ROLE);
      if (DisplayRole.NONE.equals(displayRole))
      {
        // ok, no need to clean up.
        currentSilbling = currentElement;
        currentElement = currentElement.getParent();
        ignoreContext = 0;
        return;
      }
    }

    if (currentElement.isAlternateOpen())
    {
      endElement();
    }

    if (ignoreContext == 1)
    {
      LayoutContext layoutContext = currentElement.getLayoutContext();
      final ContentSpecification contentSpec =
              layoutContext.getContentSpecification();
      if (contentSpec.isAllowContentProcessing() == false)
      {
        if (!contentSpec.isInhibitContent() ||
            !layoutContext.isPseudoElement())
        {
          // clean up (2): The element generated some stuff..
          generateAfterPseudoElements(currentElement);
        }
      }
      else
      {
        generateContentAfter(currentElement);
        // clean up (2): The element generated some stuff..
        generateAfterPseudoElements(currentElement);
        // clean up (1): Just finish the element
      }
      ignoreContext -= 1;
    }
    else
    {
      generateContentAfter(currentElement);
      generateAfterPseudoElements(currentElement);
    }

    // todo: The recording normalizer is not yet handled ...

    modelBuilder.endElement();

    currentSilbling = currentElement;
    currentElement = currentElement.getParent();
  }

  public void endDocument()
          throws IOException, NormalizationException
  {
    modelBuilder.endDocument();
  }

  public void handlePageBreak(final CSSValue pageName,
                              final PseudoPage[] pseudoPages)
  {

    final DefaultPageContext dpc = new DefaultPageContext();
    final PageAreaType[] pat = PageAreaType.getPageAreas();
    for (int i = 0; i < pat.length; i++)
    {
      final PageAreaType pageAreaType = pat[i];
      final LayoutStyle style =
              styleResolver.resolvePageStyle(pageName, pseudoPages,
                      pageAreaType);
      dpc.setAreaDefinition(pageAreaType, style);
    }

    // handle the page-resets.


    modelBuilder.handlePageBreak(dpc);
  }

  protected ContentNormalizerState createSaveState()
  {
    return new ContentNormalizerState();
  }

  protected void fillState(ContentNormalizerState state) throws StateException
  {
    state.setNextId(nextId);
    state.setCurrentElement(currentElement);
    state.setCurrentSilbling(currentSilbling);
    state.setLayoutProcess(layoutProcess);
    state.setModelBuilderState(modelBuilder.saveState());
    state.setStyleResolverState(styleResolver.saveState());
    if (recordingContentNormalizer != null)
    {
      state.setRecordingContentNormalizerState(
              recordingContentNormalizer.saveState());
    }
  }

  public State saveState() throws StateException
  {
    ContentNormalizerState state = createSaveState();
    fillState(state);
    return state;
  }

  protected void restore(final ContentNormalizerState state)
          throws StateException
  {
    this.currentElement = state.getCurrentElement();
    this.currentSilbling = state.getCurrentSilbling();
    this.nextId = state.getNextId();
    this.modelBuilder = (ModelBuilder)
            state.getModelBuilderState().restore(layoutProcess);
    this.styleResolver = (StyleResolver)
            state.getStyleResolverState().restore(layoutProcess);
    if (state.getRecordingContentNormalizerState() != null)
    {
      this.recordingContentNormalizer = (RecordingContentNormalizer)
              state.getRecordingContentNormalizerState().restore(layoutProcess);
    }
  }

  /**
   * Returns the renderer. The renderer is the last step in the processing
   * chain. The ModelBuilder and ContentGenerator steps are considered internal,
   * as they may refeed the normalizer.
   *
   * @return
   */
  public Renderer getRenderer()
  {
    return modelBuilder.getRenderer();
  }

  public StyleResolver getStyleResolver()
  {
    if (styleResolver == null)
    {
      throw new IllegalStateException("Document has not yet been initialized?");
    }
    return styleResolver;
  }
}
