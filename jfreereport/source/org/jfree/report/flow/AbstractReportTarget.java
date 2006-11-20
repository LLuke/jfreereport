/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * AbstractReportTarget.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractReportTarget.java,v 1.2 2006/11/11 20:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import java.util.Iterator;
import java.util.Map;

import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.expressions.Expression;
import org.jfree.report.expressions.ExpressionRuntime;
import org.jfree.report.structure.Element;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 03.07.2006, 16:31:12
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportTarget implements ReportTarget
{
  private ResourceManager resourceManager;
  private ResourceKey baseResource;
  private ReportJob reportJob;

  protected AbstractReportTarget(final ReportJob reportJob,
                                 final ResourceManager resourceManager,
                                 final ResourceKey baseResource)
  {
    if (reportJob == null)
    {
      throw new NullPointerException();
    }
    this.baseResource = baseResource;
    this.reportJob = reportJob;

    if (resourceManager == null)
    {
      this.resourceManager = new ResourceManager();
      this.resourceManager.registerDefaults();
    }
    else
    {
      this.resourceManager = resourceManager;
    }
  }

  private void mergeDeclarationRule (final CSSDeclarationRule target,
                                     final CSSDeclarationRule source)
  {
    Iterator it = source.getPropertyKeys();
    while (it.hasNext())
    {
      StyleKey key = (StyleKey) it.next();
      CSSValue value = source.getPropertyCSSValue(key);
      boolean sourceImportant = source.isImportant(key);
      boolean targetImportant = target.isImportant(key);
      if (targetImportant)
      {
        continue;
      }
      target.setPropertyValue(key, value);
      target.setImportant(key, sourceImportant);
    }
  }

  private CSSDeclarationRule processStyleAttribute
          (final Object styleAttributeValue,
           final Element node,
           final ExpressionRuntime runtime,
           CSSDeclarationRule targetRule)
          throws DataSourceException
  {
    if (targetRule == null)
    {
      try
      {
        targetRule = (CSSDeclarationRule) node.getStyle().clone();
      }
      catch (CloneNotSupportedException e)
      {
        targetRule = new CSSStyleRule(null, null);
      }
    }

    if (styleAttributeValue instanceof String)
    {
      // ugly, we have to parse that thing. Cant think of nothing
      // worse than that.
      final String styleText = (String) styleAttributeValue;
      try
      {
        final byte[] bytes = styleText.getBytes("UTF-8");
        final ResourceKey key = resourceManager.createKey(bytes);
        final Resource resource = resourceManager.create
                (key, baseResource, StyleRule.class);

        final CSSDeclarationRule parsedRule =
                (CSSDeclarationRule) resource.getResource();
        mergeDeclarationRule(targetRule, parsedRule);
      }
      catch (Exception e)
      {
        // ignore ..
        e.printStackTrace();
      }
    }
    else if (styleAttributeValue instanceof CSSStyleRule)
    {
      final CSSStyleRule styleRule =
              (CSSStyleRule) styleAttributeValue;
      mergeDeclarationRule(targetRule, styleRule);
    }

    // ok, not lets fill in the stuff from the style expressions ..
    final Map styleExpressions = node.getStyleExpressions();
    final Iterator styleExIt = styleExpressions.entrySet().iterator();

    while (styleExIt.hasNext())
    {
      final Map.Entry entry = (Map.Entry) styleExIt.next();
      final StyleKey name = (StyleKey) entry.getKey();
      final Expression expression = (Expression) entry.getValue();
      try
      {
        expression.setRuntime(runtime);
        final Object value = expression.computeValue();
        if (value instanceof CSSValue)
        {
          targetRule.setPropertyValue(name, (CSSValue) value);
        }
        else if (value != null)
        {
          targetRule.setPropertyValueAsString(name, String.valueOf(value));
        }
      }
      finally
      {
        expression.setRuntime(null);
      }
    }
    return targetRule;
  }

  private AttributeMap collectAttributes (final Element node,
                                          final ExpressionRuntime runtime)
          throws DataSourceException
  {
    final AttributeMap attributes = node.getAttributeMap();
    final AttributeMap attributeExpressions = node.getAttributeExpressionMap();
    final String[] namespaces = attributeExpressions.getNameSpaces();
    for (int i = 0; i < namespaces.length; i++)
    {
      final String namespace = namespaces[i];
      final Map attrEx = attributeExpressions.getAttributes(namespace);

      final Iterator attributeExIt = attrEx.entrySet().iterator();
      while (attributeExIt.hasNext())
      {
        final Map.Entry entry = (Map.Entry) attributeExIt.next();
        final String name = (String) entry.getKey();
        final Expression expression = (Expression) entry.getValue();
        try
        {
          expression.setRuntime(runtime);
          final Object value = expression.computeValue();
          attributes.setAttribute(namespace, name, value);
        }
        finally
        {
          expression.setRuntime(null);
        }
      }
    }
    return attributes;
  }

  protected AttributeMap processAttributes (final Element node,
                                    final ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    final AttributeMap attributes = collectAttributes(node, runtime);
    CSSDeclarationRule rule = null;

    final AttributeMap retval = new AttributeMap();

    final String[] attrNamespaces = attributes.getNameSpaces();
    for (int i = 0; i < attrNamespaces.length; i++)
    {
      final String namespace = attrNamespaces[i];
      final Map attributeMap = attributes.getAttributes(namespace);
      if (attributeMap == null)
      {
        continue;
      }

      final NamespaceDefinition nsDef = findNamespace(namespace);
      final Iterator attributeIt = attributeMap.entrySet().iterator();
      while (attributeIt.hasNext())
      {
        final Map.Entry entry = (Map.Entry) attributeIt.next();
        final String key = (String) entry.getKey();
        if (isStyleAttribute(nsDef, node.getType(), key))
        {
          final Object styleAttributeValue = entry.getValue();
          rule = processStyleAttribute(styleAttributeValue, node, runtime, rule);
        }
        else
        {
          retval.setAttribute(namespace, key, entry.getValue());
        }
      }
    }

    // Just in case there was no style-attribute but there are style-expressions
    if (rule == null)
    {
      rule = processStyleAttribute(null, node, runtime, rule);
    }

    if (rule != null && rule.getSize() > 0)
    {
      retval.setAttribute(Namespaces.LIBLAYOUT_NAMESPACE, "style", rule);
    }

    return retval;
  }

  private boolean isStyleAttribute (NamespaceDefinition def,
                                    String elementName,
                                    String attrName)
  {
    if (def == null)
    {
      return false;
    }

    String[] styleAttr = def.getStyleAttribute(elementName);
    for (int i = 0; i < styleAttr.length; i++)
    {
      String styleAttrib = styleAttr[i];
      if (attrName.equals(styleAttrib))
      {
        return true;
      }
    }
    return false;
  }

  protected abstract NamespaceDefinition findNamespace (String namespace);

  protected NamespaceDefinition[] createDefaultNameSpaces()
  {
    return Namespaces.createFromConfig
            (reportJob.getConfiguration(), "org.jfree.report.namespaces.",
                    getResourceManager());
  }

  protected ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  protected ResourceKey getBaseResource()
  {
    return baseResource;
  }

  public ReportJob getReportJob ()
  {
    return reportJob;
  }

}
