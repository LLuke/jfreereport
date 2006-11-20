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
 * $Id: LibLayoutReportTarget.java,v 1.8 2006/11/11 20:37:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import java.util.Iterator;
import java.util.Map;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.StateException;
import org.jfree.layouting.LayoutProcessState;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.expressions.ExpressionRuntime;
import org.jfree.report.structure.ContentElement;
import org.jfree.report.structure.Element;
import org.jfree.report.structure.Node;
import org.jfree.report.structure.StaticText;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;

/**
 * Creation-Date: 07.03.2006, 18:56:37
 *
 * @author Thomas Morgner
 */
public class LibLayoutReportTarget extends AbstractReportTarget
  implements StatefullReportTarget
{
  protected static class LibLayoutReportTargetState
      implements ReportTargetState
  {
    private LayoutProcessState layoutProcess;
    private ReportJob reportJob;
    private ResourceKey baseResourceKey;
    private ResourceManager resourceManager;
    private NamespaceCollection namespaceCollection;

    public LibLayoutReportTargetState()
    {
    }

    public void fill (LibLayoutReportTarget target) throws StateException
    {
      this.layoutProcess = target.getLayoutProcess().saveState();
      this.reportJob = target.getReportJob();
      this.baseResourceKey = target.getBaseResource();
      this.resourceManager = target.getResourceManager();
      this.namespaceCollection = target.getNamespaces();
    }

    public ReportTarget restore(OutputProcessor out)
        throws StateException
    {
      final LayoutProcess layoutProcess = this.layoutProcess.restore(out);
      return new LibLayoutReportTarget(reportJob,
          baseResourceKey, resourceManager, layoutProcess, namespaceCollection);
    }
  }


  private InputFeed feed;
  private NamespaceCollection namespaces;
  private LayoutProcess layoutProcess;

  /**
   * @param basResourceKey  may be null, if the report has not gone through the parser
   * @param resourceManager may be null, a generic resource manager will be built
   * @param feed
   */
  public LibLayoutReportTarget (final ReportJob reportJob,
                                final ResourceKey baseResourceKey,
                                final ResourceManager resourceManager,
                                final LayoutProcess layoutProcess)
  {
    super(reportJob, resourceManager, baseResourceKey);

    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.layoutProcess = layoutProcess;
    this.feed = layoutProcess.getInputFeed();
  }

  public LibLayoutReportTarget(final ReportJob reportJob,
                               final ResourceKey baseResource,
                               final ResourceManager resourceManager,
                               final LayoutProcess layoutProcess,
                               final NamespaceCollection namespaces)
  {
    this(reportJob, baseResource, resourceManager, layoutProcess);
    this.namespaces = namespaces;
  }

  public ReportTargetState saveState() throws StateException
  {
    final LibLayoutReportTargetState state = new LibLayoutReportTargetState();
    state.fill(this);
    return state;
  }

  public void commit()
  {

  }

  public NamespaceCollection getNamespaces()
  {
    return namespaces;
  }

  public boolean isPagebreakEncountered()
  {
    return layoutProcess.isPagebreakEncountered();
  }

  protected LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
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

      NamespaceDefinition[] namespaces = createDefaultNameSpaces();
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
      dse.printStackTrace();
      throw new ReportProcessingException("Failed to process inputfeed", dse);
    }

  }

  public void processNode (Node node, ExpressionRuntime runtime)
          throws DataSourceException, ReportProcessingException
  {
    if (node instanceof StaticText == false)
    {
      Log.warn ("Unknown Node type encountered: " + node);
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
    AttributeMap attrs = processAttributes(node, runtime);
    handleAttributes(attrs);
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
      AttributeMap attributes = processAttributes(node, runtime);
      handleAttributes(attributes);
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to process inputfeed", e);
    }
  }

  protected NamespaceDefinition findNamespace (String namespace)
  {
    if (namespace == null)
    {
      return null;
    }
    return namespaces.getDefinition(namespace);
  }


  protected void handleAttributes(AttributeMap map)
          throws ReportProcessingException
  {
    try
    {
      InputFeed feed = getInputFeed();
      String[] namespaces = map.getNameSpaces();
      for (int i = 0; i < namespaces.length; i++)
      {
        final String namespace = namespaces[i];
        Map localAttrs = map.getAttributes(namespace);
        Iterator it = localAttrs.entrySet().iterator();
        while (it.hasNext())
        {
          Map.Entry entry = (Map.Entry) it.next();
          feed.setAttribute(namespace, (String) entry.getKey(), entry.getValue());
        }
      }
    }
    catch (InputFeedException e)
    {
      throw new ReportProcessingException("Failed to set attribute", e);
    }
  }

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


  public void resetPagebreakFlag()
  {
    getInputFeed().resetPageBreakFlag();
  }

  public String getExportDescriptor()
  {
    return getLayoutProcess().getOutputMetaData().getExportDescriptor();
  }
}
