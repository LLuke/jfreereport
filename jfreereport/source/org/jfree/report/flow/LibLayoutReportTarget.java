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
 * LibLayoutReportTarget.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LibLayoutReportTarget.java,v 1.5 2006/05/06 12:59:25 taqua Exp $
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
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.StaticText;
import org.jfree.report.util.AttributeMap;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 07.03.2006, 18:56:37
 *
 * @author Thomas Morgner
 */
public class LibLayoutReportTarget implements ReportTarget
{
  private ResourceKey baseResource;
  private ResourceManager resourceManager;
  private InputFeed feed;
  private ReportJob reportJob;
  private NamespaceCollection namespaces;

  /**
   * @param basResourceKey  may be null, if the report has not gone through the parser
   * @param resourceManager may be null, a generic resource manager will be built
   * @param feed
   */
  public LibLayoutReportTarget (final ReportJob reportJob,
                                final ResourceKey basResourceKey,
                                final ResourceManager resourceManager,
                                final InputFeed feed)
  {
    if (feed == null)
    {
      throw new NullPointerException();
    }
    if (reportJob == null)
    {
      throw new NullPointerException();
    }
    this.reportJob = reportJob;
    this.feed = feed;
    this.baseResource = basResourceKey;

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

  public ReportJob getReportJob ()
  {
    return reportJob;
  }

  protected InputFeed getInputFeed ()
  {
    return feed;
  }

  public void startReport (JFreeReport report)
          throws DataSourceException, ReportProcessingException
  {
    try
    {
      final InputFeed feed = getInputFeed();
      feed.startDocument();
      feed.startMetaInfo();

      feed.addDocumentAttribute(DocumentContext.BASE_RESOURCE_ATTR, report.getBaseResource());
      feed.addDocumentAttribute(DocumentContext.RESOURCE_MANAGER_ATTR, report.getResourceManager());

      String strictStyleMode = "false";
      if ("true".equals(strictStyleMode))
      {
        feed.addDocumentAttribute(DocumentContext.STRICT_STYLE_MODE, Boolean.TRUE);
      }


      NamespaceDefinition[] namespaces = Namespaces.createFromConfig
              (reportJob.getConfiguration(), "org.jfree.report.namespaces.",
                      resourceManager);
      for (int i = 0; i < namespaces.length; i++)
      {
        final NamespaceDefinition definition = namespaces[i];
        feed.startMetaNode();
        feed.setMetaNodeAttribute("type", "namespace");
        feed.setMetaNodeAttribute("definition", definition);
        feed.endMetaNode();
      }

      final int size = report.getStyleSheetCount();
      for (int i = 0; i < size; i++)
      {
        final StyleSheet styleSheet = report.getStyleSheet(i);
        feed.startMetaNode();
        feed.setMetaNodeAttribute("type", "style");
        feed.setMetaNodeAttribute("#content", styleSheet);
        feed.endMetaNode();
      }

      feed.endMetaInfo();
      this.namespaces = feed.getNamespaceCollection();
    }
    catch (InputFeedException dse)
    {
      throw new ReportProcessingException("Failed to process inputfeed", dse);
    }

  }

  public void processNode (Node node, ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    if (node instanceof StaticText == false)
    {
      return;
    }

    final StaticText text = (StaticText) node;
    final InputFeed feed = getInputFeed();
    try
    {
      feed.addContent(text.getText());
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
  }

  public void processContentElement (final ContentElement node,
                                     final DataFlags value,
                                     final ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    final InputFeed feed = getInputFeed();
    processAttributes(node, runtime);
    //Object styleAttributeValue = handleAttributes(node, runtime);
    //CSSDeclarationRule rule = createStyle(styleAttributeValue, node, runtime);
    try
    {
      //feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "style", rule);
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "content", value.getValue());
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isChanged", String.valueOf(value.isChanged()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isDate", String.valueOf(value.isDate()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isNegative", String.valueOf(value.isNegative()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isNull", String.valueOf(value.isNull()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isNumber", String.valueOf(value.isNumeric()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isPositive", String.valueOf(value.isPositive()));
      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "isZero", String.valueOf(value.isZero()));
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
  }

  public void startElement (Element node, ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    final String namespace = node.getNamespace();
    if (JFreeReportInfo.COMPATIBILITY_NAMESPACE.equals(namespace))
    {
      // hoho, a compatibility layer is what we need here. Whenever we hit
      // that namespace, this means that something strange is going on, which
      // needs a smart conversion.
      //
      // Luckily, for now, there are no compatibility elements required.
      return;
    }

    final InputFeed feed = getInputFeed();
    try
    {
      feed.startElement(namespace, node.getType());
      processAttributes(node, runtime);
//      Object styleAttributeValue = handleAttributes(node, runtime);
//
//      CSSDeclarationRule rule = createStyle(styleAttributeValue, node, runtime);
//      feed.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "style", rule);
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
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
        final Object value = expression.getValue();
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
          final Object value = expression.getValue();
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

  private void processAttributes (final Element node,
                                  final ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    final AttributeMap attributes = collectAttributes(node, runtime);

    final InputFeed feed = getInputFeed();

    try
    {
      CSSDeclarationRule rule = null;

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
            feed.setAttribute(namespace, key, entry.getValue());
          }
        }
      }

      if (rule != null && rule.getSize() > 0)
      {
        feed.setAttribute(Namespaces.LIBLAYOUT_NAMESPACE, "style", rule);
      }
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
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

  private NamespaceDefinition findNamespace (String namespace)
  {
    if (namespace == null)
    {
      return null;
    }

    return namespaces.getDefinition(namespace);
  }

//
//    private Object handleAttributes(final Element node,
//                                  final ExpressionRuntime runtime)
//          throws
//          DataSourceException, ReportProcessingException
//  {
//    final AttributeMap attributes = node.getAttributeMap();
//    final AttributeMap attributeExpressions = node.getAttributeExpressionMap();
//    final String[] namespaces = attributeExpressions.getNameSpaces();
//    for (int i = 0; i < namespaces.length; i++)
//    {
//      final String namespace = namespaces[i];
//      final Map attrEx = attributeExpressions.getAttributes(namespace);
//
//      final Iterator attributeExIt = attrEx.entrySet().iterator();
//      while (attributeExIt.hasNext())
//      {
//        final Map.Entry entry = (Map.Entry) attributeExIt.next();
//        final String name = (String) entry.getKey();
//        final Expression expression = (Expression) entry.getValue();
//        try
//        {
//          expression.setRuntime(runtime);
//          final Object value = expression.getValue();
//          attributes.setAttribute(namespace, name, value);
//        }
//        finally
//        {
//          expression.setRuntime(null);
//        }
//      }
//    }
//
//    final InputFeed feed = getInputFeed();
//
//    Object styleAttributeValue = null;
//
//    try
//    {
//
//
//      final String[] attrNamespaces = attributes.getNameSpaces();
//      for (int i = 0; i < attrNamespaces.length; i++)
//      {
//        final String namespace = attrNamespaces[i];
//        final boolean jfreeReportNamespace =
//                JFreeReportInfo.REPORT_NAMESPACE.equals(namespace);
//        final Map attributeMap = attributes.getAttributes(namespace);
//        if (attributeMap == null)
//        {
//          continue;
//        }
//        final Iterator attributeIt = attributeMap.entrySet().iterator();
//        while (attributeIt.hasNext())
//        {
//          final Map.Entry entry = (Map.Entry) attributeIt.next();
//          final String key = (String) entry.getKey();
//          if (jfreeReportNamespace && "style".equals(key))
//          {
//            styleAttributeValue = entry.getValue();
//          }
//          else
//          {
//            feed.setAttribute(namespace, key, entry.getValue());
//          }
//        }
//      }
//    }
//    catch (InputFeedException e)
//    {
//      throw new ReportProcessingException("Failed to process inputfeed", e);
//    }
//    return styleAttributeValue;
//  }
//

  public void endElement (Element node, ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    final String namespace = node.getNamespace();
    if (JFreeReportInfo.COMPATIBILITY_NAMESPACE.equals(namespace))
    {
      // hoho, a compatibility layer is what we need here.
      return;
    }

    final InputFeed feed = getInputFeed();
    try
    {
      feed.endElement();
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
  }

  public void endReport (JFreeReport report)
          throws DataSourceException,
          ReportProcessingException
  {
    try
    {
      getInputFeed().endDocument();
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
  }
}
