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
 * $Id: ContentNormalizer.java,v 1.2 2006/04/23 15:18:18 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer;

import java.util.Stack;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.model.ContextId;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.model.content.CloseQuoteToken;
import org.jfree.layouting.model.content.ContentSpecification;
import org.jfree.layouting.model.content.ContentToken;
import org.jfree.layouting.model.content.ExternalContentToken;
import org.jfree.layouting.model.content.OpenQuoteToken;
import org.jfree.layouting.model.content.QuotesPair;
import org.jfree.layouting.model.content.ResourceContentToken;
import org.jfree.layouting.model.content.StringContentToken;
import org.jfree.resourceloader.Resource;
import org.jfree.util.Log;

/**
 * This class is responsible for normalizing content from the 'content' style
 * property and for hiding content that has 'display:none' set.
 *
 * @author Thomas Morgner
 */
public class ContentNormalizer implements Normalizer
{
  private StyleResolver styleResolver;
  private Normalizer child;
  private Stack openElements;
  private ContextId ignoredElement;
  private LayoutProcess layoutProcess;

  public ContentNormalizer(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.layoutProcess = layoutProcess;
    this.child = layoutProcess.getNormalizer();
    this.openElements = new Stack();
  }

  public void startElement(LayoutElement element)
  {
    if (ignoredElement != null)
    {
      Log.debug ("Ignored element " + element + " as it has Display:NONE");
      return;
    }
    openElements.push(element);
    styleResolver.resolveStyle(element);

    final BoxSpecification boxSpec =
            element.getLayoutContext().getBoxSpecification();
    if (DisplayRole.NONE.equals(boxSpec.getDisplayRole()))
    {
      // ignore that element ..
      ignoredElement = element.getContextId();
      Log.debug ("Ignored element " + element + " as it has Display:NONE");
      return;
    }

    final ContentSpecification contentSpec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = contentSpec.getContents();
    if (tokens.length == 1)
    {
      final ContentToken tk = tokens[0];
      if (isElementWithPseudoElements(element) == false &&
          (tk.getType() == ContentToken.EXTERNAL_CONTENT ||
          tk.getType() == ContentToken.RESOURCE_CONTENT))
      {
        // now that was easy: Just add it as plain replaced content,
        // and we are done.
        child.addReplacedElement(element);
        return;
      }
    }

    child.startElement(element);

    // check, whether the element allows content at all..
    // isAllowContentProcessing is false, if the 'content' property
    // had been set to 'none' or 'inhibit'.
    if (contentSpec.isAllowContentProcessing() == false)
    {
      // the element does not allow content processing at all.
      ignoredElement = element.getContextId();
      Log.debug ("Ignored element " + element + " as it does not allow content processing.");
      return;
    }

    // generate all 'before' pseudo-elements. This generates not only '::before'
    // but also any PE that is rendered before the real content
    generateElementBefore(element);

    // check, whether we have a 'contents' token inside
    if (generateContentBefore(element) == false)
    {
      // we have none, so we will ignore all other content of that element
      // however, we will generate ::before and ::after pseudo-elements and
      // we will process whatever content has been given.
      Log.debug ("Starting to ignore childs of element " + element +
                 " as it does not contain the 'contents' token.");
      ignoredElement = element.getContextId();
    }
  }

  /**
   * Check, whether this element will generate pseudo-elements either with
   * the 'before', 'after' or 'outside' rule.
   *
   * @param element
   * @return
   */
  protected boolean isElementWithPseudoElements(final LayoutElement element)
  {
    return false;
  }

  public void addText(LayoutTextNode text)
  {
    if (ignoredElement != null)
    {
      return;
    }

    child.addText(text);
  }

  public void addReplacedElement(LayoutElement element)
  {
    if (ignoredElement != null)
    {
      return;
    }

    child.addReplacedElement(element);
  }

  public void endElement(LayoutElement element)
  {
    if (element.getContextId().equals(ignoredElement))
    {
      ignoredElement = null;
    }
    else if (ignoredElement != null)
    {
      return;
    }

    final BoxSpecification boxSpec =
            element.getLayoutContext().getBoxSpecification();
    if (DisplayRole.NONE.equals(boxSpec.getDisplayRole()))
    {
      // ignore that element ..
      return;
    }

    final ContentSpecification contentSpec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = contentSpec.getContents();
    if (tokens.length == 1)
    {
      final ContentToken tk = tokens[0];
      if (isElementWithPseudoElements(element) == false &&
          (tk.getType() == ContentToken.EXTERNAL_CONTENT ||
          tk.getType() == ContentToken.RESOURCE_CONTENT))
      {
        return;
      }
    }

    generateContentAfter(element);
    generateElementAfter(element);
    child.endElement(element);
    openElements.pop();
  }

  protected void generateElementBefore(LayoutElement element)
  {
    // not yet ...
  }

