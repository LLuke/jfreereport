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
 * DefaultStyleResolver.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultStyleResolver.java,v 1.9 2006/07/26 11:52:07 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver;

import java.util.Arrays;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.layouting.input.style.selectors.SelectorWeight;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.CSSStyleRuleComparator;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;


/**
 * A cascading style resolver. This resolver follows the cascading rules
 * as outlined by the Cascading Stylesheet Standard.
 *
 * @author Thomas Morgner
 */
public class DefaultStyleResolver extends AbstractStyleResolver implements StyleResolver
{
  private static class DefaultStyleResolverState extends AbstractStyleResolverState
  {
    private boolean strictStyleMode;
    private StyleRuleMatcher ruleMatcher;

    public DefaultStyleResolverState()
    {
    }

    public boolean isStrictStyleMode()
    {
      return strictStyleMode;
    }

    public void setStrictStyleMode(final boolean strictStyleMode)
    {
      this.strictStyleMode = strictStyleMode;
    }

    public StyleRuleMatcher getRuleMatcher()
    {
      return ruleMatcher;
    }

    public void setRuleMatcher(final StyleRuleMatcher ruleMatcher)
    {
      this.ruleMatcher = ruleMatcher;
    }

    protected AbstractStyleResolver create()
    {
      return new DefaultStyleResolver();
    }

    protected void fill(final AbstractStyleResolver resolver,
                        final LayoutProcess layoutProcess)
    {
      super.fill(resolver, layoutProcess);
      DefaultStyleResolver res = (DefaultStyleResolver) resolver;
      res.styleRuleMatcher = ruleMatcher;
      res.strictStyleMode = strictStyleMode;
    }
  }

  private boolean strictStyleMode;

  private StyleRuleMatcher styleRuleMatcher;

  public DefaultStyleResolver ()
  {
  }

  public void initialize (final LayoutProcess layoutProcess)
  {
    super.initialize(layoutProcess);
    DocumentContext documentContext = layoutProcess.getDocumentContext();
    this.styleRuleMatcher =
            DocumentContextUtility.getStyleRuleMatcher(documentContext);
    this.styleRuleMatcher.initialize(layoutProcess);
    this.strictStyleMode = Boolean.TRUE.equals
            (documentContext.getMetaAttribute(DocumentContext.STRICT_STYLE_MODE));

    loadInitialStyle();
  }

  protected void resolveOutOfContext (LayoutElement element)
  {
    // as this styleresolver is not statefull, we can safely call the resolve
    // style method. A statefull resolver would have to find other means.
    resolveStyle(element);
  }

  /**
   * Performs tests, whether there is a pseudo-element definition for the given
   * element. The element itself can be a pseudo-element as well.
   *
   * @param element
   * @param pseudo
   * @return
   */
  public boolean isPseudoElementStyleResolvable(LayoutElement element,
                                                String pseudo)
  {
    return styleRuleMatcher.isMatchingPseudoElement(element, pseudo);
  }

  /**
   * Resolves the style. This is guaranteed to be called in the order of the
   * document elements traversing the document tree using the
   * 'deepest-node-first' strategy.
   *
   * @param element the elemen that should be resolved.
   */
  public void resolveStyle(LayoutElement element)
  {
    // this is a three stage process
    final LayoutContext layoutContext = element.getLayoutContext();
    final LayoutStyle style = layoutContext.getStyle();
    final StyleKey[] keys = getKeys();

//    Log.debug ("Resolving style for " +
//            layoutContext.getTagName() + ":" +
//            layoutContext.getPseudoElement());

    // Stage 0: Initialize with the built-in defaults
    // Stage 1a: Add the parent styles (but only the one marked as inheritable).
    final LayoutElement parent = element.getParent();
    final LayoutStyle initialStyle = getInitialStyle();
    final LayoutStyle parentStyle;
    if (parent != null)
    {
      parentStyle = parent.getLayoutContext().getStyle();
    }
    else
    {
      parentStyle = initialStyle;
    }

    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      if (key.isInherited())
      {
        style.setValue(key, parentStyle.getValue(key));
      }
      else
      {
        style.setValue(key, initialStyle.getValue(key));
      }
    }

    // Stage 1b: Find all matching stylesheets for the given element
    performSelectionStep(element, style);

    // Stage 1c: Add the contents of the style attribute, if there is one ..
    // the libLayout style is always added: This is a computed style and the hook
    // for a element neutral user defined tweaking ..

    final AttributeMap attributes = element.getLayoutContext().getAttributes();
    final Object libLayoutStyleValue = attributes.getAttribute
            (Namespaces.LIBLAYOUT_NAMESPACE, "style");
    // You cannot override element specific styles with that. So an HTML-style
    // attribute has move value than a LibLayout-style attribute.
    addStyleFromAttribute(style, libLayoutStyleValue, element);

    if (strictStyleMode)
    {
      performStrictStyleAttr(element, style);
    }
    else
    {
      performCompleteStyleAttr(element, style);
    }

    Log.debug ("Vertical Align: " + style.getValue(LineStyleKeys.VERTICAL_ALIGN) + " on Element " + layoutContext.getTagName());

    // Stage 2: Compute the 'specified' set of values.
    // Find all explicitly inherited styles and add them from the parent.
    final CSSInheritValue inheritInstance = CSSInheritValue.getInstance();
    for (int i = 0; i < keys.length; i++)
    {
      StyleKey key = keys[i];
      Object value = style.getValue(key);
      if (inheritInstance.equals(value))
      {
        style.setValue(key, parentStyle.getValue(key));
      }
    }

