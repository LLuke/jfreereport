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
 * DefaultStyleResolver.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.jfree.layouting.input.StyleSheetLoader;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.selectors.SelectorWeight;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.ProcessAttributeName;
import org.jfree.layouting.layouter.style.DocumentStyleLoader;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;


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
  private CSSStyleRule[] activeStyleRules;
  private boolean initialized;
  private DocumentContext documentContext;

  public DefaultStyleResolver (final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
    this.documentContext = layoutProcess.getDocumentContext();

    StyleSheet initialStyleSheet = StyleSheetLoader.getInstance()
            .getInitialValuesSheet();
    if (StyleSheetLoader.getInstance().isErrorInitialValues())
    {
      Log.error(
              "There was an unexpected error loading the initial stylesheet.");
    }

    this.initialStyle = new LayoutStyle();
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

  private void copyStyleInformation (LayoutStyle target, CSSDeclarationRule rule)
  {
    Iterator keys = rule.getPropertyKeys();
    while (keys.hasNext())
    {
      StyleKey key = (StyleKey) keys.next();
      CSSValue value = rule.getPropertyCSSValue(key);
      target.setValue(key, value);
    }
  }

  public LayoutProcess getLayoutProcess ()
  {
    return layoutProcess;
  }

  public LayoutStyle getInitialStyle ()
  {
    return initialStyle;
  }

  public DocumentContext getDocumentContext ()
  {
    return documentContext;
  }


  public void resolveStyle (LayoutElement node)
  {
    initialize();

    // this is a three stage process
    final LayoutStyle style = node.getStyle();

    // Stage 0: Initialize with the built-in defaults
    StyleKey[] keys = StyleKeyRegistry.getRegistry().getKeys();
    for (int i = 0; i < keys.length; i++)
    {
      StyleKey key = keys[i];
      style.setValue(key, initialStyle.getValue(key));
    }

    // Stage 1a: Add the parent styles (but one the one marked as inherited.
    final LayoutNode parent = node.getParent();
    if (parent != null)
    {
      LayoutStyle parentStyle = parent.getStyle();
      for (int i = 0; i < keys.length; i++)
      {
        StyleKey key = keys[i];
        if (key.isInherited())
        {
          style.setValue(key, parentStyle.getValue(key));
        }
      }
    }

    // Stage 1b: Find all matching stylesheets for the given element
    SelectorWeight oldSelectorWeight = null;
    for (int i = 0; i < activeStyleRules.length; i++)
    {
      CSSStyleRule activeStyleRule = activeStyleRules[i];
      final SelectorWeight activeWeight = activeStyleRule.getSelector()
              .getWeight();
      if (isMatch(node, activeStyleRule.getSelector()) == false)
      {
        continue;
      }
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

    // Stage 1c: Add the contents of the style attribute, if there is one ..
    final Object styleValue = node.getAttribute("style");
    if (styleValue instanceof String)
    {
      final String styleText = (String) styleValue;
      final StyleSheetLoader loader = StyleSheetLoader.getInstance();
      final CSSDeclarationRule rule = loader.parseStyleText
              (styleText, documentContext.getBaseURL());
      if (rule != null)
      {
        copyStyleInformation(style, rule);
      }
    }
    else if (styleValue instanceof CSSDeclarationRule)
    {
      copyStyleInformation(style, (CSSDeclarationRule) styleValue);
    }

    // Stage 2: Compute the 'specified' set of values.

    // Stage 2a: Find all explicitly inherited styles and add them from the parent.
    //           if we have no inherited styles, grab them from the default
    //           stylesheet.
    LayoutStyle parentStyle;
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
    }

    // Stage 3:  Compute the computed value set.
    ResolverFactory.getInstance().performResolve(getLayoutProcess(), node, style);

  }


  private boolean isMatch (final LayoutElement node, final Selector selector)
  {
    final short selectorType = selector.getSelectorType();
    switch (selectorType)
    {
      case Selector.SAC_ANY_NODE_SELECTOR:
        return true;
      case Selector.SAC_ROOT_NODE_SELECTOR:
        return node.getParent() == null;
      case Selector.SAC_NEGATIVE_SELECTOR:
      {
        NegativeSelector negativeSelector = (NegativeSelector) selector;
        return isMatch(node, negativeSelector) == false;
      }
      case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
      {
        SiblingSelector silbSelect = (SiblingSelector) selector;
        //if (isMatch()silbSelect.getSelector()
        return isSilblingMatch(node, silbSelect);
      }
      case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
      {
        if ("pseudo-element".equals
                (node.getProcessAttribute(ProcessAttributeName.TYPE)) == false)
        {
          return false;
        }
      }
      case Selector.SAC_ELEMENT_NODE_SELECTOR:
      {
        ElementSelector es = (ElementSelector) selector;
        return ObjectUtilities.equal(node.getName(), es.getLocalName());
      }
      case Selector.SAC_CHILD_SELECTOR:
      {
        DescendantSelector ds = (DescendantSelector) selector;
        if (isMatch(node, ds.getSimpleSelector()) == false)
        {
          return false;
        }
        LayoutElement parent = node.getParent();
        return (isMatch(parent, ds.getAncestorSelector()));
      }
      case Selector.SAC_DESCENDANT_SELECTOR:
      {
        DescendantSelector ds = (DescendantSelector) selector;
        if (isMatch(node, ds.getSimpleSelector()) == false)
        {
          return false;
        }
        return (isDescendantMatch(node, ds.getAncestorSelector()));
      }
      case Selector.SAC_CONDITIONAL_SELECTOR:
      {
        ConditionalSelector cs = (ConditionalSelector) selector;
        if (isMatch(node, cs.getSimpleSelector()) == false)
        {
          return false;
        }
        return evaluateCondition(node, cs.getCondition());
      }
      default:
        return false;
    }
  }

  private boolean evaluateCondition (final LayoutElement node,
                                     final Condition condition)
  {
    switch (condition.getConditionType())
    {
      case Condition.SAC_AND_CONDITION:
      {
        CombinatorCondition cc = (CombinatorCondition) condition;
        return (evaluateCondition(node, cc.getFirstCondition()) &&
                evaluateCondition(node, cc.getSecondCondition()));
      }
      case Condition.SAC_OR_CONDITION:
      {
        CombinatorCondition cc = (CombinatorCondition) condition;
        return (evaluateCondition(node, cc.getFirstCondition()) ||
                evaluateCondition(node, cc.getSecondCondition()));
      }
      case Condition.SAC_ATTRIBUTE_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final String attr = (String) node.getAttribute(ac.getLocalName());
        if (ac.getSpecified() == false)
        {
          return attr != null;
        }
        return ObjectUtilities.equal(attr, ac.getValue());
      }
      case Condition.SAC_CLASS_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final String attr = (String) node.getAttribute("class");
        return isOneOfAttributes(attr, ac.getValue());
      }
      case Condition.SAC_ID_CONDITION:
      {
        AttributeCondition ac = (AttributeCondition) condition;
        return ObjectUtilities.equal(ac.getValue(), node.getId());
      }
      case Condition.SAC_LANG_CONDITION:
      {
        AttributeCondition ac = (AttributeCondition) condition;
        final String lang = (String) node.getProcessAttribute
                (ProcessAttributeName.LANGUAGE);
        return isBeginHyphenAttribute(lang, ac.getValue());
      }
      case Condition.SAC_NEGATIVE_CONDITION:
      {
        NegativeCondition nc = (NegativeCondition) condition;
        return evaluateCondition(node, nc.getCondition());
      }
      case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final String attr = (String) node.getAttribute(ac.getLocalName());
        return isOneOfAttributes(attr, ac.getValue());
      }
      case Condition.SAC_PSEUDO_CLASS_CONDITION:
      {
        AttributeCondition ac = (AttributeCondition) condition;
        return ObjectUtilities
                .equal(ac.getValue(), node.getProcessAttribute(ProcessAttributeName.PSEUDO_CLASS));
      }
      case Condition.SAC_ONLY_CHILD_CONDITION:
      case Condition.SAC_ONLY_TYPE_CONDITION:
      case Condition.SAC_POSITIONAL_CONDITION:
      case Condition.SAC_CONTENT_CONDITION:
      default:
      {
        // todo
        return false;
      }
    }
  }

  private boolean isOneOfAttributes (String attrValue, String value)
  {
    if (attrValue == null)
    {
      return false;
    }
    StringTokenizer strTok = new StringTokenizer(attrValue);
    while (strTok.hasMoreTokens())
    {
      String token = strTok.nextToken();
      if (ObjectUtilities.equal(token, value))
      {
        return true;
      }
    }
    return false;
  }

  private boolean isBeginHyphenAttribute (String attrValue, String value)
  {
    if (attrValue == null)
    {
      return false;
    }
    if (value == null)
    {
      return false;
    }
    return (attrValue.startsWith(value));

  }

  private boolean isDescendantMatch (final LayoutElement node,
                                     final Selector selector)
  {
    LayoutElement parent = node.getParent();
    while (parent != null)
    {
      if (isMatch(parent, selector))
      {
        return true;
      }
      parent = parent.getParent();
    }
    return false;
  }

  private boolean isSilblingMatch (final LayoutElement node,
                                   final SiblingSelector select)
  {
    LayoutNode pred = node.getPredecessor();
    while (pred != null)
    {
      if (pred instanceof LayoutElement)
      {
        return isMatch((LayoutElement) pred, select);
      }

      pred = pred.getPredecessor();
    }
    return false;
  }

  private synchronized void initialize ()
  {
    if (initialized)
    {
      return;
    }
    // at this point, we have processed enough of the input to load
    // all stylesheets from the header and to build the complete style-set

    // first check, whether we already have a valid stylesheet set.
    Object styles = documentContext.getMetaAttribute
            ("-liblayout-cached-style-resolver-styles");
    if (styles instanceof CSSStyleRule[])
    {
      activeStyleRules = (CSSStyleRule[]) styles;
    }
    else
    {
      final DocumentStyleLoader loader =
              new DocumentStyleLoader(documentContext);
      activeStyleRules = loader.parseDocument();
    }

    initialized = true;
  }

  /**
   * Returns the built-in default value, which is our last resort if the layout could not
   * be computed otherwise.
   *
   * @param key
   * @return
   */
  public CSSValue getDefaultValue (StyleKey key)
  {
    return initialStyle.getValue(key);
  }
}
