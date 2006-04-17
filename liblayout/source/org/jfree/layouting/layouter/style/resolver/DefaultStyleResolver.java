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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver;

import java.util.Arrays;
import java.util.Iterator;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.layouting.input.style.selectors.SelectorWeight;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.CSSStyleRuleComparator;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;


/**
 * Creation-Date: 05.12.2005, 19:15:57
 *
 * @author Thomas Morgner
 */
public class DefaultStyleResolver implements StyleResolver
{
  //private StyleSheet defaultStyleSheet;
  private LayoutStyle initialStyle;
  private LayoutProcess layoutProcess;
  private DocumentContext documentContext;
  private NamespaceCollection namespaces;

  private StyleKey[] keys;
  private StyleRuleMatcher styleRuleMatcher;

  public DefaultStyleResolver()
  {
  }

  public void initialize(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
    this.documentContext = layoutProcess.getDocumentContext();
    this.keys = new StyleKey[0];
    this.initialStyle = new LayoutStyle(null);
    this.styleRuleMatcher =
            DocumentContextUtility.getStyleRuleMatcher(documentContext);
    this.styleRuleMatcher.initialize(layoutProcess);
    this.namespaces = documentContext.getNamespaces();

    try
    {
      final ResourceManager manager = layoutProcess.getResourceManager();
      final Resource resource = manager.createDirectly
              ("res://org/jfree/layouting/initial.css", StyleSheet.class);
      final StyleSheet initialStyleSheet = (StyleSheet) resource.getResource();


      int rc = initialStyleSheet.getRuleCount();
      for (int i = 0; i < rc; i++)
      {
        StyleRule rule = initialStyleSheet.getRule(i);
        if (rule instanceof CSSDeclarationRule)
        {
          final CSSDeclarationRule drule = (CSSDeclarationRule) rule;
          copyStyleInformation(initialStyle, drule);
        }
      }
    }
    catch (Exception e)
    {
      // Not yet handled ...
      e.printStackTrace();
    }
  }

  private void copyStyleInformation(LayoutStyle target, CSSDeclarationRule rule)
  {
    Iterator keys = rule.getPropertyKeys();
    while (keys.hasNext())
    {
      StyleKey key = (StyleKey) keys.next();
      CSSValue value = rule.getPropertyCSSValue(key);
      target.setValue(key, value);
    }
  }

  protected LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  protected LayoutStyle getInitialStyle()
  {
    return initialStyle;
  }

  protected DocumentContext getDocumentContext()
  {
    return documentContext;
  }

  public void resolveStyle(LayoutElement node)
  {
    // this is a three stage process
    final LayoutStyle style = node.getStyle();

    keys = StyleKeyRegistry.getRegistry().getKeys(keys);

    // Stage 0: Initialize with the built-in defaults
    // Stage 1a: Add the parent styles (but only the one marked as inheritable).
    final LayoutNode parent = node.getParent();
    final LayoutStyle parentStyle;
    if (parent != null)
    {
      parentStyle = parent.getStyle();
    }
    else
    {
      parentStyle = initialStyle;
    }

    for (int i = 0; i < keys.length; i++)
    {
      StyleKey key = keys[i];
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
    performSelectionStep(node, style);

    // Stage 1c: Add the contents of the style attribute, if there is one ..
    performStyleAttr(node, style);

    // Stage 2: Compute the 'specified' set of values.

    // Stage 2a: Find all explicitly inherited styles and add them from the parent.
    //           if we have no inherited styles, grab them from the default
    //           stylesheet.
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

    // Stage 3:  Compute the computed value set.
    ResolverFactory.getInstance().performResolve
            (getLayoutProcess(), node, style);

  }

  /**
   * Check, whether there is a known style attribute and if so, grab its value.
   * <p/>
   * Todo: This should not be hardcoded.
   *
   * @param node
   * @param style
   */
  private void performStyleAttr(LayoutElement node, LayoutStyle style)
  {
    final String namespace = node.getNamespace();
    if (namespace == null)
    {
      return;
    }
    final NamespaceDefinition ndef = namespaces.getDefinition(namespace);
    if (ndef == null)
    {
      return;
    }

    final String classAttribute = ndef.getClassAttribute(node.getName());
    if (classAttribute == null)
    {
      return;
    }

    final Object styleValue = node.getAttribute(namespace, classAttribute);
    addStyleFromAttribute(style, styleValue);
  }

  private void addStyleFromAttribute(final LayoutStyle style,
                                     final Object styleValue)
  {
    if (styleValue instanceof String)
    {
      final String styleText = (String) styleValue;
      try
      {
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
          copyStyleInformation(style, rule);
        }
      }
      catch (Exception e)
      {
        Log.debug("Unable to handle style attribute value.", e);
      }
    }
    else if (styleValue instanceof CSSDeclarationRule)
    {
      copyStyleInformation(style, (CSSDeclarationRule) styleValue);
    }
  }

  /**
   * Todo: Make sure that the 'activeStyles' are sorted and then apply them with
   * the lowest style first. All Matching styles have to be added.
   *
   * @param node
   * @param style
   */
  private void performSelectionStep(LayoutElement node,
                                    LayoutStyle style)
  {
    final CSSStyleRule[] activeStyleRules =
            styleRuleMatcher.getMatchingRules(node);

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
      copyStyleInformation(style, activeStyleRule);
    }
  }

  /**
   * Returns the built-in default value, which is our last resort if the layout
   * could not be computed otherwise.
   *
   * @param key
   * @return blah
   */
  public CSSValue getDefaultValue(StyleKey key)
  {
    return initialStyle.getValue(key);
  }

  public StyleResolver deriveInstance()
  {
    return this;
  }
}