    Log.debug ("Vertical Align: " + style.getValue(LineStyleKeys.VERTICAL_ALIGN) + " on Element " + layoutContext.getTagName());

    // Stage 3:  Compute the computed value set.
    ResolverFactory.getInstance().performResolve
            (getLayoutProcess(), element, style);

  }

  /**
   * Check, whether there is a known style attribute for the element's namespace
   * and if so, grab its value. This method uses strict conformance to the XML
   * rules and thus it does not evaluate foreign styles.
   * <p/>
   *
   * @param node
   * @param style
   */
  private void performStrictStyleAttr (LayoutElement node, LayoutStyle style)
  {
    final LayoutContext layoutContext = node.getLayoutContext();
    final String namespace = layoutContext.getNamespace();
    if (namespace == null)
    {
      return;
    }

    final NamespaceCollection namespaces = getNamespaces();
    final NamespaceDefinition ndef = namespaces.getDefinition(namespace);
    if (ndef == null)
    {
      return;
    }

    final AttributeMap attributes = layoutContext.getAttributes();
    final String[] styleAttrs = ndef.getStyleAttribute
            (layoutContext.getTagName());
    for (int i = 0; i < styleAttrs.length; i++)
    {
      final String attr = styleAttrs[i];
      final Object styleValue = attributes.getAttribute(namespace, attr);
      addStyleFromAttribute(style, styleValue, node);
    }
  }

  /**
   * Check, whether there are known style attributes and if so, import them.
   * This method uses a relaxed syntax and imports all known style attributes
   * ignoring the element's defined namespace. This allows to add styles to
   * elements which would not support styles otherwise, but may have ..
   * chaotic .. side effects.
   * <p/>
   *
   * @param node
   * @param style
   */
  private void performCompleteStyleAttr (LayoutElement node, LayoutStyle style)
  {
    final NamespaceCollection namespaces = getNamespaces();
    final String[] namespaceNames = namespaces.getNamespaces();

    final LayoutContext layoutContext = node.getLayoutContext();
    final AttributeMap attributes = layoutContext.getAttributes();

    for (int i = 0; i < namespaceNames.length; i++)
    {
      final String namespace = namespaceNames[i];
      final NamespaceDefinition ndef = namespaces.getDefinition(namespace);
      if (ndef == null)
      {
        continue;
      }

      final String[] styleAttrs = ndef.getStyleAttribute(layoutContext.getTagName());
      for (int x = 0; x < styleAttrs.length; x++)
      {
        final String attr = styleAttrs[x];
        final Object styleValue = attributes.getAttribute(namespace, attr);
        addStyleFromAttribute(style, styleValue, node);
      }
    }
  }

  private void addStyleFromAttribute(final LayoutStyle style,
                                     final Object styleValue,
                                     final LayoutElement node)
  {
    if (styleValue instanceof String)
    {
      final String styleText = (String) styleValue;
      try
      {
        final LayoutProcess layoutProcess = getLayoutProcess();
        final byte[] bytes = styleText.getBytes("UTF-8");
        final ResourceKey baseKey =
                DocumentContextUtility.getBaseResource
                        (layoutProcess.getDocumentContext());
        final ResourceManager manager = layoutProcess.getResourceManager();
        final ResourceKey key = manager.createKey(bytes);
        final Resource resource = manager.create(key, baseKey, StyleRule.class);

        final CSSDeclarationRule rule =
                (CSSDeclarationRule) resource.getResource();
        if (rule != null)
        {
          copyStyleInformation(style, rule, node);
        }
      }
      catch (Exception e)
      {
        Log.debug("Unable to handle style attribute value.", e);
      }
    }
    else if (styleValue instanceof CSSDeclarationRule)
    {
      copyStyleInformation(style, (CSSDeclarationRule) styleValue, node);
    }
  }

  /**
   * Todo: Make sure that the 'activeStyles' are sorted and then apply them with the
   * lowest style first. All Matching styles have to be added.
   *
   * @param node
   * @param style
   */
  private void performSelectionStep (LayoutElement element,
                                     LayoutStyle style)
  {
    final CSSStyleRule[] activeStyleRules =
            styleRuleMatcher.getMatchingRules(element);

    // sort ...
    Arrays.sort(activeStyleRules, new CSSStyleRuleComparator());
    SelectorWeight oldSelectorWeight = null;
    for (int i = 0; i < activeStyleRules.length; i++)
    {
      final CSSStyleRule activeStyleRule = activeStyleRules[i];
      final CSSSelector selector = activeStyleRule.getSelector();
      final SelectorWeight activeWeight = selector.getWeight();

      if (oldSelectorWeight != null)
      {
        if (oldSelectorWeight.compareTo(activeWeight) > 0)
        {
          oldSelectorWeight = activeWeight;
          continue;
        }
      }

      oldSelectorWeight = activeWeight;
      copyStyleInformation(style, activeStyleRule, element);
    }
  }

  public StyleResolver deriveInstance ()
  {
    return this;
  }

  public State saveState() throws StateException
  {
    DefaultStyleResolverState state = new DefaultStyleResolverState();
    fillState(state);
    state.setRuleMatcher(styleRuleMatcher);
    state.setStrictStyleMode(strictStyleMode);
    return state;
  }
}