  protected boolean generateContentBefore(LayoutElement element)
  {
    final ContentSpecification cspec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = cspec.getContents();
    for (int i = 0; i < tokens.length; i++)
    {
      final ContentToken token = tokens[i];
      if (token.getType() == ContentToken.CONTENTS_CONTENT)
      {
        return true;
      }
      handleContent(token, cspec, element);
    }
    return false;
  }

  protected void generateContentAfter(LayoutElement element)
  {
    final ContentSpecification cspec =
            element.getLayoutContext().getContentSpecification();
    final ContentToken[] tokens = cspec.getContents();
    int posContent = 0;
    for (; posContent < tokens.length; posContent++)
    {
      final ContentToken token = tokens[posContent];
      if (token.getType() == ContentToken.CONTENTS_CONTENT)
      {
        break;
      }
    }
    posContent += 1;
    for (; posContent < tokens.length; posContent++)
    {
      final ContentToken token = tokens[posContent];
      if (token.getType() == ContentToken.CONTENTS_CONTENT)
      {
        continue;
      }
      handleContent(token, cspec, element);
    }
  }

  private void handleContent(final ContentToken token,
                             final ContentSpecification cspec,
                             final LayoutElement element)
  {
    switch(token.getType())
    {
      case ContentToken.CLOSE_QUOTE:
      {
        layoutProcess.getDocumentContext().closeQuote();
        CloseQuoteToken openQuoteToken = (CloseQuoteToken) token;
        if (openQuoteToken.isSurpressQuoteText())
        {
          break;
        }
        final QuotesPair currentQuote = getQuotesPair(cspec);
        if (currentQuote != null)
        {
          insertTextNode(element, currentQuote.getOpenQuote());
        }
        break;
      }
      case ContentToken.OPEN_QUOTE:
      {
        layoutProcess.getDocumentContext().openQuote();
        OpenQuoteToken openQuoteToken = (OpenQuoteToken) token;
        if (openQuoteToken.isSurpressQuoteText())
        {
          break;
        }
        final QuotesPair currentQuote = getQuotesPair(cspec);
        if (currentQuote != null)
        {
          insertTextNode(element, currentQuote.getOpenQuote());
        }
        break;
      }
      case ContentToken.STRING_CONTENT:
      {
        final StringContentToken stringContent = (StringContentToken) token;
        insertTextNode(element, stringContent.getContent());
        break;
      }
      case ContentToken.EXTERNAL_CONTENT:
      {
        final ExternalContentToken externalContent = (ExternalContentToken) token;
        insertExternalContent(element, externalContent.getData());
        break;
      }
      case ContentToken.RESOURCE_CONTENT:
      {
        final ResourceContentToken resourceContent = (ResourceContentToken) token;
        insertResourceContent(element, resourceContent.getContent());
        break;
      }
    }
  }

  private void insertResourceContent(final LayoutElement element,
                                     final Resource resource)
  {
    // generate an anonymous element ..
    final LayoutElement anonElement = new LayoutElement
            (layoutProcess.generateContextId(element.getContextId().getId()),
             layoutProcess.getOutputProcessor(),
              Namespaces.LIBLAYOUT_NAMESPACE,
              "resource-node");
    anonElement.setAttribute(Namespaces.LIBLAYOUT_NAMESPACE, "content", resource);
    anonElement.setParent(element, -1);
    startElement(anonElement);
    endElement(anonElement);
  }

  private void insertExternalContent(final LayoutElement element,
                                     final Object externalContent)
  {
    // generate an anonymous element ..
    final LayoutElement anonElement = new LayoutElement
            (layoutProcess.generateContextId(element.getContextId().getId()),
             layoutProcess.getOutputProcessor(),
              Namespaces.LIBLAYOUT_NAMESPACE,
              "external-node");
    anonElement.setAttribute(Namespaces.LIBLAYOUT_NAMESPACE, "content", externalContent);
    anonElement.setParent(element, -1);
    startElement(anonElement);
    endElement(anonElement);
  }

  private QuotesPair getQuotesPair(final ContentSpecification cspec)
  {
    QuotesPair[] qps = cspec.getQuotes();
    final int quoteLevel =
            layoutProcess.getDocumentContext().getQuoteLevel();
    final int maxQuote = Math.min (qps.length - 1, quoteLevel);
    if (maxQuote == -1)
    {
      return null;
    }
    else
    {
      return qps[maxQuote];
    }
  }

  private void insertTextNode (final LayoutElement element,
                               final String text)
  {
    final LayoutTextNode ctx = new LayoutTextNode
          (layoutProcess.generateContextId(element.getContextId().getId()),
           layoutProcess.getOutputProcessor(),
           text.toCharArray(), 0, text.length());
    ctx.setParent(element, -1);
    child.addText(ctx);

  }

  protected void generateElementAfter(LayoutElement element)
  {
  }

  public void startDocument()
  {
    child.startDocument();
    styleResolver = layoutProcess.getStyleResolver();
    styleResolver.initialize(layoutProcess);
  }

  public void endDocument()
  {
    child.endDocument();
  }
}
